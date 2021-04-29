package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlin.math.exp

class TutorialActivity : AppCompatActivity() {
    private var textProgressIndex: Int = 0
    private val tutorialTextIds = listOf(R.string.tutorial_intro, R.string.tutorial_board, R.string.tutorial_board2, R.string.tutorial_stones, R.string.tutorial_stones2, R.string.tutorial_territory, R.string.tutorial_capturing, R.string.tutorial_capturing2, R.string.tutorial_selfCapture, R.string.tutorial_selfCapture2, R.string.tutorial_twoEyes, R.string.tutorial_score1, R.string.tutorial_KoRule, R.string.tutorial_KoRule2, R.string.tutorial_end, R.string.tutorial_score2)
    private var titleProgressIndex: Int = 0
    private val tutorialTitleIds = listOf(R.string.tutorial_title_intro, R.string.tutorial_title_board, R.string.tutorial_title_stones, R.string.tutorial_title_territories, R.string.tutorial_title_capturing, R.string.tutorial_title_selfCapture, R.string.tutorial_title_score1, R.string.tutorial_title_KoRule, R.string.tutorial_title_end, R.string.tutorial_title_score2, R.string.tutorial_title_outro)
    private var isFinished = false
    private var gameIsRunning = false

    private lateinit var game: TutorialGame
    private lateinit var goView: TutorialGoView
    private lateinit var boardFrame: FrameLayout
    private lateinit var boardExplanationText: TextView
    private lateinit var boardResetButton: Button
    private lateinit var explanationTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // board initialisation
        goView = TutorialGoView(this)
        val inputDelegate = InputDelegate()
        goView.inputDelegate = inputDelegate

        boardFrame = findViewById(R.id.tutorial_frameLayout_boardFrame)
        boardFrame.addView(goView)

        boardExplanationText = findViewById(R.id.tutorial_textView_boardExplanation)
        boardResetButton = findViewById(R.id.tutorial_button_reset)
        explanationTitle = findViewById(R.id.tutorial_textView_title)

        val localPlayer = TutorialLocalPlayer(inputDelegate)
        val tutorialPlayer = TutorialPlayer()
        game = TutorialGame(localPlayer, tutorialPlayer)
        hideBoard()

        // "next" button
        findViewById<Button>(R.id.tutorial_button_next).setOnClickListener {
            val (tutorialStep, boardState) = game.nextStep()
            changeView(tutorialStep, boardState, nextText = true)
        }

        // "reset" button
        boardResetButton.setOnClickListener {
            inputDelegate.clearInput()
            val (tutorialStep, boardState) = game.reinitStep()
            changeView(tutorialStep, boardState, nextText = false)
        }

        // run the game
        GlobalScope.launch {
            var boardState = game.playTurn()
            drawBoard(boardState)
            while (!boardState.gameOver) {
                if (gameIsRunning && !(game.tutorialPlayerIsNext() && tutorialPlayer.isOutOfMoves())) {
                    boardState = game.playTurn()
                    drawBoard(boardState)
                }
            }
        }
    }

    private fun changeView(tutorialStep: TutorialGame.TutorialStep, boardState: BoardState, nextText: Boolean) {
        drawBoard(boardState)

        // display board if necessary
        if (tutorialStep.displayBoard) {
            boardExplanationText.text = ""
            showBoard()
            goView.setPlayOptions(tutorialStep.recommendedMoves)
        } else {
            hideBoard()
        }

        // display text if necessary
        if (tutorialStep.displayText) {
            displayNextText(tutorialStep.displayBoard, nextText)
        }
    }

    private fun showBoard() {
        gameIsRunning = true
        boardFrame.visibility = View.VISIBLE
        boardExplanationText.visibility = View.VISIBLE
        boardResetButton.visibility = View.VISIBLE
        explanationTitle.visibility = View.GONE
    }

    private fun hideBoard() {
        boardFrame.visibility = View.GONE
        boardExplanationText.visibility = View.GONE
        boardResetButton.visibility = View.GONE
        explanationTitle.visibility = View.VISIBLE
        gameIsRunning = false
    }
    
    private fun displayNextText(boardIsDisplayed: Boolean, next: Boolean) {
        val textView = if (boardIsDisplayed) boardExplanationText else findViewById(R.id.tutorial_textView_explanation)
        val nextIndex = if (next) textProgressIndex + 1 else textProgressIndex
        val nextTitleIndex = if (boardIsDisplayed) titleProgressIndex else titleProgressIndex + 1

        if (nextIndex < tutorialTextIds.size && nextTitleIndex < tutorialTitleIds.size) {
            textProgressIndex = nextIndex
            titleProgressIndex = nextTitleIndex
            textView.text = resources.getString(tutorialTextIds[textProgressIndex])
            explanationTitle.text = resources.getString(tutorialTitleIds[titleProgressIndex])
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