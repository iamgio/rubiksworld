package rubiksworld.view.solves

import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.view.View

/**
 * Filters bar for the Solves view.
 */
class SolvesBar : View<Pane> {

    override fun create(controller: Controller) = HBox().apply {
        styleClass += "shop-bar"

        val group = ToggleGroup()

        val personal = createButton("Personal", group) {

        }

        val friends = createButton("Friends", group) {

        }

        val global = createButton("Global", group) {

        }

        personal.fire() // TODO friends is default

        children.addAll(personal, friends, global)
    }

    private fun createButton(text: String, group: ToggleGroup, onSelect: () -> Unit) = RadioButton(text).apply {
        styleClass.setAll("toggle-button")
        toggleGroup = group
        setOnAction { onSelect() }
    }
}