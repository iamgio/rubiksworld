package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.entity.toList
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.model.Model

/**
 *
 */
class DatabaseControllerImpl : DatabaseController {

    private lateinit var database: Database

    override fun initDatabase() {
        database = Database.connect("jdbc:mysql://localhost:3306/rubiksworld", user = "root", password = "giodatabase")
    }

    override fun searchModels(filters: ModelsSearchFilters): List<Model> {
        return database.models.toList()
    }
}