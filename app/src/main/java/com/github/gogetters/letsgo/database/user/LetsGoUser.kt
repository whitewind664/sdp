package com.github.gogetters.letsgo.database.user

import android.util.Log
import com.github.gogetters.letsgo.database.CloudStorage
import com.github.gogetters.letsgo.database.Database
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * Class that represents a user of the app and coordinates the communication of user data with the
 * database. The optional parameters are for testing purposes
 */
class LetsGoUser (val uid: String) : Serializable {
    var nick: String? = null
    var first: String? = null
    var last: String? = null
    var city: String? = null
    var country: String? = null

    // The reference (== address) of the profile picture on cloud storage
    var profileImageRef: String? = null

    var friends: EnumMap<FriendStatus, MutableList<LetsGoUser>>? = null

    // map related values
    var isLookingForPlayers: Boolean? = false
    var lastPositionLatitude: Double? = null
    var lastPositionLongitude: Double? = null

    // Be very careful if changing path values!
    private val tag = "FirestoreTest"
    private val usersPath = "users"
    private val userPath = "$usersPath/$uid"
    private val userFriendsPath = "$userPath/friends"

    /**
     * Verifies if the user exists in our database
     */
    fun requireUserExists(): Task<Unit> {
        return Database.readData(userPath).continueWith {
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
            "id" to uid,
            "nick" to nick,
            "first" to first,
            "last" to last,
            "city" to city,
            "country" to country,
            "profilePictureRef" to profileImageRef,
            "isLookingForPlayers" to isLookingForPlayers,
            "lastPositionLatitude" to lastPositionLatitude,
            "lastPositionLongitude" to lastPositionLongitude
        )

        // Add a new document with user's uid
        return Database.updateData(userPath, userData)
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
        return Database.readData(userPath)
            .continueWith {
                extractUserData(it.result)
            }
            .addOnSuccessListener {
                Log.d(tag, "LetsGoUser successfully downloaded: ${toString()}")
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error downloading LetsGoUser for uid: $uid", e)
            }
    }

    private fun extractUserData(userData: DataSnapshot) {
        for (attribute in userData.children) {
            when (attribute.key) {
                "nick" -> nick = attribute.value as String
                "first" -> first = attribute.value as String
                "last" -> last = attribute.value as String
                "city" -> city = attribute.value as String
                "country" -> country = attribute.value as String
                "profilePictureRef" -> profileImageRef = attribute.value as String
                "isLookingForPlayers" -> isLookingForPlayers = attribute.value as Boolean
                "lastPositionLatitude" -> lastPositionLatitude = attribute.value as Double
                "lastPositionLongitude" -> lastPositionLongitude = attribute.value as Double
            }
        }
    }

    /**
     * Deletes this User's data from the DB. Returns a task to track progress and etc.
     */
    fun deleteUserData(): Task<Void> {
        // delete the profile picture
        if (profileImageRef != null) {
            CloudStorage.deleteFile(profileImageRef!!)
        }
        return Database.deleteData(userPath)
            .addOnSuccessListener {
                Log.d(tag, "LetsGoUser successfully deleted!")
                nick = null
                first = null
                last = null
                city = null
                country = null
                isLookingForPlayers = null
                lastPositionLongitude = null
                lastPositionLatitude = null
                profileImageRef = null
            }
            .addOnFailureListener { e ->
                Log.w(tag, "Error deleting LetsGoUser", e)
            }
    }

    override fun toString(): String {
        return "LetsGoUser(uid=$uid, nick=$nick, first=$first, last=$last, city=$city, country=$country, profileImageRef=$profileImageRef, isLookingForPlayers=$isLookingForPlayers," +
                "lastPositionLatitude=$lastPositionLatitude, lastPositionLongitude=$lastPositionLongitude)"
    }

    //===========================================================================================
    // Friend System


    /* MAYBE
    * - Do we need a function to check that status of a current friend request (probably)
    * - Blocking users?
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
        return Database.deleteData("$userFriendsPath/${otherUser.uid}").continueWithTask {
            Database.deleteData("${otherUser.userFriendsPath}/${this.uid}")
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

        return Database.writeData(path, status.name);
    }

    //-------------------------------------------------------------------------------------------
    // Listing friends of a user

    /**
     * List pending friends, sent friend requests or current friends of User!
     */
    fun listFriendsByStatus(status: FriendStatus): List<LetsGoUser> {
        if (friends == null) {
            throw IllegalStateException(
                "MUST call downloadFriends and wait for it to complete" +
                        " before calling this function!"
            )
        }
        return friends!![status]!!
    }

    /**
     * Downloads Friends of all statuses.
     */
    // Sometimes I use the word connections and friends interchangeably
    fun downloadFriends(): Task<Void> {
        return Database.readData(userFriendsPath).continueWithTask {
            val friendUids: EnumMap<FriendStatus, ArrayList<String>> =
                EnumMap(FriendStatus::class.java)
            friends = EnumMap(FriendStatus::class.java)

            // Initializing lists in connections
            for (friendStatus in FriendStatus.values()) {
                friendUids[friendStatus] = ArrayList()
                friends!![friendStatus] = ArrayList()
            }

            // Putting connection uids into their respective list
            for (connectionData in it.result.children) {
                val friendStatus = FriendStatus.valueOf(connectionData.value as String)
                val uid = connectionData.key

                friendUids[friendStatus]!!.add(uid!!)
            }

            // Turning a list of uids into a list of downloaded LetsGoUsers
            val tasks = ArrayList<Task<*>>()
            for (friendStatus in FriendStatus.values()) {
                tasks.add(
                    downloadUserList(friendUids[friendStatus]!!).continueWith {
                        friends!![friendStatus] = it.result
                    }
                )
            }
            Tasks.whenAll(tasks)
        }
    }

    /**
     * Given a list of uids. Creates a list of LetsGoUsers and downloads their data
     */
    fun downloadUserList(uids: List<String>): Task<MutableList<LetsGoUser>> {
        val tasks = ArrayList<Task<LetsGoUser>>()

        for (uid in uids) {
            val user = LetsGoUser(uid)
            tasks.add(user.downloadUserData().continueWith { user })
        }

        return Tasks.whenAllSuccess(tasks)
    }


    //-------------------------------------------------------------------------------------------
    // User Search

    fun downloadUsersByNick(nick: String): Task<MutableList<LetsGoUser>> {
        return Database.readSearchByChild(usersPath, "nick", nick).continueWithTask {
            downloadUserList(it.result.children.map { it.key!! }.toList())
        }
    }
}