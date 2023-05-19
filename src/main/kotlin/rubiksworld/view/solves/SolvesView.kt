package rubiksworld.view.solves

import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View

/**
 * The Solves section content.
 */
class SolvesView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "solves-view"

        children += SolvesBar().create(controller)
    }
}