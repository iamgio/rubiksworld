package rubiksworld.view

import javafx.scene.Scene
import javafx.scene.text.Font
import rubiksworld.controller.Controller

private const val INITIAL_WIDTH = 925.0
private const val INITIAL_HEIGHT = 600.0
private const val FONT_SIZE = 24.0

/**
 * The main scene.
 */
class MainScene : View<Scene> {

    init {
        Font.loadFont(javaClass.getResourceAsStream("/font/Manrope-Regular.ttf"), FONT_SIZE)
        Font.loadFont(javaClass.getResourceAsStream("/font/Manrope-Bold.ttf"), FONT_SIZE)
    }

    override fun create(controller: Controller) = Scene(
        MainSceneView().create(controller),
        INITIAL_WIDTH, INITIAL_HEIGHT
    ).apply {
        stylesheets += "/style/style.css"
    }
}