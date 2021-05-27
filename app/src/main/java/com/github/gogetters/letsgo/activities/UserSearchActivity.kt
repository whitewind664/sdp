package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.util.Log
import android.view.View
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

    class UserListItem(val user: LetsGoUser) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.apply {
//                Log.d(TAG, user.toString())
//                item_user_nick.text = user.nick
//                item_user_firstlast.text = ProfileActivity.combineTwoTextFields(user.first, user.last, " ")
                chat_textView_username.text = user.nick
                chat_textView_firstlast.visibility = View.VISIBLE
                chat_textView_firstlast.text = ProfileActivity.combineTwoTextFields(user.first, user.last, " ")
            }
        }

        override fun getLayout(): Int {
//            return R.layout.item_user_list_user
            return R.layout.item_chat_new_message
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)

        Log.d(TAG, "================================================================")

        val recyclerView = findViewById<RecyclerView>(R.id.user_search_recycler_view)

        val user = FirebaseUserBundleProvider.getUserBundle()!!.getUser()

        val adapter = GroupAdapter<ViewHolder>()
        user.downloadUsersByNick("tester").continueWith {
            val users = it.result;
            users.forEach {
                adapter.add(UserListItem(it))
            }
            recyclerView.adapter = adapter
        }
    }
}

