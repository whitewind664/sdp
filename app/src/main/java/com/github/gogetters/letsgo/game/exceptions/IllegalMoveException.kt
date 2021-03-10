package com.github.gogetters.letsgo.game.exceptions

open class IllegalMoveException(message: String): Throwable(message)
class KoException: IllegalMoveException("Illegal Ko move: cannot repeat previous position")
class SuicideException: IllegalMoveException("Illegal suicide: cannot kill own group")
class NotEmptyException: IllegalMoveException("Illegal move: cannot place stone on top of another stone")
class OutOfBoardException: IllegalMoveException("Illegal move: cannot place stone outside of board")