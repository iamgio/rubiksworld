package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * A customization that can be applied to a [Model].
 */
interface Customization : Entity<Customization> {

    companion object : Entity.Factory<Customization>()

    /**
     * Target model.
     */
    val model: Model

    /**
     * Part name.
     */
    val part: String

    /**
     * Change name.
     */
    val change: String

    /**
     * Price of the customization.
     */
    val price: Double

    /**
     * Whether this is the default customization for the given [part].
     */
    val isDefault: Boolean
}