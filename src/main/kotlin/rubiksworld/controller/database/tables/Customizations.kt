package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.double
import org.ktorm.schema.varchar
import rubiksworld.model.Customization

/**
 * Customizations DB table.
 */
object Customizations : Table<Customization>("Customizations") {

    val modelName = varchar("model_name").primaryKey().bindTo { it.model.name }
    val modelMaker = varchar("model_maker").primaryKey().bindTo { it.model.maker }
    val part = varchar("part").primaryKey().bindTo { it.part }
    val change = varchar("change").primaryKey().bindTo { it.change }
    val price = double("price").bindTo { it.price }
    val isDefault = boolean("is_default").bindTo { it.isDefault }
}