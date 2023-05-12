package rubiksworld.view

import javafx.scene.layout.Pane
import rubiksworld.controller.Controller

private const val INITIAL_WIDTH = 900.0
private const val INITIAL_HEIGHT = 600.0

/**
 * The main scene component.
 */
class MainSceneView : View<Pane> {

    override fun create(controller: Controller) = Pane().apply {
        prefWidth = INITIAL_WIDTH
        prefHeight = INITIAL_HEIGHT
        stylesheets += "/style/style.css"
    }
}