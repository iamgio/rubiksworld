package rubiksworld.view.shop

import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Customization
import rubiksworld.model.Model
import rubiksworld.view.ImageCache
import rubiksworld.view.View

private const val IMAGE_WIDTH = 250.0

/**
 *
 */
class ModelOverviewView(private val model: Model) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "model-overview"
        children += ImageView(ImageCache[model.imageUrl]).apply {
            isPreserveRatio = true
            fitWidth = IMAGE_WIDTH
        }
        children += createRightColumn(controller)
    }

    private fun createRightColumn(controller: Controller) = VBox().apply {
        fun title(text: String) = Label(text).apply { styleClass += "section-title" }

        children += title("${model.maker} ${model.name}")

        children += title("Category")
        children += Label(model.category.name)

        children += title("Tags")
        children += Label(getTags().joinToString(", "))

        val parts = controller.getCustomizableParts(model)

        parts.forEach { part ->
            children += title(part.part)

            val customizations = controller.getAvailableCustomizations(part)
            children += createCustomizationsBox(customizations)
        }

        children += CheckBox("Wishlist").apply {
            // isSelected = check...
        }

        children += Button("Add to cart").apply {
            // ...
        }
    }

    private fun createCustomizationsBox(customizations: List<Customization>) = HBox().apply {
        styleClass += "customizations-box"
        val group = ToggleGroup()

        val buttons = customizations.map { customization ->
            RadioButton(customization.change).apply {
                toggleGroup = group
                isSelected = customization.isDefault
            }
        }

        children.addAll(buttons)
    }

    private fun getTags(): List<String> = buildList {
        if (model.isSpeedCube) {
            add("Speed Cube")
        }
        if (model.isStickerless) {
            add("Stickerless")
        }
        if (model.isMagnetic) {
            add("Magnetic")
        }
        if (isEmpty()) {
            add("None")
        }
    }
}