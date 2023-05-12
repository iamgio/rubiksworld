package rubiksworld.view.shop

import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View
import kotlin.concurrent.thread

/**
 * The shop section content.
 *
 * @param onUpdate action to run when the UI should be updated
 */
class ShopView(val onUpdate: () -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        children += ShopBar { filters ->
            thread {
                val results = controller.searchModels(filters)
                Platform.runLater {
                    children.addAll(results.map { Label(it.toString()) })
                    onUpdate()
                }
            }
        }.create(controller)
    }
}