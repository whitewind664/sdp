package com.github.gogetters.letsgo.game

import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.util.BluetoothGTPService
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class PlayerTest {

    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun ofPlayerReturnsCorrectPlayerColor() {
        val inputDelegate = InputDelegate()
        val btService = BluetoothGTPService()
        btService.inputDelegate = inputDelegate
        val p1 = Player.playerOf(Stone.BLACK, Player.PlayerTypes.LOCAL.ordinal, inputDelegate, btService)
        val p2 = Player.playerOf(Stone.BLACK, Player.PlayerTypes.BTLOCAL.ordinal, inputDelegate, btService)
        val p3 = Player.playerOf(Stone.BLACK, Player.PlayerTypes.BTREMOTE.ordinal, inputDelegate, btService)
        assertEquals(Stone.BLACK, p1.color)
        assertEquals(Stone.BLACK, p2.color)
        assertEquals(Stone.BLACK, p3.color)
    }

    @Test
    fun ofPlayerThrowsOnInvalidType() {
        exception.expect(IllegalArgumentException::class.java)
        Player.playerOf(Stone.BLACK, 323434, InputDelegate(), BluetoothGTPService())
    }
}