package com.github.gogetters.letsgo.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MoveTest {
    @Test
    fun moveEqualsComparesDeep() {
        val a = Move(Stone.BLACK, Point(1, 2))
        val b = Move(Stone.BLACK, Point(1, 2))
        val c = Move(Stone.WHITE, Point(1, 2))
        val d = Move(Stone.BLACK, Point(1, 1))

        assertTrue(a == b)
        assertTrue(a != c)
        assertTrue(a != d)
    }

    @Test
    fun moveHashcodeEqualToProductOfComponentHashcode() {
        for (stone in Stone.values()) {
            for (i in 1..5) {
                for (j in 1..5) {
                    val point = Point(i, j)
                    val move = Move(stone, point)
                    assertEquals(point.hashCode() * stone.hashCode(), move.hashCode())
                }
            }
        }
    }

    @Test
    fun moveEquivalentToGTP() {
        val columns = "abcdefghjklmnopqrst".toList()
        for (color in listOf(Stone.BLACK, Stone.WHITE)) {

            val colorString = if (color == Stone.BLACK) "black" else "white"

            for (i in 1..19) {
                for (j in 1..19) {
                    val point = Point(i, j)
                    val col = columns[i - 1]
                    val row = j.toString()

                    val m = Move(color, point)

                    assertEquals("$colorString $col$row", "$m")
                }
            }
        }
    }

    @Test
    fun toSGFSerializesCorrectly() {
        val columns = "abcdefghijklmnopqrst".toList()
        for (stone in setOf(Stone.BLACK, Stone.WHITE)) {
            for (col in 1..19) {
                for (row in 1..19) {
                    val move = Move(stone, Point(col, row))
                    val sgf = move.toSGF()
                    val realSGF = "${stone.toString()[0].toUpperCase()}" +
                            "[${columns[col - 1]}${columns[row - 1]}]"

                    assertEquals(realSGF, sgf)
                }
            }
        }
    }

    @Test
    fun fromSGFIsInverseOfToSGF() {
        val columns = "abcdefghijklmnopqrst".toList()
        for (stone in setOf(Stone.BLACK, Stone.WHITE)) {
            for (col in 1..19) {
                for (row in 1..19) {
                    val move = Move(stone, Point(col, row))
                    val sgf = move.toSGF()
                    assertEquals(move, Move.fromSGF(sgf))
                }
            }
        }
    }
}