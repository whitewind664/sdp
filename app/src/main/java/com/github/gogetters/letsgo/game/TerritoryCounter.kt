package com.github.gogetters.letsgo.game

abstract class TerritoryCounter(boardState: BoardState) {

    abstract fun countTerritory(): Int
}