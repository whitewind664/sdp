package com.github.gogetters.letsgo.chat.views

import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.chat.model.ChatMessageData
import com.github.gogetters.letsgo.chat.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_last_message.view.*

class ChatLastMessageItem(val chatMessage: ChatMessageData): Item<ViewHolder>() {

    var chatUser: UserData? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_textView_last_message.text = chatMessage.text

        val chatId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatId = chatMessage.toId
        } else {
            chatId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatUser = snapshot.getValue(UserData::class.java)
                viewHolder.itemView.chat_textView_last_username.text = chatUser?.nick
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    override fun getLayout(): Int {
        return R.layout.item_chat_last_message
    }

}
