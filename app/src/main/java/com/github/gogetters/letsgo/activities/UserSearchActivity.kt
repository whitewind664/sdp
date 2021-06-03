package com.github.gogetters.letsgo.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.activities.UserSearchActivity.ShowUserDialogOptions.*
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.user.LetsGoUser.FriendStatus
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_new_message.view.*

class UserSearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "UserSearch"
    }

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: GroupAdapter<ViewHolder>

    private lateinit var user: LetsGoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        searchView = findViewById(R.id.user_search_search_view)
        searchView.setOnQueryTextListener(LetsGoUserSearchViewListener(this))

        recyclerView = findViewById(R.id.user_search_recycler_view)
        adapter = GroupAdapter<ViewHolder>()
        adapter.setOnItemClickListener { item, _ ->
            val selectedUser = (item as UserListItem).user
            Log.d(TAG, "REGISTERED CLICK on user : $selectedUser")
            showUserOptions(selectedUser)
        }
        recyclerView.adapter = adapter

        user = FirebaseUserBundleProvider.getUserBundle()!!.getUser()
        user.downloadFriends()
    }

    /**
     * Defines text and order of Options when clicking on a user in search!
     */
    private enum class ShowUserDialogOptions(val dialogText: CharSequence) {
        // ViewProfile("View profile (WIP)"),
        SendFriendRequest("Send Friend Request"),
        AcceptFriend("Accept Friend Request"),
        DeleteFriend("Ignore Friend Request / Unfriend"),
        Cancel("Cancel")
    }

    /**
     * Shows dialog when clicking on a user in search and binds behaviour to each option
     */
    private fun showUserOptions(selectedUser: LetsGoUser) {

        val dialogOptions = mutableListOf<ShowUserDialogOptions>()
        if (selectedUser == user) {
            return // No options for yourself (except ViewProfile but that's a future feature)
        } else {
            when (user.getFriendStatus(selectedUser)) {
                null -> { // Selected user isn't friend
                    // Allow sending Friend request
                    dialogOptions.add(SendFriendRequest)
                }
                FriendStatus.REQUESTED -> { // Selected user has sent a pending friend request
                    // Allow accepting friend request and deleting friend (reject request)
                    dialogOptions.add(AcceptFriend)
                    dialogOptions.add(DeleteFriend)
                }
                FriendStatus.SENT -> { // Waiting for selected user to accept friend request
                    // Allow Deleting (undoing friend request)
                    dialogOptions.add(DeleteFriend)
                }
                FriendStatus.ACCEPTED -> { // Selected user is a friend
                    // Allow deleting friend
                    dialogOptions.add(DeleteFriend)
                }
            }
        }

        // If any option has been added also add the option to Cancel/(Close the window)
        if (dialogOptions.size >= 1) { dialogOptions.add(Cancel) }

        val dialogTexts = dialogOptions.map { it.dialogText }.toTypedArray()

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(selectedUser.nick)
        builder.setItems(dialogTexts) { dialog: DialogInterface, clickedIndex: Int ->
            when (dialogOptions[clickedIndex]) {
                Cancel -> {
                    dialog.dismiss()
                }
                SendFriendRequest -> {
                    this.user.requestFriend(selectedUser)
                    Log.d(TAG, "Sent friend request to : $selectedUser")
                    user.downloadFriends()
                }
                AcceptFriend -> {
                    this.user.acceptFriend(selectedUser)
                    Log.d(TAG, "Accepted friend request of : $selectedUser")
                    user.downloadFriends()
                }
                DeleteFriend -> {
                    this.user.deleteFriend(selectedUser)
                    Log.d(TAG, "Deleted friendship data of : $selectedUser")
                    user.downloadFriends()
                }
            }
        }
        builder.show()
    }

    /**
     * Displays all users that have a nickname starting with "query"
     */
    private fun searchUsersOutputToRecycler(query: String) {
        user.downloadUsersByNick(query)
            .addOnSuccessListener { usersListToRecycler(it) }
            .addOnFailureListener {
                adapter.clear()
                adapter.add(UserListItem(LetsGoUser("").apply { nick = "SEARCH FAILED" }))
            }
    }

    /**
     * Displays a list of users to the recyclerView
     */
    private fun usersListToRecycler(users: List<LetsGoUser>) {
        adapter.clear()
        users.forEach {
            adapter.add(UserListItem(it))
        }
    }

    /**
     * Used to display User Info in the RecyclerView
     */
    private class UserListItem(val user: LetsGoUser) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.apply {
                chat_textView_username.text = user.nick
                chat_textView_firstlast.visibility = View.VISIBLE
                chat_textView_firstlast.text =
                    ProfileActivity.combineTwoTextFields(user.first, user.last, " ")
            }
        }

        override fun getLayout(): Int {
            return R.layout.item_chat_new_message
        }
    }

    /**
     * Processes Search Bar "Events"
     */
    private class LetsGoUserSearchViewListener(val userSearchActivity: UserSearchActivity) :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                Log.d(TAG, "Searching for '$query'...")
                userSearchActivity.searchUsersOutputToRecycler(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }
}

