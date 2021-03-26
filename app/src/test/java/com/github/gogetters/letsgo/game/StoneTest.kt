package com.github.gogetters.letsgo.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoneTest {
    @Test
    fun toStringEquivalentToGTP() {
        val white = Stone.WHITE
        val black = Stone.BLACK
        val empty = Stone.EMPTY

        assertEquals("white", "$white")
        assertEquals("black", "$black")
        assertEquals("pass", "$empty")

    }

    @Test
    fun otherColorReturnsDifferentForNonEmpty() {
        val white = Stone.WHITE
        val black = Stone.BLACK
        val empty = Stone.EMPTY

        assertEquals(black, white.otherColor())
        assertEquals(white, black.otherColor())
        assertEquals(empty, empty.otherColor())
    }
}