package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import rubiksworld.model.OrderPresence

/**
 * OrderPresences DB table.
 */
object OrderPresences : Table<OrderPresence>("OrderPresences") {

    val orderId = int("order_id").primaryKey().bindTo { it.order.id }
    val orderDate = date("order_date").primaryKey().bindTo { it.order.orderDate }
    val modelVersionId = int("model_version_id").primaryKey().references(ModelVersions) { it.modelVersion }
}