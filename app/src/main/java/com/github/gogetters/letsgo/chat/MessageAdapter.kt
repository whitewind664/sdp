package com.github.gogetters.letsgo.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.gogetters.letsgo.R


class MessageAdapter(private var context: Context): BaseAdapter() {
    private var messages: MutableList<ChatMessage> = mutableListOf()

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(i: Int): Any {
        return messages[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }


    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val message = messages[i]
        val messageInflater: LayoutInflater = LayoutInflater.from(context)
        lateinit var newConvertView: View

        if (message.isBelongingToUser()) {
            // show outgoing message
            newConvertView = messageInflater.inflate(R.layout.my_message, null)
            val body: TextView = newConvertView.findViewById(R.id.message_body)
            newConvertView.tag = body
            body.text = message.getText()
        } else {
            // show incoming message
            newConvertView = messageInflater.inflate(R.layout.their_message, null)

            //val avatar: View = newConvertView.findViewById(...)
            val name: TextView = newConvertView.findViewById(R.id.name)
            val body: TextView = newConvertView.findViewById(R.id.message_body)

            name.text = message.getUserName()
            body.text = message.getText()

            /* TODO add avatar
            avatar.getBackground()
                .setColor(Color.parseColor(message.getMemberData().getColor()))*/
        }

        return newConvertView
    }


}