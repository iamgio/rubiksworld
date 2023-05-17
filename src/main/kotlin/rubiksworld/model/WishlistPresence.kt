package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * Presence of a model in a user's wishlist.
 */
interface WishlistPresence : Entity<WishlistPresence> {

    companion object : Entity.Factory<WishlistPresence>()

    /**
     * Wishlist owner.
     */
    var user: User

    /**
     * Target model.
     */
    var model: Model
}