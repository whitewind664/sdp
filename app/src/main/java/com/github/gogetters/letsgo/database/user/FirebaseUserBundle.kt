package com.github.gogetters.letsgo.database.user

import com.google.android.gms.tasks.Tasks
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
        return firebase.email!!
    }

    override fun deleteUser() {
        Tasks.await(letsGoUser.deleteUserData())
        Tasks.await(firebase.delete())
    }
}