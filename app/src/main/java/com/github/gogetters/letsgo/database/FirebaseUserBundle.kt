package com.github.gogetters.letsgo.database

import com.google.firebase.auth.FirebaseUser

/**
 * This is the simplest way to handle FirebaseUser and our own LetsGoUser together.
 */
class FirebaseUserBundle(private val firebase: FirebaseUser): UserBundle() {

    private val letsGoUser = LetsGoUser(firebase.uid)

    override fun getUser(): LetsGoUser {
        return letsGoUser
    }

    override fun getEmail(): String {
        return firebase.email
    }

    override fun deleteUser() {
        letsGoUser.deleteUserData().continueWith { firebase.delete() }
    }
}