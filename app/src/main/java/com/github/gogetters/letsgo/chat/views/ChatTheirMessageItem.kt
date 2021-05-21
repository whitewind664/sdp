package com.github.gogetters.letsgo.chat.views

import com.github.gogetters.letsgo.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_their_message.view.*

class ChatTheirMessageItem(val text: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_textView_their_name.text = "From Name"
        viewHolder.itemView.chat_textView_their_messageBody.text = text
    }

    override fun getLayout(): Int {
        return R.layout.item_chat_their_message
    }

}