package rubiksworld.controller

import rubiksworld.controller.database.DatabaseController
import rubiksworld.model.Customization
import rubiksworld.model.Model
import rubiksworld.model.User

/**
 * The model-view bridge.
 */
interface Controller : DatabaseController {

    /**
     * Current active user.
     */
    var user: User

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
    fun calcPrice(model: Model, customizations: List<Customization>, applyDiscount: Boolean = true): Double

    /**
     * Adds or removes friendship between [user] and [receiver].
     * @param receiver target user
     * @return whether the friendship was added or not
     */
    fun toggleFriendship(receiver: User): Boolean
}