package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import rubiksworld.model.Category

/**
 * Categories DB table
 */
object Categories : Table<Category>("Categories") {

    val name = varchar("name").primaryKey().bindTo { it.name }
}