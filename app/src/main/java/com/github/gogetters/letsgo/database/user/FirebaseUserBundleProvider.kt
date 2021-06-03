package com.github.gogetters.letsgo.database.user

import com.github.gogetters.letsgo.database.Authentication
import com.google.firebase.auth.FirebaseUser

object FirebaseUserBundleProvider: UserBundleProvider() {

    override fun getUserBundle(): UserBundle? {
        val firebaseUser: FirebaseUser? = Authentication.getCurrentUser()
        return if (firebaseUser == null) {
            null
        } else {
            FirebaseUserBundle(firebaseUser)
        }
    }
}