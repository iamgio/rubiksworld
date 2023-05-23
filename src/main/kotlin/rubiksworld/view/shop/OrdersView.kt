package rubiksworld.view.shop

import javafx.scene.control.Control
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Order
import rubiksworld.view.View

/**
 *
 */
class OrdersView : View<Control> {

    override fun create(controller: Controller) = ScrollPane(createContent(controller))

    private fun createContent(controller: Controller) = VBox().apply {
        children.addAll(controller.getOrders(controller.user).map(::createItem))
    }

    private fun createItem(order: Order) = VBox().apply {
        children += Label(order.id.toString())
    }
}