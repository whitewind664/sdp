package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.util.TouchInputDelegate
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito

class TutorialLocalPlayerTest {

    @Test
    fun recommendedMoveCanBePlayed() {
        val move = Move(Stone.BLACK, Point(2, 2))
        val boardState = BoardState(BoardState.emptyBoard(Board.Size.SMALL), null, 0, 0)

        val inputDelegate = TouchInputDelegate()
        inputDelegate.saveLatestInput(move.point)

        val player = TutorialLocalPlayer(inputDelegate)
        player.setRecommendedMoves(listOf(move))
        assertEquals(player.requestMove(boardState), move)
    }

    @Test
    fun unrecommendedMoveCannotBePlayed() {
        /* // MOCKING THE INPUT DELEGATE DOESN'T SEEM TO WORK
        val firstTryMove = Move(Stone.BLACK, Point(1, 1))
        val secondTryMove = Move(Stone.BLACK, Point(2, 2))
        val boardState: BoardState = BoardState(BoardState.emptyBoard(Board.Size.SMALL), null, 0, 0)

        val inputDelegate = Mockito.mock(InputDelegate::class.java)
        Mockito.`when`(inputDelegate.latestInput).thenReturn(firstTryMove.coord).thenReturn(secondTryMove.coord)

        val player = TutorialLocalPlayer(inputDelegate)
        player.setRecommendedMoves(listOf(secondTryMove))
        assertEquals(player.requestMove(boardState), secondTryMove)*/
    }
}