package rubiksworld.controller.database

import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.model.CustomizablePart
import rubiksworld.model.Customization
import rubiksworld.model.Model

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
}