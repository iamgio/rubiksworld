package rubiksworld.model

/**
 * The time spent to solve a Rubik's cube.
 */
data class SolveTime(val minutes: Int, val seconds: Int) : Comparable<SolveTime> {

    override fun toString() = buildString {
        if (minutes > 0) {
            append(minutes).append("m ")
        }
        append(seconds).append("s")
    }

    override fun compareTo(other: SolveTime): Int {
        return toSeconds() - other.toSeconds()
    }

    fun toSeconds(): Int = minutes * 60 + seconds

    companion object {
        fun fromSeconds(seconds: Int) = SolveTime(seconds / 60, seconds % 60)
    }
}