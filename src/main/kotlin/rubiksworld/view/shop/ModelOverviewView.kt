package rubiksworld.view.shop

import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.doublePercentageToInt
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.model.Customization
import rubiksworld.model.Model
import rubiksworld.view.ImageCache
import rubiksworld.view.View

private const val IMAGE_WIDTH = 250.0

/**
 * The view that contains information about a model and lets the user add it to their wishlist and cart.
 *
 * @param model target model
 */
class ModelOverviewView(private val model: Model) : View<Pane> {

    private val price: DoubleProperty = SimpleDoubleProperty(model.price)
    private val discountedPrice: DoubleProperty = SimpleDoubleProperty()
    private val appliedCustomizations = mutableListOf<Customization>()

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
            children += createCustomizationsBox(customizations, controller)
        }

        children += title("Total")

        val priceBox = HBox().apply { styleClass += "price-box" }

        model.discountPercentage?.let {
            priceBox.children += Label().apply {
                styleClass += "initial-price"
                textProperty().bind(price.map { formatPrice(it.toDouble()) })
            }
        }

        priceBox.children += Label().apply {
            textProperty().bind(discountedPrice.map { formatPrice(it.toDouble()) })
        }

        model.discountPercentage?.let {
            val discount = "-${doublePercentageToInt(it)}%"
            priceBox.children += Label(discount).apply { styleClass += "discount" }
        }

        children += priceBox

        children += CheckBox("Wishlist").apply {
            // isSelected = check...
        }

        children += Button("Add to cart").apply {
            controller.addToCart(controller.user, model, appliedCustomizations)
        }
    }

    private fun createCustomizationsBox(customizations: List<Customization>, controller: Controller) = HBox().apply {
        styleClass += "customizations-box"
        val group = ToggleGroup()

        val buttons = customizations.map { customization ->
            RadioButton(customization.change).apply {
                toggleGroup = group

                selectedProperty().addListener { _ ->
                    if (isSelected) {
                        appliedCustomizations += customization
                    } else {
                        appliedCustomizations -= customization
                    }

                    if (model.discountPercentage != null) {
                        price.set(controller.calcPrice(model, appliedCustomizations, applyDiscount = false))
                    }
                    discountedPrice.set(controller.calcPrice(model, appliedCustomizations))
                }

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