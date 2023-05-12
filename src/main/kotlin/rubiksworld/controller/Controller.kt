package rubiksworld.controller

import rubiksworld.controller.database.DatabaseController

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
}