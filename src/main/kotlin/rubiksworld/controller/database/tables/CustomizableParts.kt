package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import rubiksworld.model.CustomizablePart

/**
 * Customizable parts DB table.
 */
object CustomizableParts : Table<CustomizablePart>("CustomizableParts") {

    val modelName = varchar("model_name").primaryKey().bindTo { it.model.name }
    val modelMaker = varchar("model_maker").primaryKey().bindTo { it.model.maker }
    val part = varchar("part").primaryKey().bindTo { it.part }
}