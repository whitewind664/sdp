package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.user.FirebaseUserBundleProvider
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_new_message.view.*

class UserSearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "UserSearch"
    }

    lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView

    lateinit var adapter: GroupAdapter<ViewHolder>

    lateinit var user: LetsGoUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        Log.d(TAG, "================================================================")

        searchView = findViewById<SearchView>(R.id.user_search_search_view)
        searchView.setOnQueryTextListener(LetsGoUserSearchViewListerner(this))

        recyclerView = findViewById<RecyclerView>(R.id.user_search_recycler_view)
        adapter = GroupAdapter<ViewHolder>()
        recyclerView.adapter = adapter

        user = FirebaseUserBundleProvider.getUserBundle()!!.getUser()
    }


    private fun searchUsersOutputToRecycler(query: String) {
        user.downloadUsersByNick(query)
            .addOnSuccessListener { users ->
                adapter.clear()
                users.forEach {
                    adapter.add(UserListItem(it))
                }
            }
            .addOnFailureListener {
                adapter.clear()
                adapter.add(UserListItem(LetsGoUser("").apply { nick = "SEARCH FAILED" }))
            }
    }

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

    private class LetsGoUserSearchViewListerner(val userSearchActivity: UserSearchActivity) : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                userSearchActivity.searchUsersOutputToRecycler(query)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }
}

