package com.github.gogetters.letsgo.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
}