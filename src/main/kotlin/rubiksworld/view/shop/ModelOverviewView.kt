package rubiksworld.view.shop

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.View

/**
 *
 */
class ModelOverviewView(private val model: Model) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        children += createRightColumn(controller)
    }

    private fun createRightColumn(controller: Controller) = VBox().apply {
        styleClass += "model-overview"

        fun title(text: String) = Label(text).apply { styleClass += "section-title" }

        children += title("${model.maker} ${model.name}")

        children += title("Category")
        children += Label(model.category.name)

        children += title("Tags")
        // TODO tags

        val parts = controller.getCustomizableParts(model)

        parts.forEach { part ->
            children += title(part.part)
        }
    }
}