package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.view.GoView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameActivity : BaseActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
        const val EXTRA_PLAYER_WHITE = "com.github.gogetters.letsgo.game.PLAYER_ONE"
        const val EXTRA_PLAYER_BLACK = "com.github.gogetters.letsgo.game.PLAYER_TWO"
    }

    private lateinit var game: Game
    private lateinit var goView: GoView

    private lateinit var turnText: TextView
    private lateinit var blackScore: TextView
    private lateinit var whiteScore: TextView
    private lateinit var passButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val blackType = intent.getIntExtra(EXTRA_PLAYER_BLACK, 0)
        val whiteType = intent.getIntExtra(EXTRA_PLAYER_WHITE, 0)

        //TODO: unify "input providers???"
        val bluetoothService = BluetoothActivity.service
        bluetoothService.inputDelegate = InputDelegate()

        val boardSize = Board.Size.withSize(gameSizeInput)
        goView = GoView(this, boardSize)

        val touchInputDelegate = InputDelegate()
        goView.inputDelegate = touchInputDelegate


        val boardFrame = findViewById<FrameLayout>(R.id.game_frameLayout_boardFrame)
        boardFrame.addView(goView)
        turnText = findViewById(R.id.game_textView_turnIndication)
        blackScore = findViewById(R.id.game_textView_blackScore)
        whiteScore = findViewById(R.id.game_textView_whiteScore)
        passButton = findViewById(R.id.game_button_pass)

        val blackPlayer =
            Player.playerOf(Stone.BLACK, blackType, touchInputDelegate, bluetoothService)
        val whitePlayer =
            Player.playerOf(Stone.WHITE, whiteType, touchInputDelegate, bluetoothService)

        game = Game(boardSize, komi, whitePlayer, blackPlayer)

        GlobalScope.launch {
            var boardState = game.playTurn()
            while (!boardState.gameOver) {
                drawBoard(boardState)
                runOnUiThread {
                    updateTurnText(boardState.lastMove)
                    whiteScore.text = boardState.whiteScore.toString()
                    blackScore.text = boardState.blackScore.toString()
                }

                boardState = game.playTurn()
            }

            runOnUiThread {
                displayEndOfGame()
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game
    }


    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }

    private fun updateTurnText(lastMove: Move?) {
        if (lastMove?.stone == Stone.BLACK) {
            // White's turn
            turnText.text = resources.getString(R.string.game_whiteTurn)
        } else {
            // Black's turn
            turnText.text = resources.getString(R.string.game_blackTurn)
        }
    }

    private fun displayEndOfGame() {
        turnText.text = resources.getString(R.string.game_endOfGame)
        passButton.visibility = View.GONE
    }

}