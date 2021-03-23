package com.github.gogetters.letsgo

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.chat.MessageAdapter


class ChatActivity : AppCompatActivity() {
    // the text field to which messages are written
    private lateinit var entryText: EditText
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        entryText = findViewById(R.id.editText)

        val listView: ListView = findViewById(R.id.messages_view)
        adapter = MessageAdapter(this)
        listView.adapter = adapter
    }

    fun sendMessage(view: View?) {
        val message: String = entryText.text.toString()


        if (message.isNotEmpty()) {
            // TODO send message. For the moment it is just displayed.

            entryText.text.clear()

            // display the message without sending
            adapter.addMessage(message)
        }
    }

    // TODO interaction with backend


}