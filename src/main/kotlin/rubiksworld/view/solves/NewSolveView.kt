package rubiksworld.view.solves

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View
import rubiksworld.view.common.NumericTextField
import rubiksworld.view.common.TitleLabel

/**
 * The view that lets the user register a new solve.
 */
class NewSolveView : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "new-solve-view"

        val minutes = NumericTextField().apply { promptText = "mm" }
        val seconds = NumericTextField().apply { promptText = "ss" }

        children += VBox(
            TitleLabel("Time"),
            HBox(minutes, Label(":"), seconds).apply { styleClass += "time-box" }
        )
    }
}