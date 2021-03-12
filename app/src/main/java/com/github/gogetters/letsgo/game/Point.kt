package com.github.gogetters.letsgo.game

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
}

