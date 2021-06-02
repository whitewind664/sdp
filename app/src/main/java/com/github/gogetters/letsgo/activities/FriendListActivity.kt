package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

// This is all quite rushed code but its main reason is to show functionality!
// TODO Make this pretty!
class FriendListActivity : AppCompatActivity() {

    lateinit var searchView: SearchView
    lateinit var usersListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        searchView = findViewById(R.id.friend_list_search)
        usersListView = findViewById(R.id.friend_list_users_list)

        val currentUser = Authentication.getCurrentUser()

        val user = if (currentUser != null) LetsGoUser(currentUser.uid) else LetsGoUser("tESTuID")
        user.downloadFriends().addOnSuccessListener {

            val friendList = listOf("FRIENDS") +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.ACCEPTED)
                        .map { formatUserInfo(it) } +
                    "SENT FRIEND REQUESTS" +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.SENT)
                        .map { formatUserInfo(it) } +
                    "PENDING FRIEND REQUESTS" +
                    user.listFriendsByStatus(LetsGoUser.FriendStatus.REQUESTED)
                        .map { formatUserInfo(it) }

            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                friendList
            )
            usersListView.adapter = arrayAdapter
        }
    }

    private fun formatUserInfo(user: LetsGoUser): String {
        return "${user.nick} - ${user.first} ${user.last} - ${user.country}, ${user.city}"
    }
}
