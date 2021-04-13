package com.github.gogetters.letsgo.database

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


        fun writeValue(path: String, value: String) {
            database.child(path).setValue(value)
        }

        private fun getCurrentUserId(): String? {
            val user = FirebaseAuth.getInstance().currentUser ?: return null
            return user.uid
        }
    }

}