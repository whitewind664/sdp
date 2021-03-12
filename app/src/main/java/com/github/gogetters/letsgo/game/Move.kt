package com.github.gogetters.letsgo.game

data class Move(val stone: Stone, val coord: Point) {
    override fun equals(other: Any?): Boolean {
        if (other is Move) {
            return other.stone == stone && other.coord == coord
        }

        return false
    }

    override fun hashCode(): Int {
        return stone.hashCode() * coord.hashCode()
    }
}
