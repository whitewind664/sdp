package com.github.gogetters.letsgo.activities

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.view.GoView
import com.github.gogetters.letsgo.util.PermissionUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameActivity : BaseActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
        const val EXTRA_PLAYER_WHITE = "com.github.gogetters.letsgo.game.PLAYER_ONE"
        const val EXTRA_PLAYER_BLACK = "com.github.gogetters.letsgo.game.PLAYER_TWO"
        
        const val CONFIRM_PASS_IDX = 0
        const val CANCEL_PASS_IDX = 1
    }

    private lateinit var game: Game
    private lateinit var goView: GoView

    private lateinit var turnText: TextView
    private lateinit var blackScore: TextView
    private lateinit var whiteScore: TextView
    private lateinit var passButton: Button

    private lateinit var blackTurnText: String
    private lateinit var whiteTurnText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val blackType = intent.getIntExtra(EXTRA_PLAYER_BLACK, 0)
        val whiteType = intent.getIntExtra(EXTRA_PLAYER_WHITE, 0)
        setInfoAboutThisPlayers(blackType, whiteType)

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
        passButton.setOnClickListener {
            passPopUp(touchInputDelegate)
        }

        val blackPlayer =
            Player.playerOf(Stone.BLACK, blackType, touchInputDelegate, bluetoothService)
        val whitePlayer =
            Player.playerOf(Stone.WHITE, whiteType, touchInputDelegate, bluetoothService)

        game = Game(boardSize, komi, whitePlayer, blackPlayer)
        runGame()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game
    }

    private fun runGame() {
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

    /**
     * Is called when the pass button is clicked. Asks whether the player really wants to pass and then does the passing
     */
    private fun passPopUp(inputDelegate: InputDelegate) {
        val dialogTexts = arrayOf<CharSequence>(
            resources.getString(R.string.game_passConfirm),
            resources.getString(R.string.game_passCancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.game_passTitle))
        builder.setItems(dialogTexts, DialogInterface.OnClickListener { dialog, clickedIndex ->
            if (clickedIndex == CONFIRM_PASS_IDX) {
                inputDelegate.saveLatestInput(Game.PASS_MOVE.point)
            } else {
                dialog.dismiss()
                return@OnClickListener
            }
        })
        builder.show()
    }
    
    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }

    /**
     * Based on the types of the players, pops up your color and
     * sets the turn text messages accordingly (so that you always know if you are black or white)
     */
    private fun setInfoAboutThisPlayers(blackType: Int, whiteType: Int) {
        // popup
        if (blackType == Player.PlayerTypes.BTLOCAL.ordinal) {
            showLongToast(resources.getString(R.string.game_startAsBlack))
        } else if (whiteType == Player.PlayerTypes.BTLOCAL.ordinal) {
            showLongToast(resources.getString(R.string.game_startAsWhite))
        }
        // sets messages
        // TODO update for online play
        blackTurnText = when (blackType) {
            Player.PlayerTypes.BTLOCAL.ordinal -> resources.getString(R.string.game_blackYouTurn)
            else -> resources.getString(R.string.game_blackTurn)
        }
        whiteTurnText = when (whiteType) {
            Player.PlayerTypes.BTLOCAL.ordinal -> resources.getString(R.string.game_whiteYouTurn)
            else -> resources.getString(R.string.game_whiteTurn)
        }
    }

    private fun showLongToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun updateTurnText(lastMove: Move?) {
        if (lastMove?.stone == Stone.BLACK) {
            // White's turn
            turnText.text = whiteTurnText
        } else {
            // Black's turn
            turnText.text = blackTurnText
        }
        turnText.text = when(lastMove?.stone) {
            Stone.BLACK -> whiteTurnText
            Stone.WHITE -> blackTurnText
            Stone.EMPTY -> getTextAfterPass(lastMove)
            else -> ""
        }
    }

    /**
     * Returns the correct text for the turn text after a pass
     */
    private fun getTextAfterPass(lastMove: Move): String {
        return if (lastMove.point == Game.PASS_MOVE.point) {
            if (turnText.text == whiteTurnText) resources.getString(R.string.game_blackPassTurn)
            else resources.getString(R.string.game_whitePassTurn)
        } else { "" }
    }

    private fun displayEndOfGame() {
        turnText.text = resources.getString(R.string.game_endOfGame)
        passButton.visibility = View.GONE
    }

}