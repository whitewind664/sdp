package com.github.gogetters.letsgo.game

class Katago: Engine {
    private lateinit var gameEngine: Any

    init {
        System.loadLibrary("libkatago.so")
    }

    override fun initialize() {
        gameEngine =
        TODO("Not yet implemented")
    }

    override fun enemyPlay() {
        TODO("Not yet implemented")
    }

    override fun requestMove(): Move {
        TODO("Not yet implemented")
    }

}