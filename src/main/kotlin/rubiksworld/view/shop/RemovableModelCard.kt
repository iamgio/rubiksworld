package rubiksworld.view.shop

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.ModelCard

/**
 * A [ModelCard] with a 'Remove' button.
 *
 * @param model model to represent
 * @param onRemove action to run when this is model version is removed from a list,
 *                 with this card's layout as an argument
 */
class RemovableModelCard(model: Model, private val onRemove: (Node) -> Unit) : ModelCard(model) {

    override fun create(controller: Controller) = super.create(controller).apply {
        val spacer = Pane().apply { VBox.setVgrow(this, Priority.ALWAYS) }

        val remove = Button("Remove").also {
            it.setOnAction { onRemove(this) }
        }

        children.addAll(spacer, remove)
    }
}