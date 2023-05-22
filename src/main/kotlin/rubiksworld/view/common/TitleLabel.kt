package rubiksworld.view.common

import javafx.scene.control.Label

/**
 * A title.
 */
class TitleLabel(text: String) : Label(text) {

    init {
        styleClass += "title"
    }
}