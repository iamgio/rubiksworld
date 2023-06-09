package rubiksworld.view

import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.common.doublePercentageToInt
import rubiksworld.common.formatPrice
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.common.ImageCache

private const val WIDTH = 150.0
private const val LOADING_IMAGE_HEIGHT = 92.0

/**
 * The graphic representation of a model.
 *
 * @param model model to represent
 */
open class ModelCard(private val model: Model, private val showPrice: Boolean = true) : View<Pane> {

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

        if (!showPrice) {
            return@apply
        }

        // Base price
        val priceHbox = HBox(
            Label(formatPrice(model.price)).apply {
                styleClass += if (model.discountPercentage == null) "price" else "initial-price"
            }
        ).apply { styleClass += "price-box" }

        // Discounts
        model.discountPercentage?.let {
            val price = formatPrice(calcDiscountedPrice(model.price, it))
            val discount = "-${doublePercentageToInt(it)}%"
            priceHbox.children += Label(price).apply { styleClass += "price" }
            priceHbox.children += Label(discount).apply { styleClass += "discount" }
        }

        children += priceHbox
    }
}