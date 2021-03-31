package com.github.gogetters.letsgo.game

import org.junit.Assert.assertEquals
import org.junit.Test

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