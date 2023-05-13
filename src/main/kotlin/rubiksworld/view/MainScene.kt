package rubiksworld.view

import javafx.scene.Scene
import rubiksworld.controller.Controller

private const val INITIAL_WIDTH = 925.0
private const val INITIAL_HEIGHT = 600.0

/**
 * The main scene.
 */
class MainScene : View<Scene> {

    override fun create(controller: Controller) = Scene(
        MainSceneView().create(controller),
        INITIAL_WIDTH, INITIAL_HEIGHT
    ).apply {
        stylesheets += "/style/style.css"
    }
}