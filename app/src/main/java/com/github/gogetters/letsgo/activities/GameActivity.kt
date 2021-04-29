package com.github.gogetters.letsgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.github.gogetters.letsgo.game.view.GoView
import com.github.gogetters.letsgo.game.util.TouchInputDelegate
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.Game
import com.github.gogetters.letsgo.util.BluetoothGTPService
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
        const val EXTRA_PLAYER_TYPES = "com.github.gogetters.letsgo.game.PLAYER_TYPES"
        const val EXTRA_BT_SERVICE = "com.github.gogetters.letsgo.game.BT_SERVICE"
    }

    private lateinit var game: Game
    private lateinit var goView: GoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val playerTypes = intent.getStringArrayExtra(EXTRA_PLAYER_TYPES)
        //TODO HOW AM I SUPPOSED TO GET THIS SHIT..................
        val bluetoothService = BluetoothGTPService()

        val boardSize = Board.Size.withSize(gameSizeInput)
        goView = GoView(this, boardSize)

        val touchInputDelegate = TouchInputDelegate()

        goView.touchInputDelegate = touchInputDelegate


        val boardFrame = findViewById<FrameLayout>(R.id.game_frameLayout_boardFrame)
        boardFrame.addView(goView)

        //TODO use different delegate depending on which type of Player
        val whiteType = playerTypes!![0]
        val blackType = playerTypes[0]

        val whitePlayer = Player.playerOf(Stone.BLACK, whiteType, touchInputDelegate, bluetoothService)
        val blackPlayer = Player.playerOf(Stone.WHITE, blackType, touchInputDelegate, bluetoothService)

        game = Game(boardSize, komi, whitePlayer, blackPlayer)

        GlobalScope.launch {
            var boardState = game.playTurn()
            while (!boardState.gameOver) {
                drawBoard(boardState)
                boardState = game.playTurn()
            }
        }
    }

    //TODO oneliner can be inlined??
    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }

}