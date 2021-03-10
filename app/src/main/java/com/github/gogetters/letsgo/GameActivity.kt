package com.github.gogetters.letsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.Game
import com.github.gogetters.letsgo.game.LocalPlayer

class GameActivity : AppCompatActivity() {
    val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
    val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameSize = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val whitePlayer = LocalPlayer(Board.Stone.WHITE)
        val blackPlayer = LocalPlayer(Board.Stone.BLACK)
        game = Game(gameSize, komi, whitePlayer, blackPlayer)

    }
}