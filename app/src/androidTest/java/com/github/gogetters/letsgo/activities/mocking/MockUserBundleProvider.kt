package com.github.gogetters.letsgo.activities.mocking

import com.github.gogetters.letsgo.database.UserBundle
import com.github.gogetters.letsgo.database.UserBundleProvider

class MockUserBundleProvider: UserBundleProvider() {
    override fun getUserBundle(): UserBundle? {
        return MockUserBundle()
    }
}