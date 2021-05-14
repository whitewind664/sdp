package com.github.gogetters.letsgo.activities.mocking

import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.github.gogetters.letsgo.database.user.UserBundle

class MockUserBundle: UserBundle() {
    override fun getUser(): LetsGoUser {
        return LetsGoUser("0")
    }

    override fun getEmail(): String {
        return "mock@epfl.ch"
    }

    override fun deleteUser() {

    }
}