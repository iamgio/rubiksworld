package rubiksworld.view.shop

import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.view.View

/**
 * A bar for the shop that lets the user search models and access his cart and wishlist.
 *
 * @param onSearchFiltersChange action to run when search filters change
 */
class ShopBar(private val onSearchFiltersChange: (ModelsSearchFilters) -> Unit) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        children.addAll(createSearch())
    }

    private fun createSearch(): List<Node> {
        val field = TextField().apply {
            promptText = "Search product..."
        }

        val speedCube = ToggleButton("Speed Cube")
        val stickerless = ToggleButton("Stickerless")
        val magnetic = ToggleButton("Magnetic")

        val getFilters = {
            ModelsSearchFilters(field.text, speedCube.isSelected, magnetic.isSelected, stickerless.isSelected)
        }

        val search = { onSearchFiltersChange(getFilters()) }

        field.setOnKeyTyped { search() }
        speedCube.setOnAction { search() }
        stickerless.setOnAction { search() }
        magnetic.setOnAction { search() }

        search()

        return listOf(field, speedCube, magnetic, stickerless)
    }
}