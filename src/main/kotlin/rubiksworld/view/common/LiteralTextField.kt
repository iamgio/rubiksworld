package rubiksworld.view.common

import javafx.scene.control.TextField

/**
 * A letters-only text field.
 */
class LiteralTextField(text: String = "") : TextField(text) {

    init {
        // Remove non-literal characters
        textProperty().addListener { _, _, new ->
            setText(new.replace("[^a-zA-Z ]".toRegex(), ""))
        }
    }
}