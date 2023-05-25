package rubiksworld.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.model.User
import rubiksworld.view.login.LoginView
import rubiksworld.view.profile.FindUserView
import rubiksworld.view.profile.ProfileView
import rubiksworld.view.shop.*
import rubiksworld.view.solves.NewSolveView
import rubiksworld.view.solves.SolvesView

/**
 * The main scene component.
 */
class MainSceneView : View<Pane> {

    private lateinit var controller: Controller
    private lateinit var tabPane: TabPane

    override fun create(controller: Controller) = Pane().apply {
        sceneProperty().addListener { _, _, new ->
            prefWidthProperty().bind(new.widthProperty())
            prefHeightProperty().bind(new.heightProperty())
        }

        val tabPane = TabPane().also {
            it.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            it.prefWidthProperty().bind(prefWidthProperty())
        }

        this@MainSceneView.controller = controller
        this@MainSceneView.tabPane = tabPane

        // addLoginPage(tabPane, controller)
        // TODO development: remove before production
        // ---
        controller.user = controller.getUser("luca_rossi")!!
        populateTabs()
        // ---

        children += tabPane
    }

    private fun addLoginPage() {
        val login = LoginView().also {
            it.onLogin = { nickname, name, surname ->
                controller.async {
                    controller.user = controller.insertUser(nickname, name, surname)
                    controller.sync {
                        tabPane.tabs.clear()
                        populateTabs()
                    }
                }
            }
        }.create(controller)

        tabPane.tabs += Tab("Log in", login)
    }

    private fun populateTabs() {
        tabPane.tabs.setAll(
            Tab("Shop", ShopView(
                onUpdate = { tabPane.requestLayout() },
                onModelSelect = { model -> openTemporaryModelOverviewTab(model) },
                onOrdersOpen = { openTemporaryOrdersTab() },
                onCartOpen = { openTemporaryCartTab() },
                onWishlistOpen = { openTemporaryWishlistTab() }
            ).create(controller)),

            Tab("Solves", SolvesView(onRegister = {
                openTemporaryNewSolveTab()
            }).create(controller)),

            Tab("Profile", ProfileView(controller.user, onUserRedirect = {
                openTemporaryProfileTab(it)
            }).create(controller)),

            Tab("Find user", FindUserView(onProfileOpen = {
                openTemporaryProfileTab(it)
            }).create(controller))
        )
    }

    private fun openTemporaryModelOverviewTab(model: Model) {
        val tab = Tab(model.name, ModelOverviewView(model).create(controller))
        openTemporaryTab(tab)
    }

    private fun openTemporaryCartTab() {
        val tab = Tab("Cart", CartView(onCheckout = {
            tabPane.selectionModel.select(0)
            openTemporaryCheckoutTab()
        }).create(controller))
        openTemporaryTab(tab)
    }

    private fun openTemporaryCheckoutTab() {
        val tab = Tab("Checkout", CheckoutView(onCheckoutComplete = {
            tabPane.selectionModel.select(0)
            openTemporaryOrdersTab()
        }).create(controller))
        openTemporaryTab(tab)
    }

    private fun openTemporaryOrdersTab() {
        val tab = Tab("Orders", OrdersView().create(controller))
        openTemporaryTab(tab)
    }

    private fun openTemporaryWishlistTab() {
        val tab = Tab("Wishlist", WishlistView(onModelSelect = {
            tabPane.selectionModel.select(0)
            openTemporaryModelOverviewTab(it)
        }).create(controller))
        openTemporaryTab(tab)
    }

    private fun openTemporaryProfileTab(user: User) {
        val view = ProfileView(user, onUserRedirect = {
            tabPane.selectionModel.select(0)
            openTemporaryProfileTab(it)
        })

        val tab = Tab(user.nickname, view.create(controller))

        openTemporaryTab(tab)

        // Refresh changes on close if changes were made
        tabPane.selectionModel.selectedItemProperty().addListener { _, old, _ ->
            if (old == tab && view.requiresRefresh) {
                populateTabs()
            }
        }
    }

    private fun openTemporaryNewSolveTab() {
        val initialTabIndex = tabPane.selectionModel.selectedIndex
        val tab = Tab("New solve", NewSolveView(onRegistered = {
            populateTabs()
            tabPane.selectionModel.select(initialTabIndex)
        }).create(controller))
        openTemporaryTab(tab)
    }

    /**
     * Opens a tab that gets closed when it is unselected.
     * @param tab tab to open
     */
    private fun openTemporaryTab(tab: Tab) {
        tabPane.tabs += tab
        tabPane.selectionModel.select(tab)
        tab.setOnSelectionChanged {
            tabPane.tabs -= tab
        }
    }
}