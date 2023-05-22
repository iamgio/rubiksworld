package rubiksworld.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.controller.ModelsSearchFilters

/**
 * A general bar that lets the user search models.
 *
 * @param onSearchFiltersChange action to run when search filters change
 */
open class ModelsSearchBar(
    private val onSearchFiltersChange: (ModelsSearchFilters) -> Unit
) : View<Pane> {

    private val text = SimpleStringProperty()
    private val speedCube = SimpleBooleanProperty()
    private val stickerless = SimpleBooleanProperty()
    private val magnetic = SimpleBooleanProperty()

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "shop-bar"
        children.addAll(createSearch())
    }

    private fun createSearch(): List<Node> {
        val field = TextField().apply {
            promptText = "Search product..."
            this@ModelsSearchBar.text.bind(textProperty())
        }

        val speedCube = ToggleButton("Speed Cube").apply {
            this@ModelsSearchBar.speedCube.bind(selectedProperty())
        }

        val stickerless = ToggleButton("Stickerless").apply {
            this@ModelsSearchBar.stickerless.bind(selectedProperty())
        }

        val magnetic = ToggleButton("Magnetic").apply {
            this@ModelsSearchBar.magnetic.bind(selectedProperty())
        }

        field.setOnKeyTyped { search() }
        speedCube.setOnAction { search() }
        stickerless.setOnAction { search() }
        magnetic.setOnAction { search() }

        search()

        return listOf(field, speedCube, stickerless, magnetic)
    }

    private val filters: ModelsSearchFilters
        get() = ModelsSearchFilters(text.get(), speedCube.get(), stickerless.get(), magnetic.get())

    fun search() {
        onSearchFiltersChange(filters)
    }
}