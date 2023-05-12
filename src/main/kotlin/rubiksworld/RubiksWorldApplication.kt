package rubiksworld

import javafx.application.Application
import javafx.stage.Stage
import rubiksworld.controller.ControllerInitializer
import rubiksworld.view.MainScene

/**
 * The JavaFX application.
 */
class RubiksWorldApplication : Application() {

    override fun start(primaryStage: Stage?) {
        val controller = ControllerInitializer().initialize()
        controller.initDatabase()

        primaryStage?.run {
            title = "Rubik's World"
            scene = MainScene().create(controller)
            show()
        }
    }
}