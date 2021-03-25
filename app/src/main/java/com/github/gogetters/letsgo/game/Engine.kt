package com.github.gogetters.letsgo.game

interface Engine {
    fun initialize()
    fun enemyPlay()
    fun requestMove(): Move
}