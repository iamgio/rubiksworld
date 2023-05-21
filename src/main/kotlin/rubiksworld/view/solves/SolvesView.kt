package rubiksworld.view.solves

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

        val table = SimpleTableView<Solve>(
            column("Position") { (items.indexOf(it) + 1).toString() },
            column("User") { it.user.nickname },
            column("Time") { formatTime(it.solveTime) },
            column("Model") {
                it.model?.let { model -> "${model.maker} ${model.name}" } ?: ""
            },
            column("Date") {
                it.registrationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
        )

        children += SolvesBar(onFiltersChange = {
            table.items.setAll(it)
        }).create(controller)

        children += VBox(table).apply { styleClass += "table-container" }
    }

    private fun formatTime(time: SolveTime) = buildString {
        if (time.minutes > 0) {
            append(time.minutes).append("m ")
        }
        append(time.seconds).append("s")
    }
}