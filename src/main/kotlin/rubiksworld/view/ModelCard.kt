package rubiksworld.view

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.shop.CURRENCY
import kotlin.concurrent.thread

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

        // Image placeholder
        children += Rectangle(WIDTH, LOADING_IMAGE_HEIGHT).apply { styleClass += "placeholder" }

        // Image
        thread {
            val image = Image(model.imageUrl)
            controller.sync {
                children[0] = ImageView(image).also {
                    it.isPreserveRatio = true
                    it.fitWidth = WIDTH
                }
            }
        }
        /*children += ImageView(model.imageUrl).also {
            it.isPreserveRatio = true
            it.fitWidth = WIDTH
        }*/

        // Info
        children += Label(model.maker).apply { styleClass += "maker" }
        children += Label(model.name).apply { styleClass += "name" }

        // Base price
        val priceHbox = HBox(
            Label(model.price.toString() + CURRENCY).apply {
                styleClass += if (model.discountPercentage == null) "price" else "initial-price"
            }
        ).apply { styleClass += "price-box" }

        // Discounts
        model.discountPercentage?.let {
            val price = String.format("%.2f", model.price - model.price * it) + CURRENCY
            val discount = "-${(it * 100).toInt()}%"
            priceHbox.children += Label(price).apply { styleClass += "price" }
            priceHbox.children += Label(discount).apply { styleClass += "discount" }
        }

        children += priceHbox
    }
}