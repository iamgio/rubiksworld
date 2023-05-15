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
     * Creates a row for a new customized version of a [Model].
     * @return a new model version
     */
    fun insertModelVersion(model: Model, customizations: List<Customization>): ModelVersion
}