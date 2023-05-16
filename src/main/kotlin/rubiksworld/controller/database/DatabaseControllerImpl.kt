package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import rubiksworld.common.calcDiscountedPrice
import rubiksworld.controller.ModelsSearchFilters
import rubiksworld.controller.database.tables.Applications
import rubiksworld.controller.database.tables.Customizations
import rubiksworld.model.*

/**
 *
 */
open class DatabaseControllerImpl : DatabaseController {

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

    override fun getCustomizableParts(model: Model): List<CustomizablePart> {
        return database.customizableParts
            .filter { it.modelName eq model.name }
            .filter { it.modelMaker eq model.maker }
            .toList()
    }

    override fun getAvailableCustomizations(part: CustomizablePart): List<Customization> {
        return database.customizations
            .filter { it.modelName eq part.model.name }
            .filter { it.modelMaker eq part.model.maker }
            .filter { it.part eq part.part }
            .toList()
    }

    override fun getAllUsers(): List<User> {
        return database.users.toList()
    }

    override fun getUser(nickname: String): User? {
        return database.users
            .find { it.nickname eq nickname }
    }

    override fun insertUser(nickname: String, name: String, surname: String): User {
        val match = getUser(nickname)
        if (match != null) {
            match.name = name
            match.surname = surname
            match.flushChanges()
            return match
        }

        val newUser = User {
            this.nickname = nickname
            this.name = name
            this.surname = surname
        }
        database.users.add(newUser)
        return newUser
    }

    override fun insertModelVersion(model: Model, customizations: List<Customization>): ModelVersion {
        val modelVersion = ModelVersion {
            this.model = model
        }
        database.modelVersions.add(modelVersion)

        customizations.forEach { customization ->
            val application = Application {
                this.customization = customization
                this.modelVersion = modelVersion
            }
            database.applications.add(application)
        }

        return modelVersion
    }

    override fun getModelVersionPrice(modelVersion: ModelVersion): Double {
        val customizationsPrice = database.from(Applications)
            .leftJoin(Customizations,
                on = (Applications.part eq Customizations.part)
                        and (Applications.change eq Customizations.change)
                        and (Applications.modelName eq Customizations.modelName)
                        and (Applications.modelMaker eq Customizations.modelMaker))
            .select(sum(Customizations.price))
            .where(Applications.modelVersionId eq modelVersion.id)
            .map { it.getDouble(1) }
            .firstOrNull() ?: .0

        val model = getFullModelInfo(modelVersion.model)
        val subtotal = model.price + customizationsPrice
        return model.discountPercentage?.let { calcDiscountedPrice(subtotal, it) } ?: subtotal
    }

    override fun getAppliedCustomizations(modelVersion: ModelVersion): List<Application> {
        return database.applications
            .filter { it.modelVersionId eq modelVersion.id }
            .toList()
    }

    override fun getFullModelInfo(partialModel: Model): Model {
        return database.models
            .filter { it.name eq partialModel.name }
            .first { it.maker eq partialModel.maker }
    }

    override fun addToCart(user: User, modelVersion: ModelVersion) {
        val cartPresence = CartPresence {
            this.user = user
            this.modelVersion = modelVersion
        }
        database.cartPresences.add(cartPresence)
    }

    override fun getCart(user: User): List<ModelVersion> {
        return database.cartPresences
            .filter { it.userNickname eq user.nickname }
            .map { it.modelVersion }
    }
}