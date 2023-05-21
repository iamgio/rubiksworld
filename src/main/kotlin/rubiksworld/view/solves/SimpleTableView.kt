package rubiksworld.view.solves

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView

/**
 * A simple [TableView] shortcut.
 *
 * @param columns columns to insert
 */
class SimpleTableView<T>(vararg columns: TableColumn<T, String>) : TableView<T>() {

    init {
        super.getColumns().addAll(columns)

        super.getColumns().forEach { column ->
            column.prefWidthProperty().bind(layoutBoundsProperty().map {
                it.width / super.getColumns().size - 1
            })
        }
    }
}

/**
 * @param name column name
 * @param binding mapping to an item to its representation
 * @return a new column
 */
fun <T> column(name: String, binding: TableView<T>.(T) -> String) = TableColumn<T, String>(name).apply {
    setCellValueFactory { SimpleStringProperty(binding(tableView, it.value)) }
}