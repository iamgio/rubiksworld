package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.double
import org.ktorm.schema.varchar
import rubiksworld.model.Model

/**
 * Models DB table
 */
object Models : Table<Model>("Models") {

    val name = varchar("name").primaryKey().bindTo { it.name }
    val maker = varchar("maker").primaryKey().bindTo { it.maker }
    val category = varchar("category_name").references(Categories) { it.category }
    val price = double("price").bindTo { it.price }
    val discountPercentage = double("discount_percentage").bindTo { it.discountPercentage }
    val imageUrl = varchar("image_url").bindTo { it.imageUrl }
    val isSpeedCube = boolean("is_speed_cube").bindTo { it.isSpeedCube }
    val isStickerless = boolean("is_stickerless").bindTo { it.isStickerless }
    val isMagnetic = boolean("is_magnetic").bindTo { it.isMagnetic }
}