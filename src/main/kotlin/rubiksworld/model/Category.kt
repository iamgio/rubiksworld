package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * Shop categories.
 */
interface Category : Entity<Category> {

    companion object : Entity.Factory<Category>()

    /**
     * Name of the categories.
     */
    val name: String
}