package rubiksworld.view.common

import javafx.scene.control.TextField

/**
 * A numbers-only text field.
 */
class NumericTextField(text: String = "") : TextField(text) {

    init {
        // Remove non-literal characters
        textProperty().addListener { _, _, new ->
            setText(new.replace("\\D".toRegex(), ""))
        }
    }
}