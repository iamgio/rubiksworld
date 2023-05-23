package rubiksworld.view.shop

import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import rubiksworld.controller.Controller
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.view.ModelsSearchBar

/**
 * A bar for the shop that lets the user search models and access his cart and wishlist.
 *
 * @param onSearchFiltersChange action to run when search filters change
 * @param onOrdersOpen action to run when the orders page is opened
 * @param onCartOpen action to run when the cart is opened
 * @param onWishlistOpen action to run when the wishlist is opened
 */
class ShopBar(
    private val onSearchFiltersChange: (ModelsSearchFilters) -> Unit,
    private val onOrdersOpen: () -> Unit,
    private val onCartOpen: () -> Unit,
    private val onWishlistOpen: () -> Unit
) : ModelsSearchBar(onSearchFiltersChange) {

    override fun create(controller: Controller) = super.create(controller).apply {
        val spacer = Pane().apply { HBox.setHgrow(this, Priority.ALWAYS) }
        val orders = Button("My Orders").apply { setOnAction { onOrdersOpen() } }
        val wishlist = Button("Wishlist").apply { setOnAction { onWishlistOpen() } }
        val cart = Button("Cart").apply { setOnAction { onCartOpen() } }

        children.addAll(spacer, orders, wishlist, cart)
    }
}