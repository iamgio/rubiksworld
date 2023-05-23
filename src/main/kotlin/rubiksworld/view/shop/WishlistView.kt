package rubiksworld.view.shop

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.ModelsPane
import rubiksworld.view.View
import rubiksworld.view.common.fitToViewport

/**
 * User's wishlist view.
 * @param onModelSelect action to run when a model is selected
 */
class WishlistView(private val onModelSelect: (Model) -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        val modelsPane = ModelsPane()

        controller.getWishlist(controller.user).forEach { model ->
            modelsPane.children += RemovableModelCard(model) { card ->
                controller.removeFromWishlist(controller.user, model)
                modelsPane.children -= card
            }.create(controller).apply {
                setOnMousePressed { onModelSelect(model) }
            }
        }

        children += ScrollPane(modelsPane).apply { fitToViewport(includeBar = false) }
    }
}