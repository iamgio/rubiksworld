package rubiksworld.view.solves

import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.model.Solve
import rubiksworld.view.View

/**
 * Filters bar for the Solves view.
 */
class SolvesBar(private val onFiltersChange: (List<Solve>) -> Unit) : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "shop-bar"

        val group = ToggleGroup()

        val personal = createButton("Personal", group) {
            controller.getPersonalSolves(controller.user)
        }

        val friends = createButton("Friends", group) {
            controller.getFriendsSolves(controller.user)
        }

        val global = createButton("Global", group) {
            controller.getAllSolves()
        }

        friends.fire() // Default

        children.addAll(personal, friends, global)
    }

    private fun createButton(text: String, group: ToggleGroup,
                             onSelect: () -> List<Solve>) = RadioButton(text).apply {
        styleClass.setAll("toggle-button")
        toggleGroup = group
        setOnAction { onFiltersChange(onSelect()) }
    }
}