package com.github.gogetters.letsgo.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object {
        val database = Firebase.database.reference

        init {
            Firebase.database.setPersistenceEnabled(true)
        }

        // TODO write database functions here

        fun writeValue(path: String, value: String) {
            database.child(path).setValue(value)
        }
    }

}