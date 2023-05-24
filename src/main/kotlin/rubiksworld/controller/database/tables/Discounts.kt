package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.Discount

/**
 * Discounts DB table.
 */
object Discounts : Table<Discount>("Discounts") {

    val orderId = int("order_id").primaryKey().bindTo { it.order.id }
    val orderDate = date("order_date").primaryKey().bindTo { it.order.orderDate }
    val couponCode = varchar("coupon_code").primaryKey().references(Coupons) { it.coupon }
}