package com.github.gogetters.letsgo.database

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        val database = Firebase.database.reference

        init {
            Firebase.database.setPersistenceEnabled(true)
        }

        // TODO write database functions here
        // ---- Map related ----
        /**
         * Activates the location sharing and sends location to database
         */
        fun shareLocation(location: LatLng) {

        }

        /**
         * Stops the displaying of the position with other users (not looking for a new game anymore)
         */
        fun disableLocationSharing() {

        }

        /**
         * Retrieves all locations except the own one
         */
        fun getAllLocations(): Map<LatLng, String> {
            database.child("users").get()
            return emptyMap()
        }
        // ---- [end] Map related ----

        fun writeValue(path: String, value: String) {
            database.child(path).setValue(value)
        }
    }

}