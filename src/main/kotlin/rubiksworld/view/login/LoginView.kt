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
        nickname.textProperty().addListener { _, _, new ->
            // Remove special characters
            nickname.text = new.replace("[^\\w_]".toRegex(), "")
        }

        val name = TextField()
        name.textProperty().addListener { _, _, new ->
            // Remove non-literal characters
            name.text = new.replace("[^a-zA-Z ]".toRegex(), "")
        }

        val surname = TextField()
        surname.textProperty().addListener { _, _, new ->
            // Remove non-literal characters
            surname.text = new.replace("[^a-zA-Z ]".toRegex(), "")
        }

        val button = Button("Log in").apply {
            setOnAction {
                if (nickname.text.isBlank() || name.text.isBlank() || surname.text.isBlank()) {
                    return@setOnAction
                }
                text = "Loading..."
                isDisable = true
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