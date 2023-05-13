package rubiksworld.controller

import javafx.application.Platform
import rubiksworld.controller.database.DatabaseControllerImpl
import kotlin.concurrent.thread

/**
 * [Controller] implementation.
 */
class ControllerImpl : DatabaseControllerImpl(), Controller {

    override fun async(action: Controller.() -> Unit) {
        thread { action(this) }
    }

    override fun sync(action: Controller.() -> Unit) {
        Platform.runLater { action(this) }
    }
}