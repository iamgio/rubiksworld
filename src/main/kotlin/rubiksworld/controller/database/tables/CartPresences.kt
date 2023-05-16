package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.CartPresence

/**
 * Cart presences DB table.
 */
object CartPresences : Table<CartPresence>("CartPresences") {

    val userNickname = varchar("user_nickname").primaryKey().bindTo { it.user.nickname }
    val modelVersionId = int("model_version_id").primaryKey().references(ModelVersions) { it.modelVersion }
}