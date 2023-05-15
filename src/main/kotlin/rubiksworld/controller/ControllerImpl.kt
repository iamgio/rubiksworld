package rubiksworld.controller

import javafx.application.Platform
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.controller.database.DatabaseControllerImpl
import rubiksworld.model.Customization
import rubiksworld.model.Model
import kotlin.concurrent.thread

/**
 * [Controller] implementation.
 */
class ControllerImpl : DatabaseControllerImpl(), Controller {

    override fun async(action: Controller.() -> Unit) {
        thread { action(this) }
    }

    override fun sync(action: Controller.() -> Unit) {
        Platform.runLater { action(this) }
    }

    override fun calcPrice(model: Model, customizations: List<Customization>, applyDiscount: Boolean): Double {
        val price = model.price + customizations.sumOf { it.price }
        return model.discountPercentage?.let { calcDiscountedPrice(price, it) }?.takeIf { applyDiscount } ?: price
    }
}