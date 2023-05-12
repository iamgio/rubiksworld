package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * A model that is available in the shop.
 */
interface Model : Entity<Model> {

    companion object : Entity.Factory<Model>()

    /**
     * Model name.
     */
    val name: String

    /**
     * Model maker.
     */
    val maker: String

    /**
     * Cctegory this model belongs to.
     */
    val category: Category

    /**
     * Model price.
     */
    val price: Double

    /**
     * If not `null`, discount percentage on this model's final price.
     */
    val discountPercentage: Double?

    /**
     * Image URL of this model.
     */
    val imageUrl: String

    /**
     * Whether this is a model for speed cubing.
     */
    val isSpeedCube: Boolean

    /**
     * Whether this is a stickerless model.
     */
    val isStickerless: Boolean

    /**
     * Whether this is a stickerless model.
     */
    val isMagnetic: Boolean
}