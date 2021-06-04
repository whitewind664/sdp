package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.FrameLayout
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.util.ogs.OGSCommunicatorService
import com.github.gogetters.letsgo.game.view.GoView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameActivity : BaseActivity() {
    companion object {
        const val EXTRA_GAME_SIZE = "com.github.gogetters.letsgo.game.GAME_SIZE"
        const val EXTRA_KOMI = "com.github.gogetters.letsgo.game.KOMI"
        const val EXTRA_GAME_TYPE = "com.github.gogetters.letsgo.game.GAME_TYPE"
        const val EXTRA_LOCAL_COLOR = "com.github.gogetters.letsgo.game.LOCAL_COLOR"
    }

    private lateinit var game: Game
    private lateinit var goView: GoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameSizeInput = intent.getIntExtra(EXTRA_GAME_SIZE, 9)
        val komi = intent.getDoubleExtra(EXTRA_KOMI, 5.5)
        val gameType = intent.getStringExtra(EXTRA_GAME_TYPE) ?: "LOCAL"


        val boardSize = Board.Size.withSize(gameSizeInput)
        goView = GoView(this, boardSize)
        val boardFrame = findViewById<FrameLayout>(R.id.game_frameLayout_boardFrame)
        boardFrame.addView(goView)

        val touchInputDelegate = InputDelegate()
        goView.inputDelegate = touchInputDelegate


        val (whitePlayer: Player, blackPlayer: Player) = when (gameType) {

            "LOCAL" -> Pair(DelegatedPlayer(Stone.BLACK, touchInputDelegate),
                    DelegatedPlayer(Stone.WHITE, touchInputDelegate))

            "BLUETOOTH", "OGS" -> {

                val service = when (gameType) {
                    "BLUETOOTH" -> BluetoothActivity.service
                    "OGS" -> OGSCommunicatorService.service
                    else -> throw IllegalArgumentException("illegal game type $gameType")
                }

                service.inputDelegate = InputDelegate()
                val localColorString = intent.getStringExtra(EXTRA_LOCAL_COLOR)
                val localColor = Stone.fromString(localColorString!!)
                if (localColor == Stone.EMPTY)
                    throw IllegalArgumentException("local player must have real color")
                when (localColor) {
                    Stone.WHITE -> Pair(DelegatedPlayer(Stone.WHITE, service.inputDelegate),
                            RemotePlayerAdapter(DelegatedPlayer(Stone.WHITE, touchInputDelegate), service))
                    Stone.BLACK -> Pair(RemotePlayerAdapter(DelegatedPlayer(Stone.BLACK, touchInputDelegate), service),
                            DelegatedPlayer(Stone.WHITE, service.inputDelegate))
                    else -> throw IllegalArgumentException("this cannot happen")
                }

            }
            else -> throw IllegalArgumentException("illegal game type $gameType")
        }

        game = Game(boardSize, komi, whitePlayer, blackPlayer)

        GlobalScope.launch {
            var boardState = game.playTurn()
            while (!boardState.gameOver) {
                drawBoard(boardState)
                boardState = game.playTurn()
            }
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game
    }


    private fun drawBoard(boardState: BoardState) {
        goView.updateBoardState(boardState)
    }

}