package rubiksworld.view.shop

import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.view.ModelsPane
import rubiksworld.view.View

/**
 * User's cart view.
 *
 * @param onCheckout action to run when the user should checkout
 */
class CartView(private val onCheckout: () -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        children += createBar(controller)

        val modelsPane = ModelsPane()

        controller.getCart(controller.user).forEach {
            modelsPane.children += ModelVersionCard(it) { card ->
                controller.removeFromCart(controller.user, it)
                modelsPane.children -= card
            }.create(controller)
        }

        children += ScrollPane(modelsPane).apply {
            isFitToWidth = true
        }
    }

    private fun createBar(controller: Controller) = HBox().apply {
        styleClass += "shop-bar"
        children += Pane().apply { HBox.setHgrow(this, Priority.ALWAYS) }
        children += Button("Checkout").apply {
            val isLocked = !controller.canCheckout(controller.user)
            isDisable = isLocked
            if (isLocked) {
                text = "Minimum: " + formatPrice(controller.user.minimumSubtotal)
            }
            setOnAction { onCheckout() }
        }
    }
}