package rubiksworld.model

/**
 * A model that is available in the shop.
 *
 * @param name model name
 * @param maker model maker
 * @param category category this model belongs to
 * @param price model price
 * @param discountPercentage if not `null`, discount percentage on this model's final price
 * @param imageUrl image URL of this model
 * @param isSpeedCube whether this is a model for speed cubing
 * @param isStickerless whether this is a stickerless model
 * @param isMagnetic whether this is a magnetic model
 */
data class Model(
    val name: String,
    val maker: String,
    val category: Category,
    val price: Double,
    val discountPercentage: Double?,
    val imageUrl: String,
    val isSpeedCube: Boolean,
    val isStickerless: Boolean,
    val isMagnetic: Boolean
)