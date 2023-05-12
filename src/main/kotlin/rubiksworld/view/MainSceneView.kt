package rubiksworld.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller

/**
 * The main scene component.
 */
class MainSceneView : View<Pane> {

    override fun create(controller: Controller) = Pane().apply {
        sceneProperty().addListener { _, _, new ->
            prefWidthProperty().bind(new.widthProperty())
            prefHeightProperty().bind(new.heightProperty())
        }

        children += TabPane(Tab("Shop"), Tab("Solves")).also {
            it.prefWidthProperty().bind(prefWidthProperty())
        }
    }
}