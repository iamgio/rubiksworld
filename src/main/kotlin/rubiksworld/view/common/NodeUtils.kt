package rubiksworld.view.common

import javafx.application.Platform
import javafx.scene.control.ScrollPane

/**
 * Makes a [ScrollPane] fit to the available size.
 */
fun ScrollPane.fitToViewport(includeBar: Boolean = true) {
    isFitToWidth = true
    sceneProperty().addListener { _, _, scene ->
        if (scene != null) {
            Platform.runLater {
                val tabHeaderHeight = scene.lookup(".tab-header-area")?.layoutBounds?.height ?: .0
                val barHeight = scene.takeIf { includeBar }?.lookup(".shop-bar")?.layoutBounds?.height ?: .0
                prefHeight = scene.height - tabHeaderHeight - barHeight
            }
        }
    }
}