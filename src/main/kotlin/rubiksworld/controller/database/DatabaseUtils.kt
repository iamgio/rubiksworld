package rubiksworld.controller.database

import org.ktorm.expression.FunctionExpression
import org.ktorm.expression.ScalarExpression
import org.ktorm.schema.VarcharSqlType

/**
 * SQL `CONCAT` function
 */
fun concat(vararg expressions: ScalarExpression<String>): FunctionExpression<String> {
    return FunctionExpression(
        functionName = "concat",
        arguments = expressions.toList(),
        sqlType = VarcharSqlType
    )
}