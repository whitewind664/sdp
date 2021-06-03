package com.github.gogetters.letsgo.database

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Authentication {
    private val auth = Firebase.auth

    fun emulatorSettings() {
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        auth.useEmulator("10.0.2.2", 9099)
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }

    fun getUid(): String? {
        return auth.uid
    }
}