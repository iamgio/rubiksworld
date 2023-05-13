package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * A part that can be applied to a [Model].
 */
interface CustomizablePart : Entity<CustomizablePart> {

    companion object : Entity.Factory<CustomizablePart>()

    /**
     * Target model.
     */
    val model: Model

    /**
     * Part name.
     */
    val part: String
}