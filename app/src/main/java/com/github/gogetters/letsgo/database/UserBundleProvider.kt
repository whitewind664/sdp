package com.github.gogetters.letsgo.database

import java.io.Serializable

/**
 * This class has been created to enable testing of code that contains a LetsGoUser
 * without actually connecting to the database. This can be done by writing a mock
 * subclass to this class.
 */
abstract class UserBundleProvider: Serializable {
    abstract fun getUserBundle(): UserBundle?
}
