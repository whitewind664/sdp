package com.github.gogetters.letsgo.database

import com.google.firebase.auth.FirebaseUser

class UserBundle(val firebase: FirebaseUser) {

    val letsGo = LetsGoUser(firebase.uid)

    fun deleteUser() {
        letsGo.deleteUserData().continueWith { firebase.delete() }
    }
}