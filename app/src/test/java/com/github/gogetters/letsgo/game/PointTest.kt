package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class PointTest {
    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun equalsComparesDeep() {
        val a = Point(1, 2)
        val b = Point(1, 2)
        val c = Point(2, 3)
        assertTrue(a == b)
        assertTrue(a != c)
        assertTrue(b != c)
    }

    @Test
    fun equalsFailsWithOtherObject() {
        val a = Point(1, 2)
        val move = Move(Stone.BLACK, a)
        assertFalse(a.equals(move))
    }

    @Test
    fun sumOfPointsHasSummedComponents() {
        val a = Point(1, 2)
        val b = Point(3, 4)

        val sum = a + b
        assertTrue(Point(4, 6) == sum)
    }

    @Test
    fun hashcodeEqualTo31TimesFirstPlusSecond() {
        for (i in 1..10) {
            for (j in 1..10) {
                assertEquals(31 * i + j, Point(i, j).hashCode())
            }
        }
    }

    @Test
    fun toStringEquivalentToGTP() {
        //i is skipped on purpose due to the GTP protocol
        val columns = "abcdefghjklmnopqrst".toList()
        for (i in 1..19) {
            for (j in 1..19) {
                val point = Point(i, j)
                val col = columns[i - 1]
                val row = j.toString()
                assertEquals("$col$row", "$point")
            }
        }
    }

    @Test
    fun toSGFSerializesCorrectly() {
        val columns = "abcdefghijklmnopqrstuvwxyz".toList()
        for (i in 1..columns.size) {
            for (j in 1..columns.size) {
                val point = Point(i, j)
                val col = columns[i - 1]
                val row = columns[j - 1]
                assertEquals("$col$row", point.toSGF())
            }
        }
    }

    @Test
    fun fromSGFIsInverseOfToSGF() {
        for (i in 1..19) {
            for (j in 1..19) {
                val point = Point(i, j)
                assertEquals(point, Point.fromSGF(point.toSGF()))
            }
        }
    }
    
    @Test
    fun fromStringFailsOnWrongArgLeng() {
        exception.expect(IllegalArgumentException::class.java)
        Point.fromString("SomeRandomString")
    }

    @Test
    fun fromStringToStringGivesSameElement() {
        val pointString = "a1"
        assertEquals(pointString, Point.fromString(pointString).toString())
    }

}