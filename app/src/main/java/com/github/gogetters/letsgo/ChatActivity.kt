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
import com.github.gogetters.letsgo.chat.ChatMessage
import com.github.gogetters.letsgo.chat.MessageAdapter
import java.util.*


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
        val messageText: String = entryText.text.toString()

        if (messageText.isNotEmpty()) {
            // TODO send message. For the moment it is just displayed.

            entryText.text.clear()

            // display the message without sending
            val message = ChatMessage(messageText, true, Calendar.getInstance().time)
            adapter.addMessage(message)
            adapter.notifyDataSetChanged()
        }
    }

    // TODO interaction with backend
}