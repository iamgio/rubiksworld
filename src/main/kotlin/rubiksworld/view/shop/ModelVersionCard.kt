package rubiksworld.view.shop

import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.model.ModelVersion
import rubiksworld.view.ModelCard
import rubiksworld.view.View

/**
 * The card for a customized [ModelVersion].
 */
class ModelVersionCard(private val modelVersion: ModelVersion) : View<Pane> {

    override fun create(controller: Controller): Pane {
        val model = controller.getFullModelInfo(modelVersion.model).copy()
        model.price = controller.getModelVersionPrice(modelVersion)
        model.discountPercentage = null

        return ModelCard(model).create(controller)
    }
}