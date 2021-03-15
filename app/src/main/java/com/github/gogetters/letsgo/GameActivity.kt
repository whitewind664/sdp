package com.github.gogetters.letsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.Game
import com.github.gogetters.letsgo.game.LocalPlayer
import com.github.gogetters.letsgo.game.Stone

class GameActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
    }

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val boardSize = Board.Size.withSize(gameSizeInput)

        drawBoard(boardSize)

        val whitePlayer = LocalPlayer(Stone.WHITE)
        val blackPlayer = LocalPlayer(Stone.BLACK)
        game = Game(boardSize, komi, whitePlayer, blackPlayer)

        //TODO(advance the game in a loop in a background thread)
        //TODO(maybe make a bean that links the GUI board to the game state returned here??)
    }

    private fun drawBoard(boardSize: Board.Size) {
        val boardImageView = findViewById<ImageView>(R.id.gameBoardImage)

        val boardImage = when (boardSize) {
            Board.Size.SMALL -> R.drawable.board_9
            Board.Size.MEDIUM -> R.drawable.board_13
            Board.Size.LARGE -> R.drawable.board_19
        }

        boardImageView.setImageResource(boardImage)
        val gameBoardDimension = boardImageView.width
    }

}