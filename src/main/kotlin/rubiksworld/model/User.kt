package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * A person that uses the software.
 */
interface User : Entity<User> {

    companion object : Entity.Factory<User>()

    /**
     * User's unique nickname.
     */
    val nickname: String

    /**
     * User's name.
     */
    val name: String

    /**
     * User's surname.
     */
    val surname: String

    /**
     * Client's city.
     */
    var city: String?

    /**
     * User's zip code.
     */
    var zip: String?

    /**
     * Client's address.
     */
    var address: String?

    /**
     * Client's e-mail address.
     */
    var email: String?

    /**
     * Client's phone number.
     */
    var phoneNumber: String?

    /**
     * Shipping price for this user.
     */
    val shippingPrice: Double

    /**
     * Minimum subtotal to proceed to checkout for this user.
     */
    val minimumSubtotal: Double
}