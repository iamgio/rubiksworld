package rubiksworld.model

import org.ktorm.entity.Entity
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.common.doublePercentageToInt
import rubiksworld.common.formatPrice

/**
 * A coupon that provides a discount.
 */
interface Coupon : Entity<Coupon> {

    companion object : Entity.Factory<Coupon>()

    /**
     * Coupon code.
     */
    val code: String

    /**
     * Coupon value.
     */
    val value: Double

    /**
     * Coupon type.
     */
    val type: Type

    /**
     * @return the formatted content of this coupon as a string
     */
    fun formatted() = when(type) {
        Type.ABSOLUTE -> formatPrice(value)
        Type.PERCENTAGE -> "${doublePercentageToInt(value)}%"
    }

    fun applied(price: Double) = when(type) {
        Type.ABSOLUTE -> kotlin.math.max(.0, price - value)
        Type.PERCENTAGE -> calcDiscountedPrice(price, value)
    }

    enum class Type {
        ABSOLUTE, PERCENTAGE
    }
}