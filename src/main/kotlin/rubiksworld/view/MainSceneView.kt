package rubiksworld.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.login.LoginView
import rubiksworld.view.shop.*

/**
 * The main scene component.
 */
class MainSceneView : View<Pane> {

    override fun create(controller: Controller) = Pane().apply {
        sceneProperty().addListener { _, _, new ->
            prefWidthProperty().bind(new.widthProperty())
            prefHeightProperty().bind(new.heightProperty())
        }

        val tabPane = TabPane().also {
            it.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            it.prefWidthProperty().bind(prefWidthProperty())
        }

        // addLoginPage(tabPane, controller)
        // TODO development: remove before production
        // ---
        controller.user = controller.getUser("luca_rossi")!!
        populateTabs(tabPane, controller)
        // ---

        children += tabPane
    }

    private fun addLoginPage(tabPane: TabPane, controller: Controller) {
        val login = LoginView().also {
            it.onLogin = { nickname, name, surname ->
                controller.async {
                    controller.user = controller.insertUser(nickname, name, surname)
                    controller.sync {
                        tabPane.tabs.clear()
                        populateTabs(tabPane, controller)
                    }
                }
            }
        }.create(controller)

        tabPane.tabs += Tab("Log in", login)
    }

    private fun populateTabs(tabPane: TabPane, controller: Controller) {
        tabPane.tabs.addAll(
            Tab("Shop", ShopView(
                onUpdate = { tabPane.requestLayout() },
                onModelSelect = { model -> openTemporaryModelOverviewTab(model, controller, tabPane) },
                onCartOpen = { openTemporaryCartTab(controller, tabPane) },
                onWishlistOpen = { openTemporaryWishlistTab(controller, tabPane) }
            ).create(controller)),
            Tab("Solves")
        )
    }

    private fun openTemporaryModelOverviewTab(model: Model, controller: Controller, tabPane: TabPane) {
        val tab = Tab(model.name, ModelOverviewView(model).create(controller))
        openTemporaryTab(tabPane, tab)
    }

    private fun openTemporaryCartTab(controller: Controller, tabPane: TabPane) {
        val tab = Tab("Cart", CartView(onCheckout = {
            tabPane.selectionModel.select(0)
            openTemporaryCheckoutTab(controller, tabPane)
        }).create(controller))
        openTemporaryTab(tabPane, tab)
    }

    private fun openTemporaryCheckoutTab(controller: Controller, tabPane: TabPane) {
        val tab = Tab("Checkout", CheckoutView().create(controller))
        openTemporaryTab(tabPane, tab)
    }

    private fun openTemporaryWishlistTab(controller: Controller, tabPane: TabPane) {
        val tab = Tab("Wishlist", WishlistView(onModelSelect = {
            tabPane.selectionModel.select(0)
            openTemporaryModelOverviewTab(it, controller, tabPane)
        }).create(controller))
        openTemporaryTab(tabPane, tab)
    }

    /**
     * Opens a tab that gets closed when it is unselected.
     * @param tabPane tab pane to add the tab to
     * @param tab tab to open
     */
    private fun openTemporaryTab(tabPane: TabPane, tab: Tab) {
        tabPane.tabs += tab
        tabPane.selectionModel.select(tab)
        tab.setOnSelectionChanged {
            tabPane.tabs -= tab
        }
    }
}