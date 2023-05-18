package rubiksworld.model

import org.ktorm.entity.Entity
import rubiksworld.view.shop.CURRENCY

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
    val value: Int

    /**
     * Coupon type.
     */
    val type: Type

    /**
     * @return the formatted content of this coupon as a string
     */
    fun formatted() = when(type) {
        Type.ABSOLUTE -> "$value$CURRENCY"
        Type.PERCENTAGE -> "$value%"
    }

    enum class Type {
        ABSOLUTE, PERCENTAGE
    }
}