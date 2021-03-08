package com.github.gogetters.letsgo.game

//TODO: refactor into a Board class with a __unique__ hashcode to check for histories


private typealias Point = Pair<Int, Int>
private typealias Board = HashMap<Point, Game.Stone>

internal class Game(private val size: Int, val komi: Double) {
    enum class Stone(val color: Int) {
        BLACK(-1), WHITE(1), EMPTY(0);

        companion object {
            fun otherColor(s: Stone): Stone {
                return if (s == BLACK) WHITE else BLACK
            }
        }
    }


    private class Move(val stone: Stone, val coord: Point)

    private val board = Board()

    init {
        for (i in 1..size) {
            for (j in 1..size) {
                board[Point(i, j)] = Stone.EMPTY
            }
        }
    }


    private fun isEmpty(c: Point): Boolean {
        return board[c] == Stone.EMPTY
    }

    operator fun Point.plus(other: Point) = Point(first + other.first, second + other.second)


    /**
     * Returns true if a coordinate is a valid coordinate (on the board) false otherwise
     */
    private fun insideBoard(c: Point): Boolean {
        return c.first in 1..size && c.second in 1..size
    }

    /**
     * Returns the neighbor coordinates of a given coordinate
     */
    private fun neighbors(c: Point): List<Point> {
        val neighbors = ArrayList<Point>()

        var d = Point(0, 1)
        for (i in 0..3) {
            val neighbor = c + d
            if (insideBoard(neighbor)) neighbors.add(neighbor)
            d = Point(d.second, -d.first)
        }

        return neighbors
    }


    /**
     * Returns the set of stones (Points) connected to a point
     * @param c: Point (can be empty) to check for
     * @return ArrayList of connected points
     */
    private fun getGroup(c: Point): List<Point> {
        fun connected(c: Point, existing: List<Point>, other: List<Point>) {

        }
        return ArrayList()
    }

    /**
     * Counts the number of liberties of the group at a given coordinate. If there is an empty
     * point at that coordinate then this function returns zero
     *
     * @param c Coordinate of any stone in group to count liberties of
     * @return number of liberties of group at coordinate
     */
    private fun countLiberties(group: List<Point>): Int {
        val liberties = HashSet<Point>()
        for (stone in group) {
            for (neighbor in neighbors(stone)) {
                if (isEmpty(neighbor)) liberties.add(neighbor)
            }
        }

        return liberties.size
    }

    /**
     * Checks if a move would result in suicide. Other player's stones are captured first.
     * @param m move that would be played (assumed to be on empty point)
     * @return true if m would result in self-capture, false otherwise
     */
    private fun isSuicide(m: Move): Boolean {
        if (isEmpty(m.coord)) {
            //Not sure if this is safe...
            board[m.coord] = m.stone
            val group = getGroup(m.coord)
            if (countLiberties(group) == 0) {
                for (neighbor in neighbors(m.coord)) {

                    // if the neighbor would be killed by the move
                    if (board[neighbor] == Stone.otherColor(m.stone)) {
                        if (countLiberties(getGroup(neighbor)) == 0) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    /**
     * Checks if a move would result in a Ko situation, ie repeated position
     */
    private fun isKo(m: Move):  Boolean {
        // need to keep track of game state..
        return false
    }

    /**
     * Full check to see if a move is playable (includes suicide and Ko)
     */
    private fun canPlay(m: Move): Boolean {
        return isEmpty(m.coord) && !isSuicide(m) && !isKo(m)
    }


    //TODO refactor into EAFP.... maybe exceptions for illegal moves?
    /**
     * Places a piece on the board if it is playable
     */
    private fun playMove(m: Move): Boolean {
        if (canPlay(m)) {
            board[m.coord] = m.stone
            //handle captures
            return true
        }
        return false
    }
}