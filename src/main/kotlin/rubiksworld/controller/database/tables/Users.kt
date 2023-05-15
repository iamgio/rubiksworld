package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.varchar
import rubiksworld.model.User

/**
 * Users DB table
 */
object Users : Table<User>("Users") {

    val nickname = varchar("nickname").primaryKey().bindTo { it.nickname }
    val name = varchar("name").bindTo { it.name }
    val surname = varchar("surname").bindTo { it.surname }
    val city = varchar("city").bindTo { it.city }
    val zip = varchar("zip").bindTo { it.zip }
    val email = varchar("email").bindTo { it.email }
    val address = varchar("address").bindTo { it.address }
    val phoneNumber = varchar("phone_number").bindTo { it.phoneNumber }
    val shippingPrice = double("shipping_price").bindTo { it.shippingPrice }
    val minimumSubtotal = double("minimum_subtotal").bindTo { it.minimumSubtotal }
}