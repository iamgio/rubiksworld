package rubiksworld.controller.database

import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.model.*

/**
 * The controller responsible for communicating with the database.
 */
interface DatabaseController {

    /**
     * Initializes the database.
     */
    fun initDatabase()

    /**
     * Searches models by some given filters.
     * @param filters search filters
     * @return models matching the given filters
     */
    fun searchModels(filters: ModelsSearchFilters): List<Model>

    /**
     * @return available customizable parts for the given [model]
     */
    fun getCustomizableParts(model: Model): List<CustomizablePart>

    /**
     * @return the available customizations for the given [part]
     */
    fun getAvailableCustomizations(part: CustomizablePart): List<Customization>

    /**
     * @return all the registered users
     */
    fun getAllUsers(): List<User>

    /**
     * @param nickname exact user nickname
     * @return user from [nickname] if it exists
     */
    fun getUser(nickname: String): User?

    /**
     * @param query part of nickname, name or surname
     * @param except user to exclude from the results
     * @return users that match the query
     */
    fun findUser(query: String, except: User?): List<User>

    /**
     * Inserts a new user if it does not exist, or updates it if it exists.
     * @param nickname user's nickname
     * @param name user's name
     * @param surname user's surname
     */
    fun insertUser(nickname: String, name: String, surname: String): User

    /**
     * Updates a user's information.
     * @param user user to update
     */
    fun updateUserInfo(user: User, name: String, surname: String, city: String, zip: String,
                       email: String, address: String, phoneNumber: String?)

    /**
     * Creates a row for a new customized version of a [Model].
     * @return a new model version
     */
    fun insertModelVersion(model: Model, customizations: List<Customization>): ModelVersion

    /**
     * @return the price of a customized [modelVersion], discounts included
     */
    fun getModelVersionPrice(modelVersion: ModelVersion): Double

    /**
     * @return the customizations applied to a [modelVersion].
     */
    fun getAppliedCustomizations(modelVersion: ModelVersion): List<Application>

    /**
     * Joins the complete information about a model from a partial structure
     * composed by its name and maker.
     * @param partialModel partial model information
     * @return model filled with information
     */
    fun getFullModelInfo(partialModel: Model): Model

    /**
     * Adds a model version to the [user]'s cart.
     * @param user cart owner
     * @param modelVersion customized model version
     */
    fun addToCart(user: User, modelVersion: ModelVersion)

    /**
     * Adds a model version to the [user]'s cart.
     * @param user cart owner
     * @param model model
     * @param customizations customizations to be applied
     */
    fun addToCart(user: User, model: Model, customizations: List<Customization>) {
        addToCart(user, insertModelVersion(model, customizations))
    }

    /**
     * Removes a model version from the [user]'s cart.
     * @param user cart owner
     * @param modelVersion customized model version
     */
    fun removeFromCart(user: User, modelVersion: ModelVersion)

    /**
     * @param user cart owner
     * @return the model versions from the [user]'s cart
     */
    fun getCart(user: User): List<ModelVersion>

    /**
     * @param user cart owner
     * @return [user]'s cart subtotal
     */
    fun getCartSubtotal(user: User): Double

    /**
     * @param user cart owner
     * @param coupons used coupons
     * @return [user]'s cart total, including shipping price and coupon discounts
     */
    fun getCartTotal(user: User, coupons: List<Coupon>): Double

    /**
     * @param user cart owner
     * @return whether [user] can proceed to checkout
     */
    fun canCheckout(user: User): Boolean

    /**
     * Places an order.
     * @param user cart owner
     * @param coupons applied coupons
     */
    fun insertOrderFromCart(user: User, coupons: List<Coupon>)

    /**
     * @return orders placed by [user]
     */
    fun getOrders(user: User): List<Order>

    /**
     * @return model versions present in an [order]
     */
    fun getModelVersionsFromOrder(order: Order): List<ModelVersion>

    /**
     * Adds a model to the [user]'s wishlist.
     * @param user wishlist owner
     * @param model model to add
     */
    fun addToWishlist(user: User, model: Model)

    /**
     * Removes a model from the [user]'s wishlist.
     * @param user wishlist owner
     * @param model model to remove
     */
    fun removeFromWishlist(user: User, model: Model)

    /**
     * @param user wishlist owner
     * @return the models from the [user]'s wishlist
     */
    fun getWishlist(user: User): List<Model>

    /**
     * @param user wishlist owner
     * @param model model to check existance for
     * @return whether [model] is in [user]'s wishlist
     */
    fun isInWishlist(user: User, model: Model): Boolean

    /**
     * @param code coupon code
     * @return corresponding coupon, if it exists
     */
    fun getCoupon(code: String): Coupon?

    /**
     * @param user friendship sender
     * @return users that have received friendship from [user]
     */
    fun getFriends(user: User): List<User>

    /**
     * @return whether [sender] has sent friendship to [receiver]
     */
    fun isFriend(sender: User, receiver: User): Boolean

    /**
     * Adds a friendship from [sender] to [receiver].
     */
    fun addFriend(sender: User, receiver: User)

    /**
     * Removes a friendship from [sender] to [receiver].
     */
    fun removeFriend(sender: User, receiver: User)

    /**
     * @return all the solves registered by [user], in ascending order.
     */
    fun getPersonalSolves(user: User): List<Solve>

    /**
     * @return all the solves registered by [user]'s friends,
     *         plus their personal solves, in ascending order
     */
    fun getFriendsSolves(user: User): List<Solve>

    /**
     * @return all the solves registered by any user, in ascending order.
     */
    fun getAllSolves(): List<Solve>

    /**
     * @return the top solve registered by [user] for each model (`null` (unregistered) model is included)
     */
    fun getTopSolvesByModel(user: User): List<Solve>

    /**
     * Registers a new solve.
     * @param user solve owner
     * @param model optional model used to make the solve
     * @param solveTime time spent to make the solve
     */
    fun insertSolve(user: User, model: Model?, solveTime: SolveTime)
}