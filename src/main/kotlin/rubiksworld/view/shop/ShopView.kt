package rubiksworld.view.shop

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.ModelCard
import rubiksworld.view.ModelsPane
import rubiksworld.view.View

private const val BARS_HEIGHT = 40.0 // TODO make dynamic

/**
 * The shop section content.
 *
 * @param onUpdate action to run when the UI should be updated
 * @param onModelSelect action to run when a model is selected
 * @param onOrdersOpen action to run when the orders page should be opened
 * @param onCartOpen action to run when the cart should be opened
 * @param onWishlistOpen action to run when the wishlist should be opened
 */
class ShopView(
    private val onUpdate: () -> Unit,
    private val onModelSelect: (Model) -> Unit,
    private val onOrdersOpen: () -> Unit,
    private val onCartOpen: () -> Unit,
    private val onWishlistOpen: () -> Unit,
) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "shop-view"

        val modelsPane = ModelsPane()

        val bar = ShopBar(
            onSearchFiltersChange = { filters ->
                controller.async {
                    val results = searchModels(filters)
                    controller.sync {
                        val cards = results.map { model ->
                            ModelCard(model).create(controller).apply {
                                setOnMouseClicked { onModelSelect(model) }
                            }
                        }
                        modelsPane.children.setAll(cards)
                        onUpdate()
                    }
                }
            },
            onOrdersOpen, onCartOpen, onWishlistOpen
        ).create(controller)

        val scrollPane = ScrollPane(modelsPane).apply {
            isFitToWidth = true
            prefHeightProperty().bind(sceneProperty().map { it?.height?.minus(BARS_HEIGHT) ?: 0 })
        }

        children.addAll(bar, scrollPane)
    }
}