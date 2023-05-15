package rubiksworld.view.login

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View

/**
 *
 */
class LoginView : View<Pane> {

    var onLogin: ((String, String, String) -> Unit)? = null

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "login-view"

        val nickname = TextField()
        val name = TextField()
        val surname = TextField()

        val button = Button("Log in").apply {
            setOnAction {
                onLogin?.invoke(nickname.text, name.text, surname.text)
            }
        }

        children += VBox(
            Label("Nickname"), nickname,
            Label("Name"), name,
            Label("Surname"), surname
        ).apply { styleClass += "fields-box" }
        children += button

        Platform.runLater { nickname.requestFocus() }
    }
}