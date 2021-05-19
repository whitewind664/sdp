package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Player

class GameModeChooserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localButton = findViewById<Button>(R.id.gameModeChooser_button_local)
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

        val ogsButton = findViewById<Button>(R.id.gameModeChooser_button_ogs)
        ogsButton.setOnClickListener {
            // login

            // TODO ask for information concerning the game 
        }

        val btButton = findViewById<Button>(R.id.gameModeChooser_button_bluetooth)
        btButton.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_game_mode_chooser
    }
}