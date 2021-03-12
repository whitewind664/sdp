package com.github.gogetters.letsgo.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.game.exceptions.KoException
import com.github.gogetters.letsgo.game.exceptions.NotEmptyException
import com.github.gogetters.letsgo.game.exceptions.OutOfBoardException
import com.github.gogetters.letsgo.game.exceptions.SuicideException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

import com.github.gogetters.letsgo.game.Board.*

@RunWith(AndroidJUnit4::class)
class BoardTest {

    @Test
    fun sizeIsOneOfAllowedSizes() {
        val board = Board(Size.withSize(2))
        assertTrue(board.size == 9 || board.size == 13 || board.size == 19)
    }

    @Test
    fun otherColorOnlyReturnsEmptyForEmpty() {
        val blackStone = Stone.BLACK
        val whiteStone = Stone.WHITE
        assertEquals(Stone.WHITE, blackStone.otherColor())
        assertEquals(Stone.BLACK, whiteStone.otherColor())
        assertEquals(Stone.EMPTY, Stone.EMPTY.otherColor())
    }

    @Test
    fun sizeOfWithSizeIsEqualToInputForValidInput() {
        val validSizes = listOf(9, 13, 19)
        for (size in validSizes) {
            val board = Board(Size.withSize(size))
            assertEquals(size, board.size)
        }

        val board = Board(Size.withSize(1))
        assertEquals(9, board.size)

    }

    @Test(expected = OutOfBoardException::class)
    fun playMoveThrowsForStonePlacedOutsideBoard() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(15, 15)))
    }


    @Test(expected = SuicideException::class)
    fun isSuicideToPlayInMiddleOfEnemyStones() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 2)))
        board.playMove(Move(Stone.BLACK, Point(4, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))
        board.playMove(Move(Stone.BLACK, Point(2, 3)))

        board.playMove(Move(Stone.WHITE, Point(3, 3)))
    }

    @Test
    fun isNotSuicideToPlayOnEmptyBoard() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 2)))
    }

    @Test(expected = KoException::class)
    fun isKoWhenTryingToRecaptureDirectly() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 2)))
        board.playMove(Move(Stone.BLACK, Point(2, 3)))
        board.playMove(Move(Stone.BLACK, Point(4, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 5)))
        board.playMove(Move(Stone.WHITE, Point(4, 4)))
        board.playMove(Move(Stone.WHITE, Point(2, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))
    }


    @Test(expected = KoException::class)
    fun gettingViewDoesNotChangeBoard() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 2)))
        board.playMove(Move(Stone.BLACK, Point(2, 3)))
        board.playMove(Move(Stone.BLACK, Point(4, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 5)))
        board.playMove(Move(Stone.WHITE, Point(4, 4)))
        board.playMove(Move(Stone.WHITE, Point(2, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 3)))

        board.getView(0, 0)
        board.playMove(Move(Stone.BLACK, Point(3, 4)))
    }

    @Test(expected = NotEmptyException::class)
    fun cantPlayOnExistingStone() {
        val board = Board(Size.withSize(1))
        board.playMove(Stone.BLACK, Point(3, 3))
        board.playMove(Stone.WHITE, Point(3, 3))
    }

    @Test(expected = OutOfBoardException::class)
    fun cantPlayOutsideBoard() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(15, 15)))
    }

    @Test
    fun passingClearsKoMove() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 2)))
        board.playMove(Move(Stone.BLACK, Point(2, 3)))
        board.playMove(Move(Stone.BLACK, Point(4, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 5)))
        board.playMove(Move(Stone.WHITE, Point(4, 4)))
        board.playMove(Move(Stone.WHITE, Point(2, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 3)))
        board.playMove(Move(Stone.EMPTY, Point(0, 0)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))
    }

    @Test
    fun captureClearsEntireGroupStones() {
        val board = Board(Size.withSize(1))
        board.playMove(Move(Stone.BLACK, Point(3, 3)))
        board.playMove(Move(Stone.BLACK, Point(3, 4)))

        board.playMove(Move(Stone.WHITE, Point(3, 2)))
        board.playMove(Move(Stone.WHITE, Point(3, 5)))
        board.playMove(Move(Stone.WHITE, Point(4, 4)))
        board.playMove(Move(Stone.WHITE, Point(2, 4)))

    }

    @Test
    fun stoneInCornerHasOnlyTwoLiberties() {
        val board = Board(Size.withSize(1))
        board.playMove(Stone.BLACK, Point(1, 1))

        board.playMove(Stone.WHITE, Point(1, 2))
        board.playMove(Stone.WHITE, Point(2, 1))

        board.playMove(Stone.EMPTY, Point(0, 0))

        board.playMove(Stone.WHITE, Point(1, 1))
    }

    @Test
    fun stoneOnBorderHasOnlyThreeLiberties() {
        val board = Board(Size.withSize(1))
        board.playMove(Stone.BLACK, Point(1, 2))

        board.playMove(Stone.WHITE, Point(1, 1))
        board.playMove(Stone.WHITE, Point(1, 3))
        board.playMove(Stone.WHITE, Point(2, 2))

        board.playMove(Stone.EMPTY, Point(0, 0))
        board.playMove(Stone.WHITE, Point(1, 2))
    }
}