package rubiksworld.controller

import javafx.application.Platform
import rubiksworld.controller.database.DatabaseController
import kotlin.concurrent.thread

/**
 * [Controller] implementation.
 */
class ControllerImpl(
    private val databaseController: DatabaseController
) : Controller {

    override fun async(action: Controller.() -> Unit) {
        thread { action(this) }
    }

    override fun sync(action: Controller.() -> Unit) {
        Platform.runLater { action(this) }
    }

    override fun initDatabase() {
        databaseController.initDatabase()
    }

    override fun searchModels(filters: ModelsSearchFilters) = databaseController.searchModels(filters)
}