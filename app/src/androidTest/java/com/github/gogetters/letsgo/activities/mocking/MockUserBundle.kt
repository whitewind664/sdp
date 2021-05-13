package com.github.gogetters.letsgo.activities.mocking

import com.github.gogetters.letsgo.database.LetsGoUser
import com.github.gogetters.letsgo.database.UserBundle

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