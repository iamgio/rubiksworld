package rubiksworld

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import rubiksworld.controller.ControllerInitializer
import rubiksworld.view.MainSceneView

/**
 * The JavaFX application.
 */
class RubiksWorldApplication : Application() {

    override fun start(primaryStage: Stage?) {
        val controller = ControllerInitializer().initialize()
        primaryStage?.run {
            title = "Rubik's World"
            scene = Scene(MainSceneView().create(controller))
            show()
        }
    }
}