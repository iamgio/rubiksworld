package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.controller.database.tables.Applications
import rubiksworld.controller.database.tables.Customizations
import rubiksworld.controller.database.tables.Models
import rubiksworld.controller.database.tables.Solves
import rubiksworld.model.*
import java.time.LocalDateTime

/**
 *
 */
open class DatabaseControllerImpl : DatabaseController {

    private lateinit var database: Database

    override fun initDatabase() {
        database = Database.connect("jdbc:mysql://localhost:3306/rubiksworld", user = "root", password = "giodatabase")
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

    override fun insertUser(nickname: String, name: String, surname: String): User {
        val match = getUser(nickname)
        if (match != null) {
            match.name = name
            match.surname = surname
            match.flushChanges()
            return match
        }

        val newUser = User {
            this.nickname = nickname
            this.name = name
            this.surname = surname
        }
        database.users.add(newUser)
        return newUser
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

    override fun getSolves(user: User): List<Solve> {
        return database.solves
            .filter { it.userNickname eq user.nickname }
            .toList()
    }

    override fun getTopSolvesByModel(user: User): Map<Model?, Solve> {
        return database.from(Solves)
            .leftJoin(Models, on = (Solves.userNickname eq user.nickname)
                    and (Solves.modelName eq Models.name)
                    and (Solves.modelMaker eq Models.maker))
            .select()
            .orderBy(Solves.solveTime.asc())
            .associate {
                Models.createEntity(it).let { model ->
                    if (model.name.isEmpty()) null else model
                } to Solves.createEntity(it)
            }
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