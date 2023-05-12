package rubiksworld.controller

import rubiksworld.controller.database.DatabaseController

/**
 * [Controller] implementation.
 */
class ControllerImpl(
    private val databaseController: DatabaseController
) : Controller {

    override fun initDatabase() {
        databaseController.initDatabase()
    }

    override fun searchModels(filters: ModelsSearchFilters) = databaseController.searchModels(filters)
}