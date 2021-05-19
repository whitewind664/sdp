package com.github.gogetters.letsgo.game

private const val offsetASCII = 96
private const val skippedColumn = 9

data class Point(val first: Int, val second: Int) {
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
        val firstSkip = if (first < skippedColumn) first else first + 1
        val col = (firstSkip + offsetASCII).toChar().toString()
        val row = second.toString()
        return col + row
    }

    companion object {
        fun fromString(string: String): Point {
            if (string.length != 2) {
                throw IllegalArgumentException("Incorrectly formatted string")
            }

            val col = string[0]
            val row = string[1]
            val colNumMaybeSkip = (col.toInt() - offsetASCII)
            val colNum = if (colNumMaybeSkip > skippedColumn) colNumMaybeSkip - 1 else colNumMaybeSkip
            val rowNum = row.toString().toInt()

            return Point(colNum, rowNum)
        }
    }
}