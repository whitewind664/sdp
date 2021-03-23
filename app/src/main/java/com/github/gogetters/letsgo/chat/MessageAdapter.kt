package com.github.gogetters.letsgo.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.gogetters.letsgo.R

class MessageAdapter(private var context: Context): BaseAdapter() {
    // TODO change to type Message instead of String
    private var messages: MutableList<String> = mutableListOf()

    fun addMessage(message: String) {
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

        // show outgoing message
        val convertView = messageInflater.inflate(R.layout.my_message, null)
        val body: TextView = convertView.findViewById(R.id.message_body)
        convertView.tag = body
        body.text = message

        return convertView
    }


}