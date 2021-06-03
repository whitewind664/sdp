package com.github.gogetters.letsgo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.views.ChatNewMessageItem
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_new_message.*

/**
 * Fetches users from database and enables to launch chat with them
 * The class uses the 3rd party groupie library for recycler views and for their bindings
 */
class ChatNewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_new_message)
        // fetch the users from database
        listUsers()
    }

    // key for passing the object to new activity
    companion object {
        val KEY = "NEW_MESSAGE_CHANNEL_KEY"
    }

    private fun listUsers() {
        val user = LetsGoUser(Authentication.getUid()!!)

        user.downloadFriends().addOnSuccessListener {
            val friends = user.friendsByStatus!![LetsGoUser.FriendStatus.ACCEPTED]!!

            friends.forEach{
                it.uid
            }
        }


        user.downloadFriends().addOnSuccessListener {
            val friends = user.friendsByStatus!![LetsGoUser.FriendStatus.ACCEPTED]!!

            val adapter = GroupAdapter<ViewHolder>()

            friends.forEach {
                adapter.add(ChatNewMessageItem(it))
            }

            adapter.setOnItemClickListener { item, view ->
                // cast item to the actual object to retrieve info from it later on
                val newMessageItem = item as ChatNewMessageItem
                val intent = Intent(view.context, ChatActivity::class.java)
                // pass object from one activity to the other
                intent.putExtra(KEY, newMessageItem.user)
                startActivity(intent)
                // finish the current activity in order to navigate back to last message chat activity
                finish()
            }

            chat_recyclerview_new_message.adapter = adapter
        }


//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // pick the groupie adapter
//                val adapter = GroupAdapter<ViewHolder>()
//                // and fill it in with all the users
//                snapshot.children.forEach {
//                    val user = it.getValue(UserData::class.java)
//                    if (user != null) {
//                        adapter.add(ChatNewMessageItem(user))
//                    }
//                }
//
//                // whenever you click on any of the items in the recycler view list
//                // start a new activity based on the user info
//                adapter.setOnItemClickListener { item, view ->
//                    // cast item to the actual object to retrieve info from it later on
//                    val newMessageItem = item as ChatNewMessageItem
//                    val intent = Intent(view.context, ChatActivity::class.java)
//                    // pass object from one activity to the other
//                    intent.putExtra(KEY, newMessageItem.user)
//                    startActivity(intent)
//                    // finish the current activity in order to navigate back to last message chat activity
//                    finish()
//                }
//
//                // link the groupie adapter to the recycler view
//                chat_recyclerview_new_message.adapter = adapter
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//
//        })
    }
}
