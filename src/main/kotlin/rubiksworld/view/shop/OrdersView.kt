package rubiksworld.view.shop

import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.defaultFormat
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.model.Order
import rubiksworld.view.ModelsPane
import rubiksworld.view.View
import rubiksworld.view.common.TitleLabel
import rubiksworld.view.common.fitToViewport

/**
 * A page containing a user's orders.
 */
class OrdersView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "orders-view"

        val content = VBox().apply {
            children.addAll(controller.getOrders(controller.user).map { createItem(it, controller) })
        }

        children += ScrollPane(content).apply { fitToViewport(includeBar = false) }
    }

    private fun createItem(order: Order, controller: Controller) = VBox(
        TitleLabel("Order #${order.id}\t" + formatPrice(order.total)),
        Label("Ordered " + order.orderDate.defaultFormat()),
        Label("Shipping " + order.orderDate.defaultFormat()),
        ModelsPane().apply {
            controller.getModelVersionsFromOrder(order).forEach {
                children += ModelVersionCard(it, null).create(controller)
            }
        },
        Separator(Orientation.HORIZONTAL)
    ).apply { styleClass += "order-item" }
}