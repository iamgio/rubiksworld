package rubiksworld.controller

/**
 * Initializer for the global controller.
 */
class ControllerInitializer {

    /**
     * @return a new controller instance
     */
    fun initialize(): Controller = ControllerImpl()
}