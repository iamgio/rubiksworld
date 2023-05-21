package rubiksworld.model

/**
 * The time spent to solve a Rubik's cube.
 */
data class SolveTime(val minutes: Int, val seconds: Int) {

    override fun toString() = buildString {
        if (minutes > 0) {
            append(minutes).append("m ")
        }
        append(seconds).append("s")
    }
}