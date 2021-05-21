package com.github.gogetters.letsgo.database.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseUserBundleProvider: UserBundleProvider() {

    private var cachedUserBundle : UserBundle? = null

    override fun getUserBundle(): UserBundle? {
        if (cachedUserBundle != null) return cachedUserBundle
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        return if (firebaseUser == null) {
            null
        } else {
            cachedUserBundle = FirebaseUserBundle(firebaseUser)
            return cachedUserBundle
        }
    }
}