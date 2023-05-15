package rubiksworld.controller

import rubiksworld.common.calcDiscountedPrice
import rubiksworld.controller.database.DatabaseController
import rubiksworld.model.Customization
import rubiksworld.model.Model

/**
 * The model-view bridge.
 */
interface Controller : DatabaseController {

    /**
     * Runs an action asynchronously.
     * @param action controller-relative action to run
     */
    fun async(action: Controller.() -> Unit)

    /**
     * Runs an action synchronously.
     * @param action controller-relative action to run
     */
    fun sync(action: Controller.() -> Unit)

    /**
     * @return the final price of a [model] with applied [customizations]
     */
    fun calcPrice(model: Model, customizations: List<Customization>): Double {
        val price = model.price + customizations.sumOf { it.price }
        return model.discountPercentage?.let { calcDiscountedPrice(price, it) } ?: price
    }
}