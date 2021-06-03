package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.cache.Cache
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.views.ChatMyMessageItem
import com.github.gogetters.letsgo.chat.views.ChatTheirMessageItem
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    // Stores last messages of the chat
    private var lastMessages = LinkedList<ChatMessageData>()
    private val activity = this
    val adapter = GroupAdapter<ViewHolder>()
    lateinit var userId: String
    var toUser: LetsGoUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chat_recyclerview_messages.adapter = adapter
        userId = Authentication.getCurrentUser()!!.uid
        toUser = intent.getSerializableExtra(ChatNewMessageActivity.KEY) as LetsGoUser

        // Without checking database connection -> blinking
        loadData()
        listenForMessages()

        // With checking database connection -> delay
        /*
        if (Database.isConnected) {
            listenForMessages()
        } else {
            lastMessages = Cache.loadChatData(activity)
            updateMessageList()
        }
        */

        chat_send_button.setOnClickListener {
            if (Database.isConnected) { sendMessage() }
        }
    }

    private fun listenForMessages() {
        val fromId = userId
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages-node/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessageData::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromId == userId) {
                        adapter.add(ChatMyMessageItem(chatMessage.text))
                    } else {
                        adapter.add(ChatTheirMessageItem(chatMessage.text))
                    }
                    saveData(chatMessage)
                    chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun sendMessage() {

        val text = chat_editText_input.text.toString()

        if (text.isNotEmpty()) {
            val fromId = userId
            val toId = toUser!!.uid!!

            // Store chat message in messages node both under from and to ids
            val ref = FirebaseDatabase.getInstance().getReference("/messages-node/$fromId/$toId").push()
            val toRef = FirebaseDatabase.getInstance().getReference("/messages-node/$toId/$fromId").push()

            val chatMessage =
                ChatMessageData(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

            ref.setValue(chatMessage).addOnSuccessListener {
                chat_editText_input.text.clear()
                chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
            }

            toRef.setValue(chatMessage).addOnSuccessListener {
                chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
            }

            // Update last messages nodes
            val lastMessageRef = FirebaseDatabase.getInstance().getReference("/last-messages-node/$fromId/$toId")
            val lastMessageToRef = FirebaseDatabase.getInstance().getReference("/last-messages-node/$toId/$fromId")
            lastMessageRef.setValue(chatMessage)
            lastMessageToRef.setValue(chatMessage)
        }
    }

    private fun saveData(chatMessageData: ChatMessageData) {
        if (lastMessages.size == 10) { lastMessages.removeAt(0) }
        lastMessages.add(chatMessageData)
        Cache.saveChatData(activity, lastMessages)
    }

    private fun loadData() {
        lastMessages = Cache.loadChatData(this)
        Log.d("CACHE", lastMessages.toString())
        lastMessages.forEach {
            if (it.fromId == userId) {
                adapter.add(ChatMyMessageItem(it.text))
            } else {
                adapter.add(ChatTheirMessageItem(it.text))
            }
        }
    }
}
