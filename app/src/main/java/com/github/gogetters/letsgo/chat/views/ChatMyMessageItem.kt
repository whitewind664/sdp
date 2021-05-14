package com.github.gogetters.letsgo.chat.views

import com.github.gogetters.letsgo.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_my_message.view.*

class ChatMyMessageItem(val text: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_textView_my_messageBody.text = text
    }

    override fun getLayout(): Int {
        return R.layout.item_chat_my_message
    }

}