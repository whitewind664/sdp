package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.matchmaking.Matchmaking

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapButton = findViewById<Button>(R.id.main_button_map)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<Button>(R.id.main_button_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val findMatchButton = findViewById<Button>(R.id.main_button_findMatch)
        findMatchButton.setOnClickListener {
            mapButton.isEnabled = false
            profileButton.isEnabled = false
            findMatchButton.isEnabled = false
            Matchmaking.findMatch { gameId ->
                val intent = Intent(this, OnlineGameActivity::class.java)
                intent.putExtra(OnlineGameActivity.GAME_ID, gameId)
                startActivity(intent)
            }
        }

    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main;
    }

}