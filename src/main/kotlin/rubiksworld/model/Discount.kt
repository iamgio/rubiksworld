package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * The application of a coupon on an order.
 */
interface Discount : Entity<Discount> {

    companion object : Entity.Factory<Discount>()

    /**
     * Target order.
     */
    var order: Order

    /**
     * Applied coupon.
     */
    var coupon: Coupon
}