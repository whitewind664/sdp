package com.github.gogetters.letsgo.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.cache.Cache
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.views.ChatLastMessageItem
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.user.LetsGoUser
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

    // Stores last messages of the users
    private val lastMessages = HashMap<String, ChatMessageData>()
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // if not logged in -> go to login activity
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_chat_last_message)

            // Link the groupie adapter and decorate with horizontal line
            chat_recyclerview_last_message.adapter = adapter
            chat_recyclerview_last_message.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

            // Start a new chat history given the user you clicked on
            adapter.setOnItemClickListener { item, view ->
                val intent = Intent(this, ChatActivity::class.java)
                val msgItem = item as ChatLastMessageItem
                intent.putExtra(ChatNewMessageActivity.KEY, msgItem.chatUser)
                startActivity(intent)
            }

            // Without checking database connection -> blinking
            updateMessageList()
            listenForLastMessages()

            // With checking database connection -> delay
            /*
            if (Database.isConnected) { listenForLastMessages() }
            else { updateMessageList() }
            */

            // Floating button to launch the ChatNewMessageActivity
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
        val fromId = Authentication.getCurrentUser()!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/last-messages-node/$fromId")

        // Whenever a new message is sent and saved as last message in database,
        // store it in the hashmap and refresh the adapter with the new content
        ref.addChildEventListener(object: ChildEventListener {
            // If new chat starts
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                presentMessages(snapshot)
            }
            // If chat exists already
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                presentMessages(snapshot)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * Get the new message and append it to the list of messages for UI
     */
    private fun presentMessages(snapshot: DataSnapshot) {
        val chatMessage = snapshot.getValue(ChatMessageData::class.java) ?: return
        lastMessages[snapshot.key!!] = chatMessage
        Cache.saveLastChatData(this, lastMessages.values)
        Log.d("CACHE", getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).getString("lastMessageList", "")!!)
        updateMessageList()
    }

    /**
     * Link the content from the hashmap to the UI
     */
    private fun updateMessageList() {
        val cachedLastMessages = Cache.loadLastChatData(this)
        adapter.clear()
        cachedLastMessages.forEach {
            adapter.add(ChatLastMessageItem(it))
        }
    }

}