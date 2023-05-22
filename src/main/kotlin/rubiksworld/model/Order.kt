package rubiksworld.model

import org.ktorm.entity.Entity
import java.time.LocalDate
import java.time.LocalTime

/**
 * An order that can be placed.
 */
interface Order : Entity<Order> {

    companion object : Entity.Factory<Order>()

    /**
     * Order ID.
     */
    var id: Int

    /**
     * The date the order was placed.
     */
    var orderDate: LocalDate

    /**
     * The time the order was placed.
     */
    var orderTime: LocalTime

    /**
     * The expected shipping date.
     */
    var shippingDate: LocalDate

    /**
     * Total of the order.
     */
    var total: Double

    /**
     * User that placed the order.
     */
    var user: User
}