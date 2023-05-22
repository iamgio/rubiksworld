package rubiksworld.view.profile

import javafx.scene.control.Label
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import rubiksworld.common.asString
import rubiksworld.controller.Controller
import rubiksworld.model.Solve
import rubiksworld.model.User
import rubiksworld.view.View
import rubiksworld.view.common.TitleLabel
import rubiksworld.view.solves.SimpleTableView
import rubiksworld.view.solves.column

/**
 * User's profile view.
 *
 * @param user profile owner
 * @param onUserRedirect action to run when it is required to show another user's profile
 */
class ProfileView(private val user: User, private val onUserRedirect: (User) -> Unit) : View<Pane> {

    override fun create(controller: Controller) = VBox().apply {
        styleClass += "profile-view"

        children += HBox(
            TitleLabel(user.nickname),
            Label(user.name + " " + user.surname)
        ).apply {
            styleClass += "name-box"
        }

        children += createSolvesBox(controller)
        children += createFriendsBox(controller)
    }

    private fun createSolvesBox(controller: Controller) = VBox().apply {
        children += TitleLabel("Best solves")

        val table = SimpleTableView<Solve>(
            column("Model") { it.model.asString(ifNull = "-") },
            column("Time") { it.solveTime.toString() }
        )

        table.items.setAll(controller.getTopSolvesByModel(user))

        children += table
    }

    private fun createFriendsBox(controller: Controller) = VBox().apply {
        children += TitleLabel("Friends")

        children += FlowPane().apply {
            children.addAll(controller.getFriends(user).map { friendNode(it) })
        }
    }

    private fun friendNode(friend: User) = Label(friend.nickname).apply {
        styleClass += "friend-node"
        setOnMouseClicked { onUserRedirect(friend) }
    }
}