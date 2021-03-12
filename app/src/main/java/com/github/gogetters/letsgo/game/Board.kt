package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.exceptions.KoException
import com.github.gogetters.letsgo.game.exceptions.NotEmptyException
import com.github.gogetters.letsgo.game.exceptions.OutOfBoardException
import com.github.gogetters.letsgo.game.exceptions.SuicideException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Board(private val boardSize: Size) {

    val size
    get() = boardSize.size

    enum class Size(val size: Int) {
        SMALL(9), MEDIUM(13), LARGE(19);

        companion object {
            fun withSize(size: Int): Size {
                return when (size) {
                    19 -> LARGE
                    13 -> MEDIUM
                    else -> SMALL
                }
            }
        }
    }

    /**
     * Requirements for Ko to be possible when stone X is placed:
     *  - Stone X has captured a single stone Y
     *  - Stone X is left with only 1 liberty
     *  - Stone X is its own group of size 1
     * Then Ko will occur if we play where stone Y was
     *
     * Only one of these positions can exist each turn
     * We keep track of this position in the koPoint variable
     */
    private var koMove: Move? = null
    private val board = HashMap<Point, Stone>()

    init {
        for (i in 1..size) {
            for (j in 1..size) {
                board[Point(i, j)] = Stone.EMPTY
            }
        }
    }

    private fun insideBoard(c: Point): Boolean {
        return c.first in 1..size && c.second in 1..size
    }

    private fun isEmpty(c: Point): Boolean {
        if (!insideBoard(c)) throw OutOfBoardException()
        val temp = board[c]
        return temp == Stone.EMPTY
    }


    /**
     * Retrieves the neighbors of a point. Points diagonally away do not count as neighbors.
     * The neighbors are restricted to points on the board.
     *
     * @param c point to get the neighbors of
     * @return list of neighboring points
     */
    private fun getNeighbors(c: Point): List<Point> {
        if (!insideBoard(c)) throw OutOfBoardException()

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
     * Retrieves the "group" connected to a point. A group is defined as a set of connected points
     * that are of the same type (e.g. all white stones). Connected empty points can also form a
     * group.
     *
     * @param c Point that should be one of the members of the group
     * @return Set of points corresponding to the group
     * @throws OutOfBoardException if the point given is outside of the board
     */
    private fun getGroup(c: Point): Set<Point> {
        if (!insideBoard(c)) throw OutOfBoardException()
        val groupColor = board[c]
        val considering = LinkedList<Point>()
        val connected = HashSet<Point>()
        considering.add(c)

        while (considering.size != 0) {
            val head = considering.removeFirst()
            for (neighbor in getNeighbors(head)) {
                val sameColor = board[neighbor] == groupColor
                if (neighbor !in considering && neighbor !in connected && sameColor) {
                    considering.add(neighbor)
                }
            }

            connected.add(head)
        }

        return connected
    }


    /**
     * Counts the number of liberties of a given group. A liberty is defined as an empty point that
     * is connected to the group. This function therefore returns the number of empty points that
     * are connected to any member of a group of points.
     *
     * @param group iterable representing the group of points
     * @return the number of liberties of "group"
     */
    private fun countLiberties(group: Iterable<Point>): Int {
        val liberties = HashSet<Point>()
        for (stone in group) {
            for (neighbor in getNeighbors(stone)) {
                if (isEmpty(neighbor)) liberties.add(neighbor)
            }
        }

        return liberties.size
    }


    /**
     * Checks if playing a move would result in suicide (illegal move). Suicide happens if a player
     * reduces the number of liberties of a group to zero (*after* capture).
     *
     * @param m move that could potentially result in suicide
     * @returns true if move would be suicide, false otherwise
     * @throws OutOfBoardException if the point given is outside the board
     * @throws NotEmptyException if the point given is already occupied by a stone
     *
     */
    private fun isSuicide(m: Move): Boolean {
        if (!insideBoard(m.coord)) throw OutOfBoardException()
        if (!isEmpty(m.coord)) throw NotEmptyException()

        val neighbors = getNeighbors(m.coord)
        //CHECK IF WE HAVE AIR TO BREATHE, OR IF WE KILLED AN ENEMY GROUP
        for (neighbor in neighbors) {
            if (isEmpty(neighbor)) return false

            if (board[neighbor] == m.stone.otherColor()) {
                val enemyGroup = getGroup(neighbor)
                if (countLiberties(enemyGroup) == 1) {
                    return false
                }
            }
        }

        //OTHERWISE CHECK IF WE ARE CONNECTED TO A GROUP WITH MORE THAN ONE LIBERTY
        for (neighbor in neighbors) {
            if (board[neighbor] == m.stone) {
                val ourGroup = getGroup(neighbor)
                if (countLiberties(ourGroup) > 1) {
                    return false
                }
            }
        }
        return true

    }

    /**
     * Checks if a move would break the Ko rule. There can only be at most one of these moves per
     * turn.
     *
     * @param m move that might break the Ko rule
     * @return true if playing m would break the Ko rule, false otherwise
     */
    private fun isKo(m: Move): Boolean = m == koMove


    /**
     * Returns a data object that represents the current board state
     */
    fun getView(whiteScore: Int, blackScore: Int): BoardView =
            BoardView(HashMap(board), koMove = koMove,
                    whiteScore = whiteScore, blackScore = blackScore)


    /**
     * Executes a given move, throwing an exception if the move is illegal. Returns the number of
     * stones captured by the move.
     *
     * @param m move to play
     * @return number of stones captured by playing m
     * @throws OutOfBoardException if the move places a stone outside the board
     * @throws NotEmptyException if the move places a stone on top of another stone
     * @throws SuicideException if the move would result in suicide
     * @throws KoException if the move would break the Ko rule
     */
    fun playMove(m: Move): Int {
        val capturedStones = HashSet<Point>()

        if (m.stone != Stone.EMPTY) {
            if (!insideBoard(m.coord)) throw OutOfBoardException()
            if (!isEmpty(m.coord)) throw NotEmptyException()
            if (isSuicide(m)) throw SuicideException()
            if (isKo(m)) throw KoException()

            board[m.coord] = m.stone
            for (neighbor in getNeighbors(m.coord)) {
                if (board[neighbor] == m.stone.otherColor()) {

                    val enemyGroup = getGroup(neighbor)
                    if (countLiberties(enemyGroup) == 0) {
                        for (enemy in enemyGroup) {
                            board[enemy] = Stone.EMPTY
                            capturedStones.add(enemy)
                        }
                    }
                }
            }

            koMove =
                    if (capturedStones.size == 1)
                        Move(m.stone.otherColor(), capturedStones.random())
                    else null

        } else {
            if (insideBoard(m.coord)) {
                board[m.coord] = m.stone
            }
            koMove = null
        }

        return capturedStones.size
    }

    fun playMove(s: Stone, p: Point) = playMove(Move(s, p))
}