package com.example.introapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.main_GreetingButton)
        button.setOnClickListener {
            val editText = findViewById<EditText>(R.id.main_NameEditText)
            val message = editText.text.toString();
            val intent = Intent(this, GreetingActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }

            startActivity(intent)
        }
    }
}