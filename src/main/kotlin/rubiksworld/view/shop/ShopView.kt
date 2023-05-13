package rubiksworld.view.shop

import javafx.geometry.Orientation
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.ModelCard
import rubiksworld.view.View

const val CURRENCY = '€'

/**
 * The shop section content.
 *
 * @param onUpdate action to run when the UI should be updated
 */
class ShopView(val onUpdate: () -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        val modelsPane = FlowPane(Orientation.HORIZONTAL).apply { styleClass += "models-pane" }

        children += ShopBar { filters ->
            controller.async {
                val results = searchModels(filters)
                controller.sync {
                    modelsPane.children.setAll(results.map { ModelCard(it).create(controller) })
                    onUpdate()
                }
            }
        }.create(controller)

        children += modelsPane
    }
}