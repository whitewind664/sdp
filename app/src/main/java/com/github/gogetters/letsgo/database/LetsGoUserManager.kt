package com.github.gogetters.letsgo.database

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

object LetsGoUserManager {
    private const val TAG = "FirestoreTest"
    private val db by lazy { Firebase.firestore }

    init {
        db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    private const val USERS_COLLECTION_PATH = "Users"

    /**
     * Call this after a successful FirebaseAuth to get your LetsGoUser instance.
     */
    fun currentUser(): Task<LetsGoUser> {
        val fbUser = FirebaseAuth.getInstance().currentUser

        if (fbUser == null) {
            val msg = "Login with FirebaseAuth before calling this!"
            Log.e(TAG, msg)
            throw IllegalStateException(msg)
        }

        return downloadUserData(fbUser)
    }

    /**
     * Uploads (additional) user data to the Firestore database. Call this after
     * modifying a LetsGoUser instance to save changes on the database.
     */
    fun uploadUserData(user: LetsGoUser): Task<Void> {
        // TODO Add all user components and whatnot!

        val userData = hashMapOf(
            "nick" to user.nick,
            "first" to user.first,
            "last" to user.last,
            "city" to user.city,
            "country" to user.country
        )

        // Add a new document with user's uid
        return db.collection(USERS_COLLECTION_PATH).document(user.fb.uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser document added for uid: ${user.fb.uid}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding LetsGoUser document", e)
            }
    }

    /**
     * Downloads (additional) user data from the Firestore database
     */
    private fun downloadUserData(fbUser: FirebaseUser): Task<LetsGoUser> {
        // TODO Add all user components and whatnot!

        return db.collection(USERS_COLLECTION_PATH).document(fbUser.uid).get()
            .continueWith { x ->
                val data = x.result.data

                val user = LetsGoUser(fbUser)
                if (data != null) {
                    user.nick = data["nick"] as String?
                    user.first = data["first"] as String?
                    user.last = data["last"] as String?
                    user.city = data["city"] as String?
                    user.country = data["country"] as String?
                }

                user
            }
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser successfully downloaded for uid: ${fbUser.uid}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error downloading LetsGoUser for uid: ${fbUser.uid}", e)
            }
    }

    /**
     * Deletes User from both the Firestore database and FirebaseAuth.
     */
    fun deleteUser(user: LetsGoUser): Task<Void> {
        return db.collection(USERS_COLLECTION_PATH).document(user.fb.uid).delete()
            .continueWithTask { user.fb.delete() }
            .addOnSuccessListener {
                Log.d(TAG, "LetsGoUser and FirebaseUser successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting LetsGoUser and/or FirebaseUser", e)
            }
    }
}
