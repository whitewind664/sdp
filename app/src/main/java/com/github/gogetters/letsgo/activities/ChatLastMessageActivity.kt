package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.views.ChatLastMessageItem
import com.github.gogetters.letsgo.database.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_last_message.*

/**
 * Lists last messages previously received from users
 * Redirects to ChatNewMessageActivity
 */
class ChatLastMessageActivity : AppCompatActivity() {

    private val TAG = "Chat"

    private val adapter = GroupAdapter<ViewHolder>()
    // hashmap to store last messages of the users
    private val lastMessages = HashMap<String, ChatMessageData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if not logged in -> go to login activity
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_chat_last_message)

            // link the groupie adapter and decorate with horizontal line
            chat_recyclerview_last_message.adapter = adapter
            chat_recyclerview_last_message.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

            // set a listener to the items of the adapter
            adapter.setOnItemClickListener { item, view ->
                // redirect to previous chat with partner
                val intent = Intent(this, ChatActivity::class.java)
                // take the partner info out from the binded item
                val msgItem = item as ChatLastMessageItem
                // launch the new activity with the corresponding partner
                intent.putExtra(ChatNewMessageActivity.KEY, msgItem.chatUser)
                startActivity(intent)
            }

            listenForLastMessages()

            // floating button to launch a new chat
            val fab: View = findViewById(R.id.chat_button_fab)
            fab.setOnClickListener {
                val intent = Intent(this, ChatNewMessageActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * Whenever a new message is sent and saved in database as last message, save it in the hashmap
     * and request the adapter to refresh the view
     */
    private fun listenForLastMessages() {
        val fromId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/last-messages-node/$fromId")
        //ref.keepSynced(true)

        // whenever a new message is sent and saved as last message in database,
        // store it in the hashmap and refresh the adapter with the new content
        ref.addChildEventListener(object: ChildEventListener {
            // When new chat starts
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "WOW - onChildAdded\t$snapshot")

                val chatMessage = snapshot.getValue(ChatMessageData::class.java) ?: return
                lastMessages[snapshot.key!!] = chatMessage
                updateMessageList()
            }

            // If chat exist already
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessageData::class.java) ?: return
                lastMessages[snapshot.key!!] = chatMessage
                updateMessageList()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * Refresh the adapter with the new content from the hashmap
     */
    private fun updateMessageList() {
        adapter.clear()
        lastMessages.values.forEach {
            adapter.add(ChatLastMessageItem(it))
        }
    }

}