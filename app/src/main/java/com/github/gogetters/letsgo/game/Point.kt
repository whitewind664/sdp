package com.github.gogetters.letsgo.game

data class Point(val first: Int, val second: Int) {
    private val offsetASCII = 96
    operator fun plus(other: Point) = Point(first + other.first, second + other.second)

    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            return other.first == first && other.second == second
        }
        return false
    }

    override fun hashCode(): Int {
        return 31 * first + second
    }

    /**
     * Respects the GTP vertex naming conventions
     */
    override fun toString(): String {
        val row = (first + offsetASCII).toChar().toString()
        val column = second.toString()
        return row + column
    }
}