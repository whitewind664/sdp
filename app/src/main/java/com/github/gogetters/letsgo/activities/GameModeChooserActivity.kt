package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.ogs.*
import org.json.JSONObject
import java.util.*

class GameModeChooserActivity : BaseActivity() {

    private lateinit var titleText: TextView
    private lateinit var localButton: Button
    private lateinit var ogsButton: Button
    private lateinit var btButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var submitButton: Button

    private lateinit var ogs: OGSCommunicatorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initButtons()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game_mode_chooser
    }

    private fun initButtons() {
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
            initOGS()
        }

        btButton = findViewById(R.id.gameModeChooser_button_bluetooth)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initChallengeButton() {
        val challengeButton = findViewById<Button>(R.id.gameModeChooser_button_challenge)

        challengeButton.setOnClickListener {
            ogs.startChallenge(OGSChallenge("", OGSGame("", "mygame"), Stone.BLACK))
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
        }
    }

    private fun initOGS() {
        titleText.text = resources.getString(R.string.gameModeChooser_loginTitle)
        localButton.visibility = View.GONE
        ogsButton.visibility = View.GONE
        btButton.visibility = View.GONE
        usernameEditText.visibility = View.VISIBLE
        passwordEditText.visibility = View.VISIBLE
        submitButton.visibility = View.VISIBLE

        ogs = OGSCommunicatorService(
                VolleyOnlineService(this),
                SocketIOService(),
                resources.getString(R.string.ogs_client_id),
                resources.getString(R.string.ogs_client_secret))
        val challengeButton = findViewById<Button>(R.id.gameModeChooser_button_challenge)

        submitButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            ogs.authenticate(username, password).setOnResponse {
                val result = if (it) "Successful" else "Failed"
                Toast.makeText(this@GameModeChooserActivity,
                        "Authentication $result", Toast.LENGTH_LONG).show()
                if (it) {
                    challengeButton.visibility = View.VISIBLE
                }
            }
        }

        initChallengeButton()
    }

    /**
     * Send the information of the new game on OGS to the interface
     */
    private fun startOgsOnlineGame(ogsChallenge: OGSChallenge) {
        ogs.startChallenge(ogsChallenge)
        //TODO go to game activity...
    }
}