package rubiksworld.common

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @return this date formatted using the `dd/MM/yyyy` pattern
 */
fun LocalDate.defaultFormat(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

/**
 * @see defaultFormat
 */
fun LocalDateTime.defaultFormat() = toLocalDate().defaultFormat()