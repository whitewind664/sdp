package com.github.gogetters.letsgo.database

import com.github.gogetters.letsgo.database.types.MessageData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        init {
//            Firebase.database.setPersistenceEnabled(true)
        }

        val database = Firebase.database.reference

        // TODO write database functions here

        fun writeValue(path: String, value: String, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
            database.child(path).setValue(value) { error, ref ->
                if (error != null) {
                    onFailure(error)
                } else {
                    onSuccess()
                }
            }
        }

        fun addEventListener(databaseReference: DatabaseReference, onDataChange: (DataSnapshot) -> Unit, onCancelled: (DatabaseError) -> Unit): ValueEventListener {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onDataChange(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onCancelled(error)
                }
            }
            return databaseReference.addValueEventListener(listener)
        }

        fun sendMessage(senderId: String, chatId: String, messageText: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
            val key = database.child("messages").child(chatId).push().key ?: return

            val message = MessageData(messageText, senderId, System.currentTimeMillis())
            val messageValues = message.toMap()

            val childUpdates = hashMapOf<String, Any>(
                    "/messages/$chatId/$key" to messageValues,
                    "/chats/$chatId/lastMessageText" to messageText
            )

            database.updateChildren(childUpdates)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener(onFailure)
        }

        fun addMessagesListener(chatId: String, onChildAdded: (DataSnapshot) -> Unit): ChildEventListener {
            val databaseReference = database.child("messages").child(chatId)
            val listener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    onChildAdded(snapshot)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            return databaseReference.addChildEventListener(listener)
        }

        fun removeMessagesListener(chatId: String, listener: ChildEventListener) {
            val databaseReference = database.child("messages").child(chatId)
            databaseReference.removeEventListener(listener)
        }
    }

}