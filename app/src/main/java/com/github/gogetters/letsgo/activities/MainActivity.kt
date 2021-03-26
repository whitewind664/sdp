package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameButton = findViewById<Button>(R.id.startGameButton)
        gameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
                putExtra(GameActivity.EXTRA_KOMI, 5.5)
            }
            startActivity(intent)
        }

        val mapButton = findViewById<Button>(R.id.openMapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).apply {}
            startActivity(intent)
        }

        val profileButton = findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val chatButton = findViewById<Button>(R.id.chatButton)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

    }

}