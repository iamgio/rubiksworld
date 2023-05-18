package rubiksworld.view.shop

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.view.View
import rubiksworld.view.common.LiteralTextField
import rubiksworld.view.common.NumericTextField

/**
 * Cart checkout view.
 */
class CheckoutView : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "checkout-view"

        val name = LiteralTextField(controller.user.name)
        val surname = LiteralTextField(controller.user.surname)
        val city = LiteralTextField(controller.user.city ?: "")
        val zip = NumericTextField(controller.user.zip ?: "")
        val email = TextField(controller.user.email ?: "")
        val address = TextField(controller.user.address ?: "")
        val phone = NumericTextField(controller.user.phoneNumber ?: "")

        val coupon = TextField()
        val couponButton = Button("Apply")
        val purchaseButton = Button("Purchase")

        children += VBox(
            Label("Name"), name,
            Label("Surname"), surname,
            Label("City"), city,
            Label("Zip code"), zip,
            Label("E-mail"), email,
            Label("Address"), address,
            Label("Phone number (optional)"), phone,
        ).apply {
            styleClass += "fields-box"
        }

        val couponBox = VBox(
            Label("Coupon"), HBox(coupon, couponButton),
        ).apply {
            styleClass.addAll("fields-box", "coupon-box")
        }

        val totalBox = VBox(
            couponBox,
            // TODO coupons + shipping price in total
            Label(formatPrice(controller.getCartSubtotal(controller.user))),
            purchaseButton
        ).apply {
            styleClass += "fields-box"
        }
        children += totalBox
    }
}