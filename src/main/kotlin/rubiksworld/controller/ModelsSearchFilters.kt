package rubiksworld.controller

/**
 * Search filters for models.
 *
 * @param text part of name, maker or category
 * @param onlySpeedCubes whether only speed cubes should be searched
 * @param onlyStickerless whether only stickerless models should be searched
 * @param onlyMagnetic whether only magnetic models should be searched
 */
data class ModelsSearchFilters(
    val text: String = "",
    val onlySpeedCubes: Boolean = false,
    val onlyStickerless: Boolean = false,
    val onlyMagnetic: Boolean = false
)