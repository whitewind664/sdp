package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.Game

/**
 * A game that is used in the tutorial and that helps the given local player to learn to play Go
 */
internal class TutorialGame(localPlayer: TutorialLocalPlayer): Game(Board.Size.SMALL, 0.0, localPlayer, TutorialPlayer(Stone.WHITE)) {
    private val DEFAULT_TURN = TutorialStep(-1, true, false, emptyList())
    private var turnCount: Int = 0
    private var tutorialSteps: List<TutorialStep> = emptyList()

    init {
        // create all the steps of the tutorial
        tutorialSteps += TutorialStep(2, false, true, emptyList())
        // TODO
    }

    override fun playTurn(): BoardState {
        return super.playTurn()
    }


    private fun currentStep(): TutorialStep {
        var currentStep = DEFAULT_TURN
        for (step in tutorialSteps) {
            if (step.turnNumber == turnCount)
                currentStep = step
        }
        return currentStep
    }

    /**
     * Represents a step in the tutorial that can display text, the board (with some recommended moves)
     */
    class TutorialStep(val turnNumber: Int, val displayText: Boolean,  val displayBoard: Boolean, val recommendedMoves: List<Move>) {
        init {
            assert(displayText || displayBoard)
        }
    }
}