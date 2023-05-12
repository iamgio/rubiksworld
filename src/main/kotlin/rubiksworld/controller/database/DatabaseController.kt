package rubiksworld.controller.database

import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.model.Model

/**
 * The controller responsible for communicating with the database.
 */
interface DatabaseController {

    /**
     * Searches models by some given filters.
     * @param filters search filters
     * @return models matching the given filters
     */
    fun searchModels(filters: ModelsSearchFilters): List<Model>
}