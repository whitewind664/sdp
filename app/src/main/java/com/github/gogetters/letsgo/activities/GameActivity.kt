package com.github.gogetters.letsgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.github.gogetters.letsgo.game.view.GoView
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.Game
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
    }

    private lateinit var game: Game
    private lateinit var goView: GoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val boardSize = Board.Size.withSize(gameSizeInput)
        goView = GoView(this, boardSize)
        val inputDelegate = InputDelegate()
        goView.inputDelegate = inputDelegate


        val boardFrame = findViewById<FrameLayout>(R.id.gameBoardFrame)
        boardFrame.addView(goView)

        val whitePlayer = LocalPlayer(Stone.WHITE, inputDelegate)
        val blackPlayer = LocalPlayer(Stone.BLACK, inputDelegate)
        game = Game(boardSize, komi, whitePlayer, blackPlayer)

        GlobalScope.launch {
            var boardState = game.playTurn()
            while (!boardState.gameOver) {
                drawBoard(boardState)
                boardState = game.playTurn()
            }
        }
    }

    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }

}