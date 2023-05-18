package rubiksworld.view.common

import javafx.scene.image.Image
import rubiksworld.controller.Controller

/**
 * A runtime cache for images.
 */
object ImageCache {

    private val cache = hashMapOf<String, Image>()

    /**
     * @return the cached image from [url], that is loaded if it is in cache
     */
    operator fun get(url: String): Image {
        return synchronized(this) {
            cache.computeIfAbsent(url) { Image(it) }
        }
    }

    /**
     * @return whether the image from [url] is already cached
     */
    operator fun contains(url: String) = url in cache

    /**
     * Gets an image from cache, or fetches it from an [url] if it is not cached.
     * @param url image url
     * @param controller global controller
     * @param onLoadingStart action to run before a non-cached image starts loading
     * @param onLoadingEnd action to run after an image was retrieved, with (image, cached) as arguments
     */
    fun fetch(url: String, controller: Controller, onLoadingStart: () -> Unit, onLoadingEnd: (Image, Boolean) -> Unit) {
        if (url in this) {
            onLoadingEnd(this[url], true)
        } else {
            onLoadingStart()
            controller.async {
                val image = this@ImageCache[url]
                controller.sync {
                    onLoadingEnd(image, false)
                }
            }
        }
    }
}