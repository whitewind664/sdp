package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.model.UserData
import com.github.gogetters.letsgo.chat.views.ChatMyMessageItem
import com.github.gogetters.letsgo.chat.views.ChatTheirMessageItem
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    lateinit var userId: String
    var toUser: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chat_recyclerview_messages.adapter = adapter
        userId = Authentication.getCurrentUser()!!.uid
        toUser = intent.getParcelableExtra<UserData>(ChatNewMessageActivity.KEY)

        listenForMessages()

        chat_send_button.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessages() {
        val fromId = userId
        val toId = toUser?.id
        val ref = FirebaseDatabase.getInstance().getReference("/messages-node/$fromId/$toId")
        //ref.keepSynced(true)

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessageData::class.java)
                // ADD to the adapter the text messages
                if (chatMessage != null) {
                    if (chatMessage.fromId == userId) {
                        adapter.add(ChatMyMessageItem(chatMessage.text))
                    } else {
                        adapter.add(ChatTheirMessageItem(chatMessage.text))
                    }
                }
                chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
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
            val fromId = userId!!
            val toId = toUser!!.id!! // This might work?
//            if (toUser?.id == null) {
//                toId = UNKNOWN
//            } else {
//                toId = toUser?.id
//            }
            val ref = FirebaseDatabase.getInstance().getReference("/messages-node/$fromId/$toId").push()
            //ref.keepSynced(true)
            val toRef = FirebaseDatabase.getInstance().getReference("/messages-node/$toId/$fromId").push()
            //toRef.keepSynced(true)
            val lastMessageRef = FirebaseDatabase.getInstance().getReference("/last-messages-node/$fromId/$toId")
            //lastMessageRef.keepSynced(true)
            val lastMessageToRef = FirebaseDatabase.getInstance().getReference("/last-messages-node/$toId/$fromId")
            //lastMessageToRef.keepSynced(true)

            val chatMessage =
                ChatMessageData(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

            ref.setValue(chatMessage).addOnSuccessListener {
                chat_editText_input.text.clear()
                chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
            }

            toRef.setValue(chatMessage).addOnSuccessListener {
                chat_recyclerview_messages.scrollToPosition(adapter.itemCount - 1)
            }

            lastMessageRef.setValue(chatMessage)

            lastMessageToRef.setValue(chatMessage)
        }

    }

}
