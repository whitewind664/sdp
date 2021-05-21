package com.github.gogetters.letsgo.chat.views

import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.UserData
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_new_message.view.*

class ChatNewMessageItem(val user: UserData): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (user.nick != "") {
            viewHolder.itemView.chat_textView_username.text = user.nick
        } else {
            viewHolder.itemView.chat_textView_username.text = "undefined"
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_chat_new_message
    }

}