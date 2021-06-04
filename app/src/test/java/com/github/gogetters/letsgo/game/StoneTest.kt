package com.github.gogetters.letsgo.game

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class StoneTest {
    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

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

    @Test
    fun fromStringThrowsOnRandomString() {
        exception.expect(IllegalArgumentException::class.java)
        Stone.fromString("Hellooo")
    }

    @Test
    fun fromStringIdentifiesColors() {
        assertEquals(Stone.WHITE, Stone.fromString("white"))
        assertEquals(Stone.BLACK, Stone.fromString("black"))
    }

    @Test
    fun fromStringIdentifiesPass() {
        assertEquals(Stone.EMPTY, Stone.fromString("pass"))
    }
}