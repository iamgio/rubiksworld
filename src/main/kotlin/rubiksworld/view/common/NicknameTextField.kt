package rubiksworld.view.common

import javafx.scene.control.TextField

/**
 * A text field for nicknames.
 */
class NicknameTextField(text: String = "") : TextField(text) {

    init {
        // Remove special characters
        textProperty().addListener { _, _, new ->
            setText(new.replace("[^\\w_]".toRegex(), ""))
        }
    }
}