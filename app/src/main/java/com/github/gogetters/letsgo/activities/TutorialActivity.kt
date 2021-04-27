package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Board
import com.github.gogetters.letsgo.game.BoardState
import com.github.gogetters.letsgo.game.Game
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.view.GoView
import com.github.gogetters.letsgo.tutorial.TutorialGame
import com.github.gogetters.letsgo.tutorial.TutorialGoView
import com.github.gogetters.letsgo.tutorial.TutorialLocalPlayer
import com.github.gogetters.letsgo.tutorial.TutorialPlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TutorialActivity : AppCompatActivity() {
    private var textProgressIndex: Int = 0
    private val tutorialTextIds = arrayOf(R.string.tutorial_intro, R.string.tutorial_board, R.string.tutorial_stones, R.string.tutorial_territory, R.string.tutorial_capturing, R.string.tutorial_selfCapture, R.string.tutorial_score1, R.string.tutorial_KoRule, R.string.tutorial_end, R.string.tutorial_score2)
    private var isFinished = false
    private var gameIsRunning = false

    private lateinit var game: TutorialGame
    private lateinit var goView: TutorialGoView
    private lateinit var boardFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // board initialisation
        goView = TutorialGoView(this)
        val inputDelegate = InputDelegate()
        goView.inputDelegate = inputDelegate

        boardFrame = findViewById<FrameLayout>(R.id.tutorial_frameLayout_boardFrame)
        boardFrame.addView(goView)

        val localPlayer = TutorialLocalPlayer(inputDelegate)
        val tutorialPlayer = TutorialPlayer()
        game = TutorialGame(localPlayer, tutorialPlayer)
        hideBoard()

        // button
        findViewById<Button>(R.id.tutorial_button_next).setOnClickListener { _ ->
            val (tutorialStep, boardState) = game.nextStep()
            drawBoard(boardState)

            // display text if necessary
            if (tutorialStep.displayText) {
                displayNextText()
            }

            // display board if necessary
            if (tutorialStep.displayBoard) {
                showBoard()
                goView.setPlayOptions(tutorialStep.recommendedMoves)
            } else {
                hideBoard()
            }
        }

        // run the game
        GlobalScope.launch {
            var boardState = game.playTurn()
            while (!boardState.gameOver) {
                if (gameIsRunning && !(game.tutorialPlayerIsNext() && tutorialPlayer.isOutOfMoves())) {
                    drawBoard(boardState)
                    boardState = game.playTurn()
                    drawBoard(boardState)
                }
            }
        }
    }

    private fun showBoard() {
        gameIsRunning = true
        boardFrame.visibility = View.VISIBLE
    }

    private fun hideBoard() {
        boardFrame.visibility = View.GONE
        gameIsRunning = false
    }
    
    private fun displayNextText() {
        val textView = findViewById<TextView>(R.id.tutorial_textView_explanation)
        if (textProgressIndex + 1 < tutorialTextIds.size) {
            textProgressIndex += 1
            textView.text = resources.getString(tutorialTextIds[textProgressIndex])
        } else {
            if (!isFinished) {
                // handle end of tutorial
                textView.text = resources.getString(R.string.tutorial_outro)
                val buttonView = findViewById<Button>(R.id.tutorial_button_next)
                buttonView.text = resources.getString(R.string.tutorial_buttonTextGoBack)
                isFinished = true
            } else {
                // go to main menu
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }
}