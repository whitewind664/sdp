package com.github.gogetters.letsgo.game

abstract class TerritoryCounter(boardView: BoardView) {

    abstract fun countTerritory(): Int
}