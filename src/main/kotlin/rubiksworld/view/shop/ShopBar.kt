package rubiksworld.view.shop

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import rubiksworld.controller.Controller
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.view.View

/**
 * A bar for the shop that lets the user search models and access his cart and wishlist.
 *
 * @param onSearchFiltersChange action to run when search filters change
 * @param onCartOpen action to run when the cart is opened
 * @param onWishlistOpen action to run when the wishlist is opened
 */
class ShopBar(
    private val onSearchFiltersChange: (ModelsSearchFilters) -> Unit,
    private val onCartOpen: () -> Unit,
    private val onWishlistOpen: () -> Unit
) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "shop-bar"
        children.addAll(createSearch())
    }

    private fun createSearch(): List<Node> {
        val field = TextField().apply {
            promptText = "Search product..."
        }

        val speedCube = ToggleButton("Speed Cube")
        val stickerless = ToggleButton("Stickerless")
        val magnetic = ToggleButton("Magnetic")

        val spacer = Pane().apply { HBox.setHgrow(this, Priority.ALWAYS) }
        val wishlist = Button("Wishlist").apply { setOnAction { onWishlistOpen() } }
        val cart = Button("Cart").apply { setOnAction { onCartOpen() } }

        val getFilters = {
            ModelsSearchFilters(field.text, speedCube.isSelected, stickerless.isSelected, magnetic.isSelected)
        }

        val search = { onSearchFiltersChange(getFilters()) }

        field.setOnKeyTyped { search() }
        speedCube.setOnAction { search() }
        stickerless.setOnAction { search() }
        magnetic.setOnAction { search() }

        search()

        return listOf(field, speedCube, stickerless, magnetic, spacer, wishlist, cart)
    }
}