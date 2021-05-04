package com.github.gogetters.letsgo.database

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.getValue

// Db is an optional argument to allow for testing!
class LetsGoUser(val uid: String, val db: Database.Companion = Database) {
    var nick: String? = null
    var first: String? = null
    var last: String? = null
    var city: String? = null
    var country: String? = null

    // Be very careful if changing path values!
    private val tag = "FirestoreTest"
    private val usersPath = "users"
    private val userPath = "$usersPath/$uid"
    private val userFriendsPath = "$userPath/friends"

    /**
     * Verifies if the user exists in our database
     */
    fun requireUserExists(): Task<Unit> {
        return db.readData(userPath).continueWith {
            val userExists = it.result.value != null

            if (userExists) {
                Log.d(tag, "User exists! uid: $uid")
            } else {
                Log.e(tag, "User DOESN'T EXIST! uid: $uid")
                throw IllegalStateException("User DOESN'T EXIST! uid: $uid")
            }
        }
    }

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
        return db.updateData(userPath, userData)
            .addOnSuccessListener {
                Log.d(tag, "LetsGoUser document added for uid: $uid")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error adding LetsGoUser document", e)
            }
    }

    /**
     * Downloads this User's data to the DB. Returns a task to track progress and etc.
     */
    fun downloadUserData(): Task<Unit> {
        return db.readData(userPath)
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
                Log.d(tag, "LetsGoUser successfully downloaded: ${toString()}")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error downloading LetsGoUser for uid: $uid", e)
            }
    }

    /**
     * Deletes this User's data from the DB. Returns a task to track progress and etc.
     */
    fun deleteUserData(): Task<Void> {
        return db.deleteData(userPath)
            .addOnSuccessListener {
                Log.d(tag, "LetsGoUser successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error deleting LetsGoUser", e)
            }
    }

    override fun toString(): String {
        // TODO Maybe improve this?

        return "LetsGoUser : " + super.toString() + "\t" + uid + "\t" + nick + "\t" + first + "\t" + last + "\t" + city + "\t" + country
    }

    //===========================================================================================
    // Friend System

    /* TODO
    * - Load a List<uids:String> into a populated List<LetsGoUsers>
    * - DONE A function that retrieves a list of all uid's in a friend list of a certain status!
    * - MAYBE Do we need a function to check that status of a current friend request (probably)
    * - MAYBE Blocking users?
    * - Whatever else we need?
    */

    enum class FriendStatus {
        /**
         * User has sent a friend request and is awaiting a response
         */
        SENT,

        /**
         * User has received a friend request and can respond by either ignoring or accepting
         */
        REQUESTED,

        /**
         * Users are friends! yay :)
         */
        ACCEPTED
    }

    fun requestFriend(otherUser: LetsGoUser): Task<Void> {
        return updateFriendStatus(otherUser, FriendStatus.SENT, FriendStatus.REQUESTED)
    }

    fun acceptFriend(otherUser: LetsGoUser): Task<Void> {
        return updateFriendStatus(otherUser, FriendStatus.ACCEPTED, FriendStatus.ACCEPTED)
    }

    /**
     * Use to remove a friend or delete a friend request.
     */
    fun deleteFriend(otherUser: LetsGoUser): Task<Void> {
        return db.deleteData("$userFriendsPath/${otherUser.uid}").continueWithTask {
            db.deleteData("${otherUser.userFriendsPath}/${this.uid}")
        }.addOnSuccessListener {
            Log.d(tag, "'Friend' successfully deleted")
        }.addOnFailureListener {
            Log.d(tag, "'Friend' FAILED to be deleted")
        }
    }

    /**
     * Updates friend status for both users!
     */
    private fun updateFriendStatus(
        otherUser: LetsGoUser,
        status1: FriendStatus,
        status2: FriendStatus
    ): Task<Void> {
        return requireUserExists().continueWithTask {
            otherUser.requireUserExists().continueWithTask {
                updateFriendStatusHelper(otherUser, status1).continueWithTask {
                    otherUser.updateFriendStatusHelper(this, status2)
                }
            }
        }.addOnSuccessListener {
            Log.d(tag, "Friend Status successfully updated")
        }.addOnFailureListener {
            Log.d(tag, "Friend Status FAILED to be updated")
        }
    }

    /**
     * Updates otherUsers friend status in this users friend list!
     */
    private fun updateFriendStatusHelper(otherUser: LetsGoUser, status: FriendStatus): Task<Void> {
        val path = "$userFriendsPath/${otherUser.uid}"
        Log.d(tag, "Adding friend data. path: $path\tstatus: $status")

        return db.writeData(path, status.name);
    }

    /**
     * Lists all sent, pending or accepted friends of current user.
     */
    fun listFriendsByStatus(status: FriendStatus): Task<List<String>> {
        return db.readData(userFriendsPath).continueWith {
            val friends: ArrayList<String> = ArrayList()

            for (friendEntry in it.result.children) {

                if (friendEntry.getValue<String>() == status.name) {
                    friends.add(friendEntry.key!!)
                }
            }

            friends
        }
    }
}