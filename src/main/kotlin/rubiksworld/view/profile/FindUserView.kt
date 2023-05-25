package rubiksworld.view.profile

import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.controller.Controller
import rubiksworld.model.User
import rubiksworld.view.View
import rubiksworld.view.common.TitleLabel

/**
 * View for searching users.
 *
 * @param onProfileOpen action to run when a user's profile should be opened
 */
class FindUserView(private val onProfileOpen: (User) -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "find-user-view"

        val field = TextField().apply {
            promptText = "Search for someone..."
        }

        val resultsBox = VBox().apply {
            styleClass += "results-box"
        }

        children += HBox().apply {
            children += field
            styleClass += "shop-bar"
        }
        children += resultsBox

        field.textProperty().addListener { _ ->
            val users = controller.findUser(field.text, except = controller.user)

            resultsBox.children.clear()
            users.forEach {
                resultsBox.children.addAll(
                    createResultItem(it),
                    Separator()
                )
            }
        }
    }

    private fun createResultItem(user: User) = HBox().apply {
        children.addAll(
            TitleLabel(user.nickname),
            Label("${user.name} ${user.surname}"),
        )

        setOnMouseClicked { onProfileOpen(user) }
    }
}