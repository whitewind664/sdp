package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.ChatMessage
import com.github.gogetters.letsgo.chat.MessageAdapter
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.types.MessageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*


class ChatActivity : AppCompatActivity() {

    private val _defaultUsername = "Default User"
    private val _testingChatId = "testingChatId"

    // current user of the chatApp
    private lateinit var userName: String
    // current user id of the chatApp
    private lateinit var userId: String
    // placeholder for chat bubbles
    private lateinit var listView: ListView
    // text message to send
    private lateinit var entryText: EditText
    // update connection to listview
    private lateinit var adapter: MessageAdapter
    private lateinit var messageListener: ChildEventListener

    // connect to the Chat Collection's Message Document in Firestore
//    private val firestoreChat by lazy {
//        FirebaseFirestore.getInstance().collection(COLLECTION_KEY).document(DOCUMENT_KEY)
//    }

    // data structure in Firestore
    companion object {
        const val COLLECTION_KEY = "Chat"
        const val DOCUMENT_KEY = "Message"
        const val NAME_FIELD = "Name"
        const val TEXT_FIELD = "Text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // get the current user info
        val authInstance = FirebaseAuth.getInstance().currentUser
        if (authInstance != null && authInstance.displayName != null) {
            userName = authInstance.displayName!!
            userId = authInstance.uid
        } else {
            userName = _defaultUsername
        }

        entryText = findViewById(R.id.chat_editText_input)
        listView = findViewById(R.id.chat_listView_messages)
        adapter = MessageAdapter(this)
        listView.adapter = adapter

        // define listener for message update (chat bubbles) in listview
        realtimeUpdateListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        Database.removeMessagesListener(_testingChatId, messageListener)
    }

    /**
     * Called when the ImageButton is clicked in the corresponding layout
     */
    fun sendMessage(view: View?) {
        // get the text message to send when the button is clicked
        val messageText: String = entryText.text.toString()

        if (messageText.isNotEmpty()) {

            // clear the text field
            entryText.text.clear()

            // create the message by matching the Firebase data structure
//            val newMessage = mapOf(
//                NAME_FIELD to userName,
//                TEXT_FIELD to messageText
//            )

            // send the message to the database
            Database.sendMessage(userId, _testingChatId, messageText, {
                Log.i("INFO", "message sent")
                Toast.makeText(this@ChatActivity, "Message Sent", Toast.LENGTH_SHORT).show()
            }, { e ->
                Log.e("ERROR", e.toString())
            })

        }
    }

    /**
     * Listener for the message update. Updates the message bubbles.
     */
    private fun realtimeUpdateListener() {

        messageListener = Database.addMessagesListener(_testingChatId) { snapshot ->
            val messageData = snapshot.getValue<MessageData>()

            messageData!!

            val belongsToUser = messageData.senderId == userId

            val message = ChatMessage(messageData.messageText, belongsToUser, Date(messageData.timestamp), if (belongsToUser) userName else "other guy")
            adapter.addMessage(message)
            listView.setSelection(listView.count + 1)
        }
//
//        firestoreChat.addSnapshotListener { documentSnapshot, e ->
//
//            when {
//                e != null -> e.message?.let { Log.e("ERROR", it) }
//                documentSnapshot != null && documentSnapshot.exists() -> {
//                    with(documentSnapshot) {
//                        // check whether incoming or outgoing message should be presented on screen based on userName
//                        var message: ChatMessage
//                        if (data?.get(NAME_FIELD).toString() != userName) {
//                            message = ChatMessage(data?.get(TEXT_FIELD).toString(), false, Calendar.getInstance().time, data?.get(NAME_FIELD).toString())
//                        } else {
//                            message = ChatMessage(data?.get(TEXT_FIELD).toString(), true, Calendar.getInstance().time, data?.get(NAME_FIELD).toString())
//                        }
//                        // present the message in the chat bubbles of the listview
//                        adapter.addMessage(message)
//                        listView.setSelection(listView.getCount() - 1)
//
//                    }
//                }
//            }
//
//        }

    }

}