package com.github.gogetters.letsgo.activities.mocking

import com.github.gogetters.letsgo.database.user.UserBundle
import com.github.gogetters.letsgo.database.user.UserBundleProvider

class MockUserBundleProvider: UserBundleProvider() {
    override fun getUserBundle(): UserBundle? {
        return MockUserBundle()
    }
}