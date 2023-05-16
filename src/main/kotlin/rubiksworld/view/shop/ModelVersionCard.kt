package rubiksworld.view.shop

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.ModelVersion
import rubiksworld.view.ModelCard
import rubiksworld.view.View

/**
 * The card for a customized [ModelVersion].
 *
 * @param modelVersion customized model version
 * @param onRemove action to run when this is model version is removed from a list,
 *                 with this card's layout as an argument
 */
class ModelVersionCard(private val modelVersion: ModelVersion, private val onRemove: (Node) -> Unit) : View<Pane> {

    override fun create(controller: Controller): Pane {
        val model = controller.getFullModelInfo(modelVersion.model).copy()
        model.price = controller.getModelVersionPrice(modelVersion)
        model.discountPercentage = null

        return ModelCard(model).create(controller).apply {
            val customizationsBox = VBox().apply { styleClass += "customization-box" }

            customizationsBox.children.addAll(
                controller.getAppliedCustomizations(modelVersion).map {
                    Label(it.customization.part + ": " + it.customization.change)
                }
            )

            val spacer = Pane().apply { VBox.setVgrow(this, Priority.ALWAYS) }

            val remove = Button("Remove").also {
                it.setOnAction { onRemove(this) }
            }

            children.addAll(customizationsBox, spacer, remove)
        }
    }
}