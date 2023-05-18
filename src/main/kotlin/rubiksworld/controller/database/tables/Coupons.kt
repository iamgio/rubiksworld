package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.Coupon

/**
 * Coupons DB table.
 */
object Coupons : Table<Coupon>("Coupons") {

    val code = varchar("code").primaryKey().bindTo { it.code }
    val value = int("value").bindTo { it.value }
    val type = int("type")
        .transform({ Coupon.Type.values()[it] }, { it.ordinal })
        .bindTo { it.type }
}