package rubiksworld.view

import javafx.geometry.Orientation
import javafx.scene.layout.FlowPane

/**
 * A grid pane for displaying models.
 */
class ModelsPane : FlowPane(Orientation.HORIZONTAL) {

    init {
        styleClass += "models-pane"
    }
}