package com.github.gogetters.letsgo.database

import com.github.gogetters.letsgo.database.types.MessageData
import com.google.firebase.database.*
import android.util.Log
import com.github.gogetters.letsgo.database.types.GameData
import com.github.gogetters.letsgo.matchmaking.Matchmaking
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.util.concurrent.CompletableFuture


class Database {
    companion object {

        init {
            //Firebase.database.setPersistenceEnabled(true)
        }

        private val db = Firebase.database
        private val database by lazy {
            db.reference
        }

        var isEmulated = false
            get() = field

        // TODO write database functions here

        fun emulatorSettings() {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            db.useEmulator("10.0.2.2", 9000)
            isEmulated = true
        }

        fun flushRealtimeDatabase() {
            // With a DatabaseReference, write null to clear the database.
            // NEVER USE IN PRODUCTION
            if (isEmulated) {
                Tasks.await(db.reference.setValue(null))
            }
        }

        fun writeData(ref: String, data:Any?): Task<Void> {
            return db.getReference(ref).setValue(data)
        }

        fun readData(ref: String): Task<DataSnapshot> {
            return db.getReference(ref).get()
        }

        fun updateData(ref: String, data: Map<String, Any?>): Task<Void> {
            return db.getReference(ref).updateChildren(data)
        }

        fun deleteData(ref: String): Task<Void> {
            return db.getReference(ref).removeValue()
        }

        fun readSearchByChild(ref: String, childName: String, queryText: String): Task<DataSnapshot> {
            return db.getReference(ref).orderByChild(childName).startAt(queryText).endAt(queryText+"\uf8ff").get()
        }

//         fun enableCache() {
//            Firebase.database.setPersistenceEnabled(true)
//         }

//        fun keepSynced(ref: String) {
//            Firebase.database.getReference(ref).keepSynced(true)
//        }

        // ---- [START} Matchmaking  ----


        fun findMatch(
                playerId: String,
                bucket: Int,
                ranked: Boolean,
                onComplete: (DatabaseError?,
                             Boolean,
                             DataSnapshot?) -> Unit
        ) {
            database.child("matchmaking")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val gameType = if (ranked) "ranked" else "unranked"
                        val p = currentData.child("currentlyWaiting")
                                .child(gameType).child(bucket.toString())
                                .getValue(String::class.java)
                        if (p == playerId) {
                            return Transaction.success(currentData);
                        }
                        if (p == null) {
                            currentData.child("currentlyWaiting")
                                    .child(gameType).child(bucket.toString())
                                    .value = playerId
                            Matchmaking.queuePath = "/matchmaking/currentlyWaiting/$gameType/$bucket"
                            Matchmaking.isSearching = true
                        } else {
                            val gameData = GameData(p, playerId, ranked)
                            currentData.child("currentlyWaiting")
                                    .child(gameType).child(bucket.toString())
                                    .value = null
                            val gameId = database.child("matchmaking").child("games").push().key
                            currentData.child("games").child("$gameId").value = gameData
                            currentData.child("currentGamesPerUser").child(gameData.player1).value = gameId
                            currentData.child("currentGamesPerUser").child(gameData.player2).value = gameId

                            Matchmaking.queuePath = null
                            Matchmaking.isSearching = false
                        }
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (error != null) {
                            Log.d("Database::findMatch", error.toString())
                        }
                        onComplete(error, committed, currentData)
                    }
                })
        }


        // ---- [END] Matchmaking ----

        // ---- Map related ----

        /**
         * Retrieves all locations except the own one
         */
        fun getAllLocations(): CompletableFuture<Map<LatLng, String>> {
            val future = CompletableFuture<Map<LatLng, String>>()
            database.child("users").get().addOnSuccessListener {
                // unpack the values
                var map: Map<LatLng, String> = emptyMap()
                val myUserId = Authentication.getUid()
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
                    if (isActive && userId != myUserId) {
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

        fun addEventListener(path: String, onDataChange: (DataSnapshot) -> Unit, onCancelled: (DatabaseError) -> Unit): ValueEventListener {
            val databaseReference = db.getReference(path)
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

        fun removeEventListener(path: String, listener: ValueEventListener) {
            db.getReference(path).removeEventListener(listener)
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

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onCancelled(error: DatabaseError) {}
            }
            return databaseReference.addChildEventListener(listener)
        }

        fun removeMessagesListener(chatId: String, listener: ChildEventListener) {
            val databaseReference = database.child("messages").child(chatId)
            databaseReference.removeEventListener(listener)
        }
    }

}