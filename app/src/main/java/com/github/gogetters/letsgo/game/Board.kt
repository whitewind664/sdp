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
     * Only one of these positions can exist each turn (I think??)
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

    private fun countLiberties(group: Iterable<Point>): Int {
        val liberties = HashSet<Point>()
        for (stone in group) {
            for (neighbor in getNeighbors(stone)) {
                if (isEmpty(neighbor)) liberties.add(neighbor)
            }
        }

        return liberties.size
    }

    private fun isSuicide(m: Move): Boolean {
        if (!insideBoard(m.coord)) throw OutOfBoardException()
        if (!isEmpty(m.coord)) throw NotEmptyException()

        val neighbors = getNeighbors(m.coord)
        for (neighbor in neighbors) {
            if (isEmpty(neighbor)) return false

            if (board[neighbor] == m.stone.otherColor()) {
                val enemyGroup = getGroup(neighbor)
                if (countLiberties(enemyGroup) == 1) {
                    return false
                }
            }
        }

        for (neighbor in neighbors) {
            if (board[neighbor] == m.stone) {
                val ourGroup = getGroup(neighbor)
                if (countLiberties(ourGroup) == 1) {
                    return true
                }
            }
        }
        return true

    }

    private fun isKo(m: Move): Boolean = m == koMove


    fun getView(lastMove: Move?, whiteScore: Int, blackScore: Int): BoardView =
            BoardView(HashMap(board), lastMove = lastMove, koMove = koMove,
                    whiteScore = whiteScore, blackScore = blackScore)

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