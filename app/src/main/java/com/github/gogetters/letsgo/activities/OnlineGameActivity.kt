package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R

class OnlineGameActivity: AppCompatActivity() {

    companion object {
        const val GAME_ID = "GAME_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameId = intent.getStringExtra(GAME_ID)
        setContentView(R.layout.activity_onlinegame)

        val gameIdText: TextView = findViewById(R.id.onlineGame_gameId)
        gameIdText.text = "$gameId"
    }
}