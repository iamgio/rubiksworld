package rubiksworld.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @return this datetime formatted using the `dd/MM/yyyy` pattern
 */
fun LocalDateTime.defaultFormat(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))