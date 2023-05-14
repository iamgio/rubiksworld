package rubiksworld.common

import rubiksworld.view.shop.CURRENCY

/**
 * @return [price] with 2 decimal places + currency symbol
 */
fun formatPrice(price: Double) = String.format("%.02f", price) + CURRENCY

/**
 * @return [percentage] from a `[0, 1]` double to a `[0, 100]` integer
 */
fun doublePercentageToInt(percentage: Double) = (percentage * 100).toInt()

/**
 * @return [price] reduced by a [discountPercentage]
 */
fun calcDiscountedPrice(price: Double, discountPercentage: Double) = price - price * discountPercentage