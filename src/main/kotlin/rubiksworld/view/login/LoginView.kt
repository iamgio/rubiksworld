package rubiksworld.view.login

import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.view.View
import rubiksworld.view.common.LiteralTextField
import rubiksworld.view.common.NicknameTextField

/**
 *
 */
class LoginView : View<Pane> {

    var onLogin: ((String, String, String) -> Unit)? = null

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "login-view"

        val nickname = NicknameTextField()
        val name = LiteralTextField()
        val surname = LiteralTextField()

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