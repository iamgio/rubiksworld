package rubiksworld.view.solves

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.Solve
import rubiksworld.model.SolveTime
import rubiksworld.view.View
import java.time.format.DateTimeFormatter

/**
 * The Solves section content.
 */
class SolvesView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "solves-view"

        val table = TableView<Solve>()

        table.columns.addAll(
            column("Position") { (table.items.indexOf(it) + 1).toString() },
            column("User") { it.user.nickname },
            column("Time") { formatTime(it.solveTime) },
            column("Model") {
                it.model?.let { model -> "${model.maker} ${model.name}" } ?: ""
            },
            column("Date") {
                it.registrationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        )

        table.columns.forEach { column ->
            column.prefWidthProperty().bind(table.layoutBoundsProperty().map {
                it.width / table.columns.size - 1
            })
        }

        //table.items.setAll(controller.getSolves(controller.user))

        children += SolvesBar(onFiltersChange = {
            table.items.setAll(it)
        }).create(controller)

        children += VBox(table).apply { styleClass += "table-container" }
    }

    private fun column(name: String, binding: (Solve) -> String) = TableColumn<Solve, String>(name).apply {
        setCellValueFactory { SimpleStringProperty(binding(it.value)) }
    }

    private fun formatTime(time: SolveTime) = buildString {
        if (time.minutes > 0) {
            append(time.minutes).append("m ")
        }
        append(time.seconds).append("s")
    }
}