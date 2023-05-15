package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * Presence of a model version in a user's cart.
 */
interface CartPresence : Entity<CartPresence> {

    companion object : Entity.Factory<CartPresence>()

    /**
     * Cart's owner.
     */
    var user: User

    /**
     * Target model.
     */
    var modelVersion: ModelVersion
}