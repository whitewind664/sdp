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

    /**
     * Respects GTP
     */
    override fun toString(): String {
        return "$stone $point"
    }

    /**
     * Respects SGF
     */
    fun toSGF(): String {
        val color = stone.toString()[0].toUpperCase()
        return "$color[${point.toSGF()}]"
    }

    companion object {
        fun fromSGF(sgf: String): Move {
            if (sgf.length != 5) throw IllegalArgumentException("sgf must be 5 characters long, eg B[cg]")

            val stone = when(sgf[0]) {
                'B' -> Stone.BLACK
                'W' -> Stone.WHITE
                else -> throw IllegalArgumentException("could not parse sgf, stone color incorrect")
            }

            val point = Point.fromSGF(sgf.substring(2,4))

            return Move(stone, point)
        }
    }
}
