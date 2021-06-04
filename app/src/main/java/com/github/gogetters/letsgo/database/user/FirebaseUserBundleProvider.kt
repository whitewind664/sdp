package com.github.gogetters.letsgo.database.user

import com.github.gogetters.letsgo.database.Authentication
import com.google.firebase.auth.FirebaseUser

object FirebaseUserBundleProvider: UserBundleProvider() {

    private var cachedUserBundle : UserBundle? = null

    override fun getUserBundle(): UserBundle? {
        if (cachedUserBundle != null) return cachedUserBundle
        val firebaseUser: FirebaseUser? = Authentication.getCurrentUser()
        return if (firebaseUser == null) { null
        } else {
            cachedUserBundle = FirebaseUserBundle(firebaseUser)
            return cachedUserBundle
        }
    }
}