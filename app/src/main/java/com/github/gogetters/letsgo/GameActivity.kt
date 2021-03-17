package com.github.gogetters.letsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.Game

class GameActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
    }

    private lateinit var game: Game

    private val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
    private val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
    private val boardSize = Board.Size.withSize(gameSizeInput)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val whitePlayer = LocalPlayer(Stone.WHITE)
        val blackPlayer = LocalPlayer(Stone.BLACK)
        game = Game(boardSize, komi, whitePlayer, blackPlayer)
        var boardState = game.playTurn()

        while (!boardState.gameOver) {
            drawBoard(boardState)
            boardState = game.playTurn()
        }
    }

    private fun drawBoard(boardState: BoardState) {
        val boardImageView = findViewById<ImageView>(R.id.gameBoardImage)


        val boardImage = when (boardSize) {
            Board.Size.SMALL -> R.drawable.board_9
            Board.Size.MEDIUM -> R.drawable.board_13
            Board.Size.LARGE -> R.drawable.board_19
        }

        boardImageView.setImageResource(boardImage)


    }

}