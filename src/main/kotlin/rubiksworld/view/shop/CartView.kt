package rubiksworld.view.shop

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.ModelsPane
import rubiksworld.view.View

/**
 * User's cart view.
 */
class CartView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        val modelsPane = ModelsPane()

        controller.getCart(controller.user).forEach {
            modelsPane.children += ModelVersionCard(it).create(controller)
        }

        children += ScrollPane(modelsPane).apply {
            isFitToWidth = true
        }
    }
}