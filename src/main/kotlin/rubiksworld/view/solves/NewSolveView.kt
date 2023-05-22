package rubiksworld.view.solves

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.ModelCard
import rubiksworld.view.ModelsPane
import rubiksworld.view.ModelsSearchBar
import rubiksworld.view.View
import rubiksworld.view.common.NumericTextField
import rubiksworld.view.common.TitleLabel

/**
 * The view that lets the user register a new solve.
 */
class NewSolveView : View<Pane> {

    private val minutes = SimpleStringProperty()
    private val seconds = SimpleStringProperty()
    private val model = SimpleObjectProperty<Model>()

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "new-solve-view"

        children += createTimeBox()
        children += createModelBox(controller)
    }

    private fun createTimeBox() = VBox().apply {
        val minutes = NumericTextField().apply {
            promptText = "mm"
            minutes.bind(textProperty())
        }

        val seconds = NumericTextField().apply {
            promptText = "ss"
            seconds.bind(textProperty())
        }

        children.addAll(
            TitleLabel("Time"),
            HBox(minutes, Label(":"), seconds).apply { styleClass += "time-box" }
        )
    }

    private fun createModelBox(controller: Controller) = VBox().apply {
        val modelsPane = ModelsPane()
        val searchBar = ModelsSearchBar(onSearchFiltersChange = { filters ->
            controller.async {
                val results = searchModels(filters)
                controller.sync {
                    val cards = results.map { model ->
                        ModelCard(model, showPrice = false).create(controller).apply {
                            setOnMouseClicked { this@NewSolveView.model.set(model) }
                        }
                    }
                    modelsPane.children.setAll(cards)
                }
            }
        })

        val searchBarNode = searchBar.create(controller)

        children.addAll(
            TitleLabel("Model (optional)"),
            searchBarNode,
            modelsPane
        )

        model.addListener { _, _, selected ->
            if (selected != null) {
                val card = ModelCard(selected, showPrice = false).create(controller)
                card.styleClass += "selected-model-card"

                modelsPane.children.setAll(card)
                searchBarNode.isDisable = true

                card.setOnMouseClicked { model.set(null) }
            } else {
                searchBarNode.isDisable = false
                searchBar.search()
            }
        }
    }
}