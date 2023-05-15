package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.Application

/**
 * Applications DB table.
 */
object Applications : Table<Application>("Applications") {

    val modelName = varchar("model_name").primaryKey().bindTo { it.customization.model.name }
    val modelMaker = varchar("model_maker").primaryKey().bindTo { it.customization.model.maker }
    val part = varchar("customization_part").primaryKey().bindTo { it.customization.part }
    val change = varchar("customization_change").primaryKey().bindTo { it.customization.change }
    val modelVersionId = int("model_version_id").primaryKey().references(ModelVersions) { it.modelVersion }
}