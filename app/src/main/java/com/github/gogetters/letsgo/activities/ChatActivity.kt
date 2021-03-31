package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.ChatMessage
import com.github.gogetters.letsgo.chat.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class ChatActivity : AppCompatActivity() {
    // the text field to which messages are written
    private lateinit var entryText: EditText
    private lateinit var adapter: MessageAdapter
    private val DEFAULT_USERNAME = "Opponent"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        entryText = findViewById(R.id.chat_editText_input)

        val listView: ListView = findViewById(R.id.chat_listView_messages)
        adapter = MessageAdapter(this)
        listView.adapter = adapter
    }


    fun sendMessage(view: View?) {
        val messageText: String = entryText.text.toString()

        if (messageText.isNotEmpty()) {
            // TODO send message. For the moment it is just displayed.

            entryText.text.clear()
            // get username to display
            var userName = DEFAULT_USERNAME
            val authInstance = FirebaseAuth.getInstance().currentUser
            if (authInstance != null && authInstance.displayName != null) {
                userName = authInstance.displayName!!
            }
            // display the message without sending
            val message = ChatMessage(messageText, true, Calendar.getInstance().time, userName)
            adapter.addMessage(message)
            adapter.notifyDataSetChanged()
        }
    }

    // TODO interaction with backend
}