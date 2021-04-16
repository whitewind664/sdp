package com.github.gogetters.letsgo.database

import com.github.gogetters.letsgo.database.types.MessageData
import com.google.firebase.database.*
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture


class Database {
    companion object {

        init {
            Firebase.database.setPersistenceEnabled(true)
        }

        val database = Firebase.database.reference

        // TODO write database functions here
        
        // ---- Map related ----
        /**
         * Activates the location sharing and sends location to database.
         * Returns true when the data has been sent to the database
         */
        fun shareLocation(location: LatLng): Boolean {
            val uid = getCurrentUserId() ?: return false
            val userRef = database.child("users").child(uid)
            userRef.child("isLookingForPlayers").setValue(true)
            userRef.child("lastPositionLatitude").setValue(location.latitude)
            userRef.child("lastPositionLongitude").setValue(location.longitude)
            Log.i("DB","Shared")
            return true
        }

        /**
         * Stops the displaying of the position with other users (not looking for a new game anymore)
         */
        fun disableLocationSharing(): Boolean {
            val uid = getCurrentUserId() ?: return false
            database.child("users").child(uid).child("isLookingForPlayers").setValue(false)
            return true
        }

        /**
         * Retrieves all locations except the own one
         */
        fun getAllLocations(): CompletableFuture<Map<LatLng, String>> {
            val future = CompletableFuture<Map<LatLng, String>>()
            database.child("users").get().addOnSuccessListener {
                // unpack the values
                var map: Map<LatLng, String> = emptyMap()
                for(user: DataSnapshot in it.children) {
                    val userId: String = user.key as String

                    var lat: Double = 0.0
                    var lng: Double = 0.0
                    var isActive: Boolean = false
                    for (attribute in user.children) {
                        when (attribute.key) {
                            "isLookingForPlayers" -> isActive = attribute.value as Boolean
                            "lastPositionLatitude" -> lat = attribute.value as Double
                            "lastPositionLongitude" -> lng = attribute.value as Double
                        }
                    }
                    Log.i("DB", "Values: $userId, $isActive, $lat, $lng")
                    if (isActive) {
                        // TODO check that its not me
                        map = map + Pair(LatLng(lat, lng), userId)
                    }
                }
                future.complete(map)
            }.addOnFailureListener {
                future.completeExceptionally(DatabaseException())
            }
            return future
        }
        // ---- [end] Map related ----


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

        private fun getCurrentUserId(): String? {
            val user = FirebaseAuth.getInstance().currentUser ?: return null
            return user.uid
        }
    }

}