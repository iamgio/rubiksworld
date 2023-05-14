package rubiksworld

import org.junit.jupiter.api.Test
import rubiksworld.controller.ControllerInitializer
import rubiksworld.controller.ModelsSearchFilters
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for database queries.
 */
class QueriesTests {

    private val controller = ControllerInitializer().initialize()

    init {
        controller.initDatabase()
    }

    @Test
    fun search() {
        val filters1 = ModelsSearchFilters(text = "qiy")
        val results1 = controller.searchModels(filters1)
        assertTrue { results1.all { it.maker == "QiYi" } }
        assertFalse { results1.all { it.isSpeedCube } }

        val filters2 = filters1.copy(onlySpeedCubes = true)
        val results2 = controller.searchModels(filters2)
        assertTrue { results2.all { it.maker == "QiYi" } }
        assertTrue { results2.all { it.isSpeedCube } }

        val filters3 = ModelsSearchFilters(onlyMagnetic = true)
        val results3 = controller.searchModels(filters3)
        assertTrue { results3.size > results1.size }
        assertTrue { results3.all { it.isMagnetic } }
    }

    @Test
    fun customizableParts() {
        val model = controller.searchModels(ModelsSearchFilters("RS3 M")).first()
        val parts = controller.getCustomizableParts(model)
        assertEquals(2, parts.size)

        val partNames = parts.map { it.part }
        assertContains(partNames, "Version")
        assertContains(partNames, "Lubrication")
    }

    @Test
    fun availableCustomizations() {
        val model = controller.searchModels(ModelsSearchFilters("356")).first()
        assertEquals("GAN", model.maker)

        val parts = controller.getCustomizableParts(model)
        assertEquals(1, parts.size)

        val customizations = controller.getAvailableCustomizations(parts.first())
        assertEquals(2, customizations.size)
        assertEquals("Lubrication", customizations.first().part)
        assertEquals(customizations[0].part, customizations[1].part)
    }
}