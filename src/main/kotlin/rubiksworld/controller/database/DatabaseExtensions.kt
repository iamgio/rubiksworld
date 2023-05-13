package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import rubiksworld.controller.database.tables.Categories
import rubiksworld.controller.database.tables.CustomizableParts
import rubiksworld.controller.database.tables.Models

val Database.categories get() = this.sequenceOf(Categories)
val Database.models get() = this.sequenceOf(Models)
val Database.customizableParts get() = this.sequenceOf(CustomizableParts)