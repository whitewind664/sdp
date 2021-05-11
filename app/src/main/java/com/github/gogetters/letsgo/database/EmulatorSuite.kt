package com.github.gogetters.letsgo.database

import com.google.firebase.database.FirebaseDatabase

class EmulatorSuite {
    companion object {

        fun emulatorSettings() {
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            val database = FirebaseDatabase.getInstance()
            database.useEmulator("10.0.2.2", 9000)
        }

        fun flushRealtimeDatabase(database: FirebaseDatabase) {
            // With a DatabaseReference, write null to clear the database.
            database.reference.setValue(null)
        }
    }

}