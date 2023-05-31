package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.schema.VarcharSqlType
import org.ktorm.support.mysql.MySqlDialect
import org.ktorm.support.mysql.insertOrUpdate
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.controller.database.tables.*
import rubiksworld.model.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Days after the order was placed in order to ship.
 */
private const val SHIPPING_TIME_DAYS = 2

/**
 * [DatabaseController] implementation.
 */
open class DatabaseControllerImpl : DatabaseController {

    private lateinit var database: Database

    override fun initDatabase() {
        database = Database.connect(
            "jdbc:mysql://localhost:3306/rubiksworld",
            user = "root", password = "giodatabase",
            dialect = MySqlDialect()
        )
    }

    override fun searchModels(filters: ModelsSearchFilters): List<Model> {
        return with(filters) {
            val textLike = "%$text%"
            database.models
                .filter {
                    text.isBlank() or
                            it.name.like(textLike) or
                            it.maker.like(textLike) or
                            it.category.like(textLike)
                }
                .filter { !onlySpeedCubes or it.isSpeedCube }
                .filter { !onlyStickerless or it.isStickerless }
                .filter { !onlyMagnetic or it.isMagnetic }
                .toList()
        }
    }

    override fun getCustomizableParts(model: Model): List<CustomizablePart> {
        return database.customizableParts
            .filter { it.modelName eq model.name }
            .filter { it.modelMaker eq model.maker }
            .toList()
    }

    override fun getAvailableCustomizations(part: CustomizablePart): List<Customization> {
        return database.customizations
            .filter { it.modelName eq part.model.name }
            .filter { it.modelMaker eq part.model.maker }
            .filter { it.part eq part.part }
            .toList()
    }

    override fun getAllUsers(): List<User> {
        return database.users.toList()
    }

    override fun getUser(nickname: String): User? {
        return database.users
            .find { it.nickname eq nickname }
    }

    override fun findUser(query: String, except: User?): List<User> {
        return database.users
            .filter {
                concat(
                    it.nickname.asExpression(), it.name.asExpression(), ArgumentExpression(" ", VarcharSqlType), it.surname.asExpression()
                ) like "%$query%"
            }
            .filter { it.nickname neq (except?.nickname ?: "") }
            .toList()
    }

    override fun insertUser(nickname: String, name: String, surname: String): User {
        database.insertOrUpdate(Users) {
            set(it.nickname, nickname)
            set(it.name, name)
            set(it.surname, surname)

            onDuplicateKey {
                set(it.name, name)
                set(it.surname, surname)
            }
        }

        return getUser(nickname)!!
    }

    override fun updateUserInfo(
        user: User,
        name: String, surname: String,
        city: String, zip: String,
        email: String, address: String,
        phoneNumber: String?
    ) {
        user.name = name
        user.surname = surname
        user.city = city
        user.zip = zip
        user.email = email
        user.address = address
        user.phoneNumber = phoneNumber
        user.flushChanges()
    }

    override fun insertModelVersion(model: Model, customizations: List<Customization>): ModelVersion {
        val modelVersion = ModelVersion {
            this.model = model
        }
        database.modelVersions.add(modelVersion)

        customizations.forEach { customization ->
            val application = Application {
                this.customization = customization
                this.modelVersion = modelVersion
            }
            database.applications.add(application)
        }

        return modelVersion
    }

    override fun getModelVersionPrice(modelVersion: ModelVersion): Double {
        val customizationsPrice = database.from(Applications)
            .leftJoin(Customizations,
                on = (Applications.part eq Customizations.part)
                        and (Applications.change eq Customizations.change)
                        and (Applications.modelName eq Customizations.modelName)
                        and (Applications.modelMaker eq Customizations.modelMaker))
            .select(sum(Customizations.price))
            .where(Applications.modelVersionId eq modelVersion.id)
            .map { it.getDouble(1) }
            .firstOrNull() ?: .0

        val model = getFullModelInfo(modelVersion.model)
        val subtotal = model.price + customizationsPrice
        return model.discountPercentage?.let { calcDiscountedPrice(subtotal, it) } ?: subtotal
    }

    override fun getAppliedCustomizations(modelVersion: ModelVersion): List<Application> {
        return database.applications
            .filter { it.modelVersionId eq modelVersion.id }
            .toList()
    }

    override fun getFullModelInfo(partialModel: Model): Model {
        return database.models
            .filter { it.name eq partialModel.name }
            .first { it.maker eq partialModel.maker }
    }

    override fun addToCart(user: User, modelVersion: ModelVersion) {
        val cartPresence = CartPresence {
            this.user = user
            this.modelVersion = modelVersion
        }
        database.cartPresences.add(cartPresence)
    }

    override fun removeFromCart(user: User, modelVersion: ModelVersion) {
        database.cartPresences.removeIf {
            (it.userNickname eq user.nickname) and
                    (it.modelVersionId eq modelVersion.id)
        }
    }

    override fun getCart(user: User): List<ModelVersion> {
        return database.cartPresences
            .filter { it.userNickname eq user.nickname }
            .map { it.modelVersion }
    }

    override fun getCartSubtotal(user: User): Double {
        return getCart(user).sumOf { getModelVersionPrice(it) }
    }

    override fun getCartTotal(user: User, coupons: List<Coupon>): Double {
        var subtotal = getCartSubtotal(user)
        coupons
            .sortedBy { it.type.ordinal }
            .forEach { subtotal = it.applied(subtotal) }

        return subtotal + user.shippingPrice
    }

    override fun canCheckout(user: User): Boolean {
        return getCartSubtotal(user) >= user.minimumSubtotal
    }

    override fun insertOrderFromCart(user: User, coupons: List<Coupon>) {
        val orderDate = LocalDate.now()

        // Getting next order ID for the current date
        val id = database.orders
            .filter { it.orderDate eq orderDate }
            .maxBy { it.id }?.plus(1) ?: 0

        val order = Order {
            this.id = id
            this.orderDate = orderDate
            this.orderTime = LocalTime.now()
            this.shippingDate = LocalDate.now().plusDays(SHIPPING_TIME_DAYS.toLong())
            this.total = getCartTotal(user, coupons)
            this.user = user
        }

        database.orders.add(order)

        // Pushing coupon applications
        coupons.forEach { coupon ->
            val discount = Discount {
                this.order = order
                this.coupon = coupon
            }
            database.discounts.add(discount)
        }

        // Pushing model versions
        getCart(user).forEach { modelVersion ->
            val orderPresence = OrderPresence {
                this.order = order
                this.modelVersion = modelVersion
            }
            database.orderPresences.add(orderPresence)
        }

        // Emptying user's cart
        database.cartPresences.removeIf { it.userNickname eq user.nickname }
    }

    override fun getOrders(user: User): List<Order> {
        return database.orders
            .filter { it.userNickname eq user.nickname }
            .sortedByDescending { it.orderDate }
            .toList()
    }

    override fun getModelVersionsFromOrder(order: Order): List<ModelVersion> {
        return database.orderPresences
            .filter { (it.orderId eq order.id) and (it.orderDate eq order.orderDate) }
            .map { it.modelVersion }
    }

    override fun addToWishlist(user: User, model: Model) {
        val wishlistPresence = WishlistPresence {
            this.user = user
            this.model = model
        }
        database.wishlistPresences.add(wishlistPresence)
    }

    override fun removeFromWishlist(user: User, model: Model) {
        database.wishlistPresences.removeIf {
            (it.userNickname eq user.nickname) and
                    (it.modelName eq model.name) and
                    (it.modelMaker eq model.maker)
        }
    }

    override fun getWishlist(user: User): List<Model> {
        return database.wishlistPresences
            .filter { it.userNickname eq user.nickname }
            .map { getFullModelInfo(it.model) }
    }

    override fun isInWishlist(user: User, model: Model): Boolean {
        return database.wishlistPresences
            .filter { it.userNickname eq user.nickname }
            .any { (it.modelName eq model.name) and (it.modelMaker eq model.maker) }
    }

    override fun getCoupon(code: String): Coupon? {
        return database.coupons
            .find { it.code eq code }
    }

    override fun getFriends(user: User): List<User> {
        return database.friendships
            .filter { it.senderNickname eq user.nickname }
            .map { it.receiver }
    }

    override fun isFriend(sender: User, receiver: User): Boolean {
        return database.friendships
            .filter { it.senderNickname eq sender.nickname }
            .any { it.receiverNickname eq receiver.nickname }
    }

    override fun addFriend(sender: User, receiver: User) {
        val friendship = Friendship {
            this.sender = sender
            this.receiver = receiver
        }
        database.friendships.add(friendship)
    }

    override fun removeFriend(sender: User, receiver: User) {
        database.friendships.removeIf {
            (it.senderNickname eq sender.nickname) and (it.receiverNickname eq receiver.nickname)
        }
    }

    override fun getPersonalSolves(user: User): List<Solve> {
        return database.solves
            .filter { it.userNickname eq user.nickname }
            .sortedBy { it.solveTime.asc() }
            .toList()
    }

    override fun getFriendsSolves(user: User): List<Solve> {
        return database.from(Solves)
            .crossJoin(Friendships, on = ((Friendships.senderNickname eq user.nickname)
                    and (Solves.userNickname eq Friendships.receiverNickname))
                    or (Solves.userNickname eq user.nickname))
            .selectDistinct(Solves.columns)
            .orderBy(Solves.solveTime.asc())
            .map { Solves.createEntity(it) }
    }

    override fun getAllSolves(): List<Solve> {
        return database.solves
            .sortedBy { it.solveTime.asc() }
            .toList()
    }

    override fun getTopSolvesByModel(user: User): List<Solve> {
        return database.from(Solves)
            .select(min(Solves.solveTime).aliased(Solves.solveTime.label), Solves.modelName, Solves.modelMaker)
            .where(Solves.userNickname eq user.nickname)
            .groupBy(Solves.modelName, Solves.modelMaker)
            .map { Solves.createEntity(it) }
    }

    override fun insertSolve(user: User, model: Model?, solveTime: SolveTime) {
        val solve = Solve {
            this.user = user
            this.model = model
            this.registrationDate = LocalDateTime.now()
            this.solveTime = solveTime
        }
        database.solves.add(solve)
    }
}