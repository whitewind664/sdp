package com.github.gogetters.letsgo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.cache.Cache
import com.github.gogetters.letsgo.chat.model.UserData
import com.github.gogetters.letsgo.chat.views.ChatNewMessageItem
import com.github.gogetters.letsgo.database.Database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_new_message.*

/**
 * Fetches users from database and enables to launch chat with them
 * The class uses the 3rd party groupie library for recycler views and for their bindings
 */
class ChatNewMessageActivity : AppCompatActivity() {

    private val activity = this
    private val adapter = GroupAdapter<ViewHolder>()
    private var cachedUsers = arrayListOf<ChatNewMessageItem>()

    companion object {
        val KEY = "NEW_MESSAGE_CHANNEL_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_new_message)
        chat_recyclerview_new_message.adapter = adapter
        fetchUsers()
    }

    /**
     * Fetches users listed under the /users path
     */
    private fun fetchUsers() {

        if (Database.isConnected) {
            val ref = FirebaseDatabase.getInstance().getReference("/users")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val user = it.getValue(UserData::class.java)
                        if (user != null) {
                            //adapter.add(ChatNewMessageItem(user))
                            cachedUsers.add(ChatNewMessageItem(user))
                        }
                    }
                    Cache.saveUserData(activity, cachedUsers)
                    Log.d("CACHE", getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).getString("userList", "")!!)
                    presentUsers()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        else {
            cachedUsers = Cache.loadUserData(activity)
            presentUsers()
        }
    }

    /**
     * Links users to the UI
     */
    private fun presentUsers() {

        cachedUsers.forEach {
            adapter.add(it)}

        // Start a new chat history given the user you clicked on
        adapter.setOnItemClickListener { item, view ->
            val newMessageItem = item as ChatNewMessageItem
            val intent = Intent(view.context, ChatActivity::class.java)
            intent.putExtra(KEY, newMessageItem.user)
            startActivity(intent)
            // Finish the current activity in order to navigate back to last message chat activity
            finish()
        }
    }

}
