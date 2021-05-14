package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.*
import org.junit.Assert.assertEquals
import org.junit.Test

class TutorialPlayerTest {

    @Test
    fun playsTheMovesThatWereSet() {
        val boardState = BoardState(BoardState.emptyBoard(Board.Size.SMALL), null, 0, 0)
        val player = TutorialPlayer()
        val moves = listOf(Move(Stone.WHITE, Point(1, 1)), Move(Stone.WHITE, Point(2, 2)))
        val moveCoords = listOf(moves[0].coord, moves[1].coord)
        player.setMoves(moveCoords)
        assertEquals(moves[0], player.requestMove(boardState))
        assertEquals(moves[1], player.requestMove(boardState))
    }

    @Test
    fun playsSameMoveWhenAllMovesPlayed() {
        val boardState = BoardState(BoardState.emptyBoard(Board.Size.SMALL), null, 0, 0)
        val player = TutorialPlayer()
        val moves = listOf(Move(Stone.WHITE, Point(1, 1)), Move(Stone.BLACK, Point(2, 2)))
        val moveCoords = listOf(moves[0].coord, moves[1].coord)
        player.setMoves(moveCoords)
        player.requestMove(boardState)
        player.requestMove(boardState)
        val first = player.requestMove(boardState)
        val second = player.requestMove(boardState)
        assertEquals(first, second)
    }

    @Test
    fun playsSameMoveWhenSetMovesEmpty() {
        val boardState = BoardState(BoardState.emptyBoard(Board.Size.SMALL), null, 0, 0)
        val player = TutorialPlayer()
        player.setMoves(emptyList())
        val first = player.requestMove(boardState)
        val second = player.requestMove(boardState)
        assertEquals(first, second)
    }
}