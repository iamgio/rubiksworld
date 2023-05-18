package rubiksworld.controller.database

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import rubiksworld.controller.database.tables.*

val Database.users get() = this.sequenceOf(Users)
val Database.categories get() = this.sequenceOf(Categories)
val Database.models get() = this.sequenceOf(Models)
val Database.customizableParts get() = this.sequenceOf(CustomizableParts)
val Database.customizations get() = this.sequenceOf(Customizations)
val Database.modelVersions get() = this.sequenceOf(ModelVersions)
val Database.cartPresences get() = this.sequenceOf(CartPresences)
val Database.wishlistPresences get() = this.sequenceOf(WishlistPresences)
val Database.applications get() = this.sequenceOf(Applications)
val Database.coupons get() = this.sequenceOf(Coupons)