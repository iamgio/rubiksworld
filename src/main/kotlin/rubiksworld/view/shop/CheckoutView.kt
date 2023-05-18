package rubiksworld.view.shop

import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.model.Coupon
import rubiksworld.view.View
import rubiksworld.view.common.LiteralTextField
import rubiksworld.view.common.NumericTextField

/**
 * Cart checkout view.
 */
class CheckoutView : View<Pane> {

    private val total: DoubleProperty = SimpleDoubleProperty()
    private val appliedCoupons = mutableListOf<Coupon>()

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

        val appliedCouponsBox = VBox()
        val couponBox = VBox(
            Label("Coupon"), HBox(coupon, couponButton),
            appliedCouponsBox
        ).apply {
            styleClass.addAll("fields-box", "coupon-box")
        }

        updateTotal(controller)

        val totalLabel = Label().apply {
            textProperty().bind(total.map { formatPrice(it.toDouble()) })
        }

        val totalBox = VBox(
            totalLabel,
            purchaseButton,
            couponBox
        ).apply {
            styleClass += "fields-box"
        }
        children += totalBox

        // Handlers

        couponButton.setOnAction {
            val code = coupon.text
            val couponMatch = controller.getCoupon(code)
            coupon.clear()
            if (code in appliedCoupons.map { it.code }) {
                return@setOnAction
            }

            appliedCouponsBox.children += Label(
                if (couponMatch == null) {
                    "$code: invalid coupon"
                } else {
                    appliedCoupons += couponMatch
                    updateTotal(controller)
                    "${couponMatch.code}: -${couponMatch.formatted()}"
                }
            ).apply {
                styleClass += "applied-coupon-label"
            }
        }

        couponButton.disableProperty().bind(coupon.textProperty().isEmpty)

        coupon.setOnAction { couponButton.fire() }
    }

    private fun updateTotal(controller: Controller) {
        this.total.set(controller.getCartTotal(controller.user, appliedCoupons))
    }
}