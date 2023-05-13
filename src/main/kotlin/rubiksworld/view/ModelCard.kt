package rubiksworld.view

import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.shop.CURRENCY

private const val WIDTH = 150.0
private const val LOADING_IMAGE_HEIGHT = 92.0

/**
 * The graphic representation of a model.
 *
 * @param model model to represent
 */
class ModelCard(private val model: Model) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "model-card"
        prefWidth = WIDTH

        // Image
        ImageCache.fetch(
            model.imageUrl, controller,
            onLoadingStart = {
                children += Rectangle(WIDTH, LOADING_IMAGE_HEIGHT).apply { styleClass += "placeholder" }
            },
            onLoadingEnd = { image, isCached ->
                val imageView = ImageView(image).apply {
                    isPreserveRatio = true
                    fitWidth = WIDTH
                }
                if (isCached) {
                    children += imageView
                } else {
                    children[0] = imageView
                }
            }
        )

        // Info
        children += Label(model.maker).apply { styleClass += "maker" }
        children += Label(model.name).apply { styleClass += "name" }

        // Base price
        val priceHbox = HBox(
            Label(formatPrice(model.price)).apply {
                styleClass += if (model.discountPercentage == null) "price" else "initial-price"
            }
        ).apply { styleClass += "price-box" }

        // Discounts
        model.discountPercentage?.let {
            val price = formatPrice(model.price - model.price * it)
            val discount = "-${(it * 100).toInt()}%"
            priceHbox.children += Label(price).apply { styleClass += "price" }
            priceHbox.children += Label(discount).apply { styleClass += "discount" }
        }

        children += priceHbox
    }

    /**
     * @return [price] with 2 decimal places + currency symbol
     */
    private fun formatPrice(price: Double) = String.format("%.02f", price) + CURRENCY
}