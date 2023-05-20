package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import rubiksworld.model.Friendship

/**
 * Friendships DB table.
 */
object Friendships : Table<Friendship>("Friendships") {

    val senderNickname = varchar("sender_nickname").primaryKey().references(Users) { it.sender }
    val receiverNickname = varchar("receiver_nickname").primaryKey().references(Users) { it.receiver }
}