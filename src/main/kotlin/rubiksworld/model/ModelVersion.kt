package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * A physical customizable version of a [Model].
 */
interface ModelVersion : Entity<ModelVersion> {

    companion object : Entity.Factory<ModelVersion>()

    /**
     * Unique ID of this model version.
     */
    val id: Int

    /**
     * Target model.
     */
    val model: Model
}