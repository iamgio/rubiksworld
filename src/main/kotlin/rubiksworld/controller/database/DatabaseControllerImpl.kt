package rubiksworld.controller.database

import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.model.Category
import rubiksworld.model.Model

/**
 *
 */
class DatabaseControllerImpl : DatabaseController {

    override fun searchModels(filters: ModelsSearchFilters): List<Model> {
        return listOf(
            Model(
                "name", "maker", Category("cat"), 10.0,
                5.0, "img", true, true, false
            )
        )
    }
}