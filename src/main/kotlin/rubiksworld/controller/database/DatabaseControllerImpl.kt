package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.dsl.like
import org.ktorm.dsl.or
import org.ktorm.entity.filter
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
        return with(filters) {
            val textLike = "%$text%"
            database.models
                .filter {
                    text.isBlank() or
                            it.name.like(textLike) or
                            it.maker.like(textLike) or
                            it.category.like(textLike)
                }
                .filter { !onlySpeedCubes or it.isSpeedCube }
                .filter { !onlyStickerless or it.isStickerless }
                .filter { !onlyMagnetic or it.isMagnetic }
                .toList()
        }
    }
}