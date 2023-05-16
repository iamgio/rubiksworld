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
     * Inserts a new user if it does not exist, or updates it if it exists.
     * @param nickname user's nickname
     * @param name user's name
     * @param surname user's surname
     */
    fun insertUser(nickname: String, name: String, surname: String): User

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
     * Adds a model version to the [user]'s cart
     * @param user cart owner
     * @param modelVersion customized model version
     */
    fun addToCart(user: User, modelVersion: ModelVersion)

    /**
     * Adds a model version to the [user]'s cart
     * @param user cart owner
     * @param model model
     * @param customizations customizations to be applied
     */
    fun addToCart(user: User, model: Model, customizations: List<Customization>) {
        addToCart(user, insertModelVersion(model, customizations))
    }

    fun removeFromCart(user: User, modelVersion: ModelVersion)

    /**
     * @param user cart owner
     * @return the model versions from the [user]'s cart
     */
    fun getCart(user: User): List<ModelVersion>
}