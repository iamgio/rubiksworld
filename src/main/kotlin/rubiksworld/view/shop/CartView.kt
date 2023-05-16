package rubiksworld.view.shop

import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View

/**
 * User's cart view.
 */
class CartView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        children += Label("${controller.user.name}'s cart")
        controller.getCart(controller.user).forEach {
            children += ModelVersionCard(it).create(controller)
        }
    }
}