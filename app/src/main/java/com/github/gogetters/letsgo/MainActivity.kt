package com.github.gogetters.letsgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
            val intent = Intent(this, Profile::class.java)

            startActivity(intent)
        }

        val goButton = findViewById<Button>(R.id.goButton)
        goButton.setOnClickListener {
            val intent = Intent(this, GoActivity::class.java)

            startActivity(intent)
        }

    }

}