package rubiksworld.model

import org.ktorm.entity.Entity

/**
 * The presence of a [ModelVersion] within an order.
 */
interface OrderPresence : Entity<OrderPresence> {

    companion object : Entity.Factory<OrderPresence>()

    /**
     * Order the model version is in.
     */
    var order: Order

    /**
     * Customized [Model] version.
     */
    var modelVersion: ModelVersion
}