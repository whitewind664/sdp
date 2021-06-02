package com.github.gogetters.letsgo.chat.views

import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_my_message.view.*
import kotlinx.android.synthetic.main.item_chat_their_message.view.*

class ChatMessageItem(val chatMessage: ChatMessageData, val id: String) : Item<ViewHolder>() {

    companion object {
        private const val MY_ID = "MY"
        private const val THEIR_ID = "THEIR"
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_textView_my_messageBody.text = chatMessage.text
        if (id.equals(THEIR_ID)) {
            viewHolder.itemView.chat_textView_their_name.text = chatMessage.text
        }
    }

    override fun getLayout(): Int {
        if (id.equals(MY_ID)) {
            return R.layout.item_chat_my_message
        } else {
            return return R.layout.item_chat_their_message
        }
    }

}