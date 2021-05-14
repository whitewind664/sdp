package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.gogetters.letsgo.R

class ChatLastMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_last_message)

        val fab: View = findViewById(R.id.chat_button_fab)
        fab.setOnClickListener {
            val intent = Intent(this, ChatNewMessageActivity::class.java)
            startActivity(intent)
        }
    }

}