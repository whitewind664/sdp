package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.LetsGoUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// This is all quite rushed code but its main reason is to show functionality!
// TODO Make this pretty!
class FriendListActivity : AppCompatActivity() {

    lateinit var friendListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        friendListView = findViewById(R.id.friend_list_list)

        val currentUser = Firebase.auth.currentUser

        val user = if (currentUser != null) LetsGoUser(currentUser.uid) else LetsGoUser("tESTuID")
        user.downloadFriends().addOnSuccessListener {

            val friendList = listOf("FRIENDS") +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.ACCEPTED)
                        .map { it.toString() } +
                    "SENT FRIEND REQUESTS" +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.SENT).map { it.toString() } +
                    "PENDING FRIEND REQUESTS" +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.REQUESTED)
                        .map { it.toString() }

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                friendList
            )
            friendListView.adapter = arrayAdapter
        }
    }
}
