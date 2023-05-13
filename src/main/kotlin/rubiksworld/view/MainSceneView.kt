package rubiksworld.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import rubiksworld.controller.Controller
import rubiksworld.model.Model
import rubiksworld.view.shop.ModelOverviewView
import rubiksworld.view.shop.ShopView

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
        tabPane.tabs.addAll(
            Tab("Shop", ShopView(
                onUpdate = { tabPane.requestLayout() },
                onModelSelect = { model -> openTemporaryModelOverviewTab(model, controller, tabPane) }
            ).create(controller)),
            Tab("Solves")
        )
        children += tabPane
    }

    private fun openTemporaryModelOverviewTab(model: Model, controller: Controller, tabPane: TabPane) {
        val tab = Tab(model.name, ModelOverviewView(model).create(controller))
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