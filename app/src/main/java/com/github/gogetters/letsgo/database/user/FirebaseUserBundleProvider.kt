package com.github.gogetters.letsgo.database.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserBundleProvider: UserBundleProvider() {
    override fun getUserBundle(): UserBundle? {
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return if (firebaseUser == null) {
            null
        } else {
            return FirebaseUserBundle(firebaseUser)
        }
    }
}