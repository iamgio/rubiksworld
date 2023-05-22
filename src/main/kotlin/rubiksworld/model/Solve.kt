package rubiksworld.model

import org.ktorm.entity.Entity
import java.time.LocalDateTime

/**
 * A registered solve of the Rubik's cube.
 */
interface Solve : Entity<Solve> {

    companion object : Entity.Factory<Solve>()

    /**
     * User that registered the solve.
     */
    var user: User

    /**
     * Optional model used.
     */
    var model: Model?

    /**
     * Registration date of the solve.
     */
    var registrationDate: LocalDateTime?

    /**
     * Time spent.
     */
    var solveTime: SolveTime
}