package rubiksworld.controller.database.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import rubiksworld.model.Solve
import rubiksworld.model.SolveTime

/**
 * Solves DB table.
 */
object Solves : Table<Solve>("Solves") {

    val userNickname = varchar("user_nickname").primaryKey().bindTo { it.user.nickname }
    val registrationDate = datetime("registration_date").primaryKey().bindTo { it.registrationDate }
    val solveTime = int("solve_time").primaryKey()
        .transform({ SolveTime(it / 60, it % 60) }, { it.minutes * 60 + it.seconds })
        .bindTo { it.solveTime }
    val modelName = varchar("model_name").bindTo { it.model?.name }
    val modelMaker = varchar("model_maker").bindTo { it.model?.maker }
}