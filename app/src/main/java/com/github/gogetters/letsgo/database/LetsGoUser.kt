package com.github.gogetters.letsgo.database

import android.util.Log
import com.google.android.gms.tasks.Task

// Db is an optional argument to allow for testing!
class LetsGoUser(val uid:String, val db: Database.Companion=Database) {
    var nick: String? = null
    var first: String? = null
    var last: String? = null
    var city: String? = null
    var country: String? = null

    // TODO Maybe find a way to make these constant or rename vars below to be consistent with format
    private val TAG = "FirestoreTest"
    private val USERS_PATH = "users"
    private val USER_PATH = "$USERS_PATH/$uid"

    /**
     * Uploads this User's data to the DB. Returns a task to track progress and etc.
     */
    fun uploadUserData(): Task<Void> {
        val userData = hashMapOf(
            "nick" to nick,
            "first" to first,
            "last" to last,
            "city" to city,
            "country" to country
        )

        // Add a new document with user's uid
        return db.writeData(USER_PATH, userData)
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser document added for uid: $uid")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding LetsGoUser document", e)
            }
    }

    /**
     * Downloads this User's data to the DB. Returns a task to track progress and etc.
     */
    fun downloadUserData(): Task<Unit> {
        return db.readData(USER_PATH)
            .continueWith {
                for (attribute in it.result.children) {
                    when (attribute.key) {
                        "nick" -> nick = attribute.value as String
                        "first" -> first = attribute.value as String
                        "last" -> last = attribute.value as String
                        "city" -> city = attribute.value as String
                        "country" -> country = attribute.value as String
                    }
                }
            }
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser successfully downloaded: ${toString()}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error downloading LetsGoUser for uid: $uid", e)
            }
    }

    /**
     * Deletes this User's data from the DB. Returns a task to track progress and etc.
     */
    fun deleteUserData(): Task<Void> {
        return db.deleteData(USER_PATH)
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting LetsGoUser", e)
            }
    }

    override fun toString(): String {
        // TODO Maybe improve this?

        return "LetsGoUser : " + super.toString() + "\t" + uid + "\t" + nick + "\t" + first + "\t" + last + "\t" + city + "\t" + country
    }
}