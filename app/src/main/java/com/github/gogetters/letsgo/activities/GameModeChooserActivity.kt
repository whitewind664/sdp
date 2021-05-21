package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.*

class GameModeChooserActivity : BaseActivity() {

    private lateinit var titleText: TextView
    private lateinit var localButton: Button
    private lateinit var ogsButton: Button
    private lateinit var btButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        titleText = findViewById(R.id.gameModeChooser_textView_title)
        usernameEditText = findViewById(R.id.gameModeChooser_editText_loginUsername)
        passwordEditText = findViewById(R.id.gameModeChooser_editText_loginPassword)
        submitButton = findViewById(R.id.gameModeChooser_button_loginSubmit)

        localButton = findViewById<Button>(R.id.gameModeChooser_button_local)
        localButton.setOnClickListener {
            // start a local game
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
                putExtra(GameActivity.EXTRA_KOMI, 5.5)
                val localType = Player.PlayerTypes.LOCAL.ordinal
                putExtra(GameActivity.EXTRA_PLAYER_BLACK, localType)
                putExtra(GameActivity.EXTRA_PLAYER_WHITE, localType)
            }
            startActivity(intent)
        }

        ogsButton = findViewById(R.id.gameModeChooser_button_ogs)
        ogsButton.setOnClickListener {
            // login
            changeToOgsLoginView()
            /**submitButton.setOnClickListener {
                val ogsCommunicator = OGSCommunicatorService(VolleyOnlineService(this), resources.getString(R.string.ogs_client_id), resources.getString(R.string.ogs_client_secret))
                ogsCommunicator.authenticate(usernameEditText.toString(), passwordEditText.toString())
            }*/

            // TODO ask for information concerning the game if completed

            // startOgsOnlineGame
        }

        btButton = findViewById<Button>(R.id.gameModeChooser_button_bluetooth)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game_mode_chooser
    }

    private fun changeToOgsLoginView() {
        titleText.text = resources.getString(R.string.gameModeChooser_loginTitle)
        localButton.visibility = View.GONE
        ogsButton.visibility = View.GONE
        btButton.visibility = View.GONE
        usernameEditText.visibility = View.VISIBLE
        passwordEditText.visibility = View.VISIBLE
        submitButton.visibility = View.VISIBLE
    }

    /**
     * Send the information of the new game on OGS to the interface
     */
    private fun startOgsOnlineGame(ogsCommunicator: OGSCommunicatorService) {
        val game = OGSGame("game")
        ogsCommunicator.startChallenge(OGSChallenge(game, Stone.BLACK))

        // TODO display waiting screen until confirmed
    }
}