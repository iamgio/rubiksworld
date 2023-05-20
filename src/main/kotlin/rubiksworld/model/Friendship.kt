package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * Unilateral friendship from a [User] to another.
 */
interface Friendship : Entity<Friendship> {

    companion object : Entity.Factory<Friendship>()

    /**
     * User that requested the friendship.
     */
    var sender: User

    /**
     * User that received the friendship.
     */
    var receiver: User
}