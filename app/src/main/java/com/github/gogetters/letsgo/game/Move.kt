package com.github.gogetters.letsgo.game

data class Move(val stone: Stone, val point: Point) {
    override fun equals(other: Any?): Boolean {
        if (other is Move) {
            return other.stone == stone && other.point == point
        }

        return false
    }

    override fun hashCode(): Int {
        return stone.hashCode() * point.hashCode()
    }

    override fun toString(): String {
        return "$stone $point"
    }
}
