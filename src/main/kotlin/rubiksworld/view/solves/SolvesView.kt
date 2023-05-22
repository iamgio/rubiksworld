package rubiksworld.view.solves

import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.asString
import rubiksworld.common.defaultFormat
import rubiksworld.controller.Controller
import rubiksworld.model.Solve
import rubiksworld.view.View

/**
 * The Solves section content.
 *
 * @param onRegister action to run when a new solve should be registered
 */
class SolvesView(private val onRegister: () -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "solves-view"

        val table = SimpleTableView<Solve>(
            column("Position") { (items.indexOf(it) + 1).toString() },
            column("User") { it.user.nickname },
            column("Time") { it.solveTime.toString() },
            column("Model") { it.model.asString(ifNull = "-") },
            column("Date") { it.registrationDate?.defaultFormat() ?: "-" }
        )

        children += SolvesBar(onFiltersChange = {
            table.items.setAll(it)
        }, onRegister).create(controller)

        children += VBox(table).apply { styleClass += "table-container" }
    }
}