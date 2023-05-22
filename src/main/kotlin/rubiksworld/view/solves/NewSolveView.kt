package rubiksworld.view.solves

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.model.SolveTime
import rubiksworld.view.ModelCard
import rubiksworld.view.ModelsPane
import rubiksworld.view.ModelsSearchBar
import rubiksworld.view.View
import rubiksworld.view.common.NumericTextField
import rubiksworld.view.common.TitleLabel

/**
 * The view that lets the user register a new solve.
 */
class NewSolveView(private val onRegistered: () -> Unit) : View<Pane> {

    private val minutes = SimpleStringProperty()
    private val seconds = SimpleStringProperty()
    private val model = SimpleObjectProperty<Model>()

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "new-solve-view"

        children += createTimeBox(controller)
        children += createModelBox(controller)
    }

    private fun createTimeBox(controller: Controller) = VBox().apply {
        val minutesField = NumericTextField().apply {
            promptText = "mm"
            minutes.bind(textProperty())
        }

        val secondsField = NumericTextField().apply {
            promptText = "ss"
            seconds.bind(textProperty())
        }

        val spacer = Pane().apply { HBox.setHgrow(this, Priority.ALWAYS) }

        val register = Button("Register solve")
        register.setOnAction {
            val minutes = this@NewSolveView.minutes.get().toIntOrNull()
            if (minutes == null) {
                minutesField.styleClass += "error-field"
            }

            val seconds = this@NewSolveView.seconds.get().toIntOrNull()
            if (seconds == null) {
                secondsField.styleClass += "error-field"
            }

            if (minutes != null && seconds != null) {
                controller.insertSolve(controller.user, model.get(), SolveTime(minutes, seconds))
                onRegistered()
            }
        }

        children.addAll(
            TitleLabel("Time"),
            HBox(minutesField, Label(":"), secondsField, spacer, register).apply { styleClass += "time-box" }
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