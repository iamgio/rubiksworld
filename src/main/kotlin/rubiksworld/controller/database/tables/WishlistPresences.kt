package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import rubiksworld.model.WishlistPresence

/**
 * Wishlist presences DB table.
 */
object WishlistPresences : Table<WishlistPresence>("WishlistPresences") {

    val userNickname = varchar("user_nickname").primaryKey().bindTo { it.user.nickname }
    val modelName = varchar("model_name").primaryKey().bindTo { it.model.name }
    val modelMaker = varchar("model_maker").primaryKey().bindTo { it.model.maker }
}