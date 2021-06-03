package com.github.gogetters.letsgo.game

import androidx.test.uiautomator.UiObjectNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class GTPCommandTest {

        @get:Rule
        val exception: ExpectedException = ExpectedException.none()

        @Test
        fun boardSizeParsedCorrectly() {

                val smallBoard = GTPCommand.BOARD_SIZE(Board.Size.SMALL)
                val gtpString1 = smallBoard.toString()
                val parsedSmallBoard = GTPCommand.toCommand(gtpString1)

                if(parsedSmallBoard is GTPCommand.BOARD_SIZE)
                        assertEquals(smallBoard.boardSize, parsedSmallBoard.boardSize)
                else
                        throw Error()

                val mediumBoard = GTPCommand.BOARD_SIZE(Board.Size.MEDIUM)
                val gtpString2 = mediumBoard.toString()
                val parsedMediumBoard = GTPCommand.toCommand(gtpString2)

                if(parsedMediumBoard is GTPCommand.BOARD_SIZE)
                        assertEquals(mediumBoard.boardSize, parsedMediumBoard.boardSize)
                else
                        throw Error()

                val largeBoard = GTPCommand.BOARD_SIZE(Board.Size.LARGE)
                val gtpString3 = largeBoard.toString()
                val parsedLargeBoard = GTPCommand.toCommand(gtpString3)

                if(parsedLargeBoard is GTPCommand.BOARD_SIZE)
                        assertEquals(largeBoard.boardSize, parsedLargeBoard.boardSize)
                else
                        throw Error()
        }

        @Test
        fun clearBoardParsesCorrectly(){
                val clearBoard = GTPCommand.CLEAR_BOARD
                val gtpString = clearBoard.toString()
                val parsedClearBoard = GTPCommand.toCommand(gtpString)


                if(parsedClearBoard is GTPCommand.CLEAR_BOARD)
                        assertEquals(parsedClearBoard, clearBoard)
                else
                        throw  Error()
        }

        @Test
        fun komiParsesCorrectly(){
                val komi = GTPCommand.KOMI(3f)
                val gtpString = komi.toString()
                val parsedKomi = GTPCommand.toCommand(gtpString)

                if(parsedKomi is GTPCommand.KOMI)
                        assertEquals(komi.new_komi, parsedKomi.new_komi)
                else
                        throw  Error()

        }

        @Test
        fun fixedHandicapParsesCorrectly(){
                val raw = GTPCommand.FIXED_HANDICAP(38)
                val gtpString = raw.toString()
                val parsed = GTPCommand.toCommand(gtpString)

                if(parsed is GTPCommand.FIXED_HANDICAP)
                        assertEquals(parsed.numberOfStones, raw.numberOfStones)
                else
                        throw  Error()

        }


        @Test
        fun undoParsesCorrectly(){
                val raw = GTPCommand.UNDO
                val gtpString = raw.toString()
                val parsed = GTPCommand.toCommand(gtpString)

                if(parsed is GTPCommand.UNDO)
                        assertEquals(parsed, raw)
                else
                        throw  Error()

        }

        @Test
        fun parsesMoveCorrectly() {
                val stone = Stone.BLACK
                val point = Point(2, 2)
                val moveArgs = listOf<String>(stone.toString(), point.toString())
                val move = GTPCommand.parseMove(moveArgs)
                assertEquals(stone, move.stone)
                assertEquals(point, move.point)
        }

        @Test
        fun parseMoveThrowsOnEmptyArgList() {
                exception.expect(IllegalStateException::class.java)
                GTPCommand.parseMove(emptyList())
        }

}