package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.ModelVersion

/**
 * Model versions DB table.
 */
object ModelVersions : Table<ModelVersion>("ModelVersions") {

    val id = int("id").primaryKey().bindTo { it.id }
    val modelName = varchar("model_name").bindTo { it.model.name }
    val modelMaker = varchar("model_maker").bindTo { it.model.maker }
}