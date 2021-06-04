package com.github.gogetters.letsgo.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.util.firebase.FirebaseService
import com.github.gogetters.letsgo.game.view.GoView
import com.github.gogetters.letsgo.matchmaking.Matchmaking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameActivity : BaseActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
        const val EXTRA_GAME_TYPE = "com.github.gogetters.letsgo.game.GAME_TYPE"
        const val EXTRA_LOCAL_COLOR = "com.github.gogetters.letsgo.game.LOCAL_COLOR"
        const val EXTRA_GAME_ID = "com.github.gogetters.letsgo.game.GAME_ID"
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
    private var onEnd: () -> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val gameId = intent.getStringExtra(EXTRA_GAME_ID)
        val gameType = intent.getStringExtra(EXTRA_GAME_TYPE) ?: "LOCAL"
        val localColorString = intent.getStringExtra(EXTRA_LOCAL_COLOR)
        val localColor = Stone.fromString(localColorString!!)
        setInfoAboutThisPlayers(localColor)


        val boardSize = Board.Size.withSize(gameSizeInput)
        goView = GoView(this, boardSize)
        val boardFrame = findViewById<FrameLayout>(R.id.game_frameLayout_boardFrame)
        boardFrame.addView(goView)
        turnText = findViewById(R.id.game_textView_turnIndication)
        blackScore = findViewById(R.id.game_textView_blackScore)
        whiteScore = findViewById(R.id.game_textView_whiteScore)
        passButton = findViewById(R.id.game_button_pass)


        val touchInputDelegate = InputDelegate()
        goView.inputDelegate = touchInputDelegate
        passButton.setOnClickListener {
            passPopUp(touchInputDelegate)
        }


        val (blackPlayer: Player, whitePlayer: Player) = when (gameType) {

            "LOCAL" -> Pair(DelegatedPlayer(Stone.BLACK, touchInputDelegate),
                    DelegatedPlayer(Stone.WHITE, touchInputDelegate))

            "BLUETOOTH", "FIREBASE" -> {

                val service = when (gameType) {
                    "BLUETOOTH" -> BluetoothActivity.service
                    "FIREBASE" -> FirebaseService(gameId!!, localColor)
                    else -> throw IllegalArgumentException("illegal game type $gameType")
                }

                if (gameType == "FIREBASE") {
                    (service as FirebaseService).setDelegate(InputDelegate())
                    onEnd = {
                        Matchmaking.endMatch()
                    }
                } else {
                    service.inputDelegate = InputDelegate()
                }


                when (localColor) {
                    Stone.WHITE -> Pair(DelegatedPlayer(Stone.BLACK, service.inputDelegate),
                            RemotePlayerAdapter(DelegatedPlayer(Stone.WHITE, touchInputDelegate), service))
                    Stone.BLACK -> Pair(RemotePlayerAdapter(DelegatedPlayer(Stone.BLACK, touchInputDelegate), service),
                            DelegatedPlayer(Stone.WHITE, service.inputDelegate))
                    else -> throw IllegalArgumentException("this cannot happen")
                }

            }

            else -> throw IllegalArgumentException("illegal game type $gameType")
        }

        game = Game(boardSize, komi, whitePlayer, blackPlayer)
        runGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        Matchmaking.surrender()
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

            onEnd();

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
    private fun setInfoAboutThisPlayers(localColor: Stone) {
        // popup
        when (localColor) {
            Stone.BLACK -> {
                showLongToast(resources.getString(R.string.game_startAsBlack))
                blackTurnText = resources.getString(R.string.game_blackYouTurn)
                whiteTurnText = resources.getString(R.string.game_whiteTurn)
            }
            Stone.WHITE -> {
                showLongToast(resources.getString(R.string.game_startAsWhite))
                blackTurnText = resources.getString(R.string.game_blackTurn)
                whiteTurnText = resources.getString(R.string.game_whiteYouTurn)
            }
            Stone.EMPTY -> {
                showLongToast(resources.getString(R.string.game_startLocal))
                blackTurnText = resources.getString(R.string.game_blackYouTurn)
                whiteTurnText = resources.getString(R.string.game_whiteYouTurn)
            }
        }
    }

    private fun showLongToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun updateTurnText(lastMove: Move?) {
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