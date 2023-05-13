package rubiksworld.view.shop

import javafx.geometry.Orientation
import javafx.scene.control.ScrollPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.ModelCard
import rubiksworld.view.View

const val CURRENCY = '€'

private const val BARS_HEIGHT = 57.0

/**
 * The shop section content.
 *
 * @param onUpdate action to run when the UI should be updated
 */
class ShopView(val onUpdate: () -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        val modelsPane = FlowPane(Orientation.HORIZONTAL).apply { styleClass += "models-pane" }

        val bar = ShopBar { filters ->
            controller.async {
                val results = searchModels(filters)
                controller.sync {
                    modelsPane.children.setAll(results.map { ModelCard(it).create(controller) })
                    onUpdate()
                }
            }
        }.create(controller)

        val scrollPane = ScrollPane(modelsPane).apply {
            isFitToWidth = true
            prefHeightProperty().bind(sceneProperty().map { it?.height?.minus(BARS_HEIGHT) ?: 0 })
        }

        children.addAll(bar, scrollPane)
    }
}