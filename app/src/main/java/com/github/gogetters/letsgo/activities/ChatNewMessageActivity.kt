package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.github.gogetters.letsgo.chat.views.ChatNewMessageItem
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.user.LetsGoUser.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_new_message.*

/**
 * Fetches users from database and enables to launch chat with them
 * The class uses the 3rd party groupie library for recycler views and for their bindings
 */
class ChatNewMessageActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()

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

        var uidList = ArrayList<String>()

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    if (it.key != null) {
                        uidList.add(it.key as String)
                    }
                }
                val userBundle = FirebaseUserBundleProvider.getUserBundle()
                if (userBundle != null) {
                    val user = userBundle.getUser()
                    user.downloadUserList(uidList).continueWith {
                        it.getResult().forEach {
                            adapter.add(ChatNewMessageItem(it))}
                        presentUsers()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    /**
     * Links users to the UI
     */
    private fun presentUsers() {
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
