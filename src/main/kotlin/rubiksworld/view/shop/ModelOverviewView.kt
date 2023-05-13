package rubiksworld.view.shop

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.View

/**
 *
 */
class ModelOverviewView(private val model: Model) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        children += createRightColumn(controller)
    }

    private fun createRightColumn(controller: Controller) = VBox().apply {
        children.addAll(controller.getCustomizableParts(model).map { Label(it.part) })
    }
}