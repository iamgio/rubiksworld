package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * The application of a [Customization] onto a [ModelVersion].
 */
interface Application : Entity<Application> {

    companion object : Entity.Factory<Application>()

    /**
     * Applied customization.
     */
    var customization: Customization

    /**
     * Target model.
     */
    var modelVersion: ModelVersion
}