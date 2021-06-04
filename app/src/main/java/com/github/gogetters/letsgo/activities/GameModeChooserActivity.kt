package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.game.Player
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.matchmaking.Matchmaking
import java.util.*

class GameModeChooserActivity : BaseActivity() {

    private lateinit var titleText: TextView
    private lateinit var localButton: Button
    private lateinit var rankedButton: Button
    private lateinit var unrankedButton: Button
    private lateinit var btButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initButtons()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game_mode_chooser
    }

    private fun initButtons() {
        titleText = findViewById(R.id.gameModeChooser_textView_title)

        localButton = findViewById<Button>(R.id.gameModeChooser_button_local)
        localButton.setOnClickListener {
            // start a local game
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
                putExtra(GameActivity.EXTRA_KOMI, 5.5)
                putExtra(GameActivity.EXTRA_LOCAL_COLOR, Stone.BLACK.toString())
                putExtra(GameActivity.EXTRA_GAME_TYPE, "LOCAL")
            }
            startActivity(intent)
        }

        rankedButton = findViewById(R.id.gameModeChooser_button_ranked)
        rankedButton.setOnClickListener {
            val intent = Intent(this, WaitMatchActivity::class.java).apply {
                putExtra(WaitMatchActivity.EXTRA_IS_RANKED, true)
            }
            startActivity(intent)
        }

        unrankedButton = findViewById(R.id.gameModeChooser_button_unranked)
        unrankedButton.setOnClickListener {
            val intent = Intent(this, WaitMatchActivity::class.java).apply {
                putExtra(WaitMatchActivity.EXTRA_IS_RANKED, false)
            }
            startActivity(intent)
        }

        val uid = Authentication.getUid()

        if (uid == null) {
            rankedButton.isEnabled = false
            unrankedButton.isEnabled = false
        }

        btButton = findViewById(R.id.gameModeChooser_button_bluetooth)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }
    }
}