package com.github.gogetters.letsgo.database.user

/**
 * Used to make mocking of FirebaseUserBundle possible
 */
abstract class UserBundle {

    abstract fun getUser(): LetsGoUser

    abstract fun  getEmail(): String

    abstract fun deleteUser()
}