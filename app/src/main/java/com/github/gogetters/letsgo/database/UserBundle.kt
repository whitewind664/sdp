package com.github.gogetters.letsgo.database

import com.google.firebase.auth.FirebaseUser

/**
 * This is the simplest way to handle FirebaseUser and our own LetsGoUser together.
 */
class UserBundle(val firebase: FirebaseUser) {

    val letsGo = LetsGoUser(firebase.uid)

    fun deleteUser() {
        letsGo.deleteUserData().continueWith { firebase.delete() }
    }
}