package rubiksworld.common

import rubiksworld.model.Model

/**
 * @return a string representation for this model as maker + name, or [ifNull] if this is `null`
 */
fun Model?.asString(ifNull: String = "") = this?.let { "${it.maker} ${it.name}" } ?: ifNull