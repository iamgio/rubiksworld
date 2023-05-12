package rubiksworld.view

import rubiksworld.controller.Controller

/**
 * A graphic component.
 *
 * @param T type of the component
 */
interface View<T> {

    /**
     * @param controller controller to interface to
     * @return an instance of the graphic component
     */
    fun create(controller: Controller): T
}