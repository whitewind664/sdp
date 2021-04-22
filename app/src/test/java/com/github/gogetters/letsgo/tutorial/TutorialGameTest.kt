package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.util.InputDelegate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TutorialGameTest {
    @Test
    fun defaultStepContainsNoStonesOnBoard() {
        val inputDelegate = InputDelegate()
        val player = TutorialLocalPlayer(inputDelegate)
        val game = TutorialGame(player)

        val (step, boardState) = game.nextStep()
        assertTrue(step.playedStones.isEmpty())
    }
}