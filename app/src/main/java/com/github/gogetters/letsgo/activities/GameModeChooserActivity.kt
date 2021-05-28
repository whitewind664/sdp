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

    private val service = VolleyOnlineService(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initButtons()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game_mode_chooser
    }

    private fun initOGS() {
        titleText.text = resources.getString(R.string.gameModeChooser_loginTitle)
        localButton.visibility = View.GONE
        ogsButton.visibility = View.GONE
        btButton.visibility = View.GONE
        usernameEditText.visibility = View.VISIBLE
        passwordEditText.visibility = View.VISIBLE
        submitButton.visibility = View.VISIBLE

        val toggleButton = findViewById<ToggleButton>(R.id.gameModeChooser_toggle)
        toggleButton.visibility = View.VISIBLE


        submitButton.setOnClickListener {
            val id = resources.getString(R.string.ogs_client_id)
            val secret = resources.getString(R.string.ogs_client_secret)

            val url = "https://online-go.com/oauth2/token/"
            val username = "kimonroxd"
            val password = "online-go.com"

            val body = JSONObject()

            body.put("client_id", id)
            body.put("client_secret", secret)
            body.put("grant_type", "password")
            body.put("username", username)
            body.put("password", password)

            val jsonRequest = object: StringRequest(Method.POST, url,
                    { Toast.makeText(this@GameModeChooserActivity, it, Toast.LENGTH_LONG).show() },
                    {
                        val response = it.networkResponse.data.decodeToString()
                        Log.d("VOLLEY ERROR", response)
                    })
            {
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    return mutableMapOf("Content-Type" to "application/x-www-form-urlencoded")
                }

                override fun getBody(): ByteArray {
                    val bodyBuilder = StringJoiner("&")
                    for (key in body.keys()) {
                        bodyBuilder.add("$key=${body.getString(key)}")
                    }
                    return bodyBuilder.toString().toByteArray()
                }
            }

            queue.add(jsonRequest)

        }
    }

    /**
     * Send the information of the new game on OGS to the interface
     */
    private fun startOgsOnlineGame(ogsCommunicator: OGSCommunicatorService) {
        val game = OGSGame("game")
        ogsCommunicator.startChallenge(OGSChallenge(game, Stone.BLACK))

        // TODO display waiting screen until confirmed
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
            // TODO finalize

            // login
            initOGS()
        }

        btButton = findViewById(R.id.gameModeChooser_button_bluetooth)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }
    }
}