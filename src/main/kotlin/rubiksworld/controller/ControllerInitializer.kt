package rubiksworld.controller

import rubiksworld.controller.database.DatabaseControllerImpl

/**
 * Initializer for the global controller.
 */
class ControllerInitializer {

    /**
     * @return a new controller instance
     */
    fun initialize(): Controller = ControllerImpl(DatabaseControllerImpl())
}