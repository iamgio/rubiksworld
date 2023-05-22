package rubiksworld.controller.database.tables

import org.ktorm.schema.*
import rubiksworld.model.Order

/**
 * Orders DB table.
 */
object Orders : Table<Order>("Orders") {

    val id = int("id").primaryKey().bindTo { it.id }
    val orderDate = date("order_date").primaryKey().bindTo { it.orderDate }
    val orderTime = time("order_time").bindTo { it.orderTime }
    val shippingDate = date("shipping_date").bindTo { it.shippingDate }
    val total = double("total").bindTo { it.total }
    val userNickname = varchar("user_nickname").references(Users) { it.user }
}