package com.github.gogetters.letsgo.database

import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import com.github.gogetters.letsgo.activities.ProfileActivity
import com.github.gogetters.letsgo.database.user.LetsGoUser
import java.io.File

class ImageStorageService {

    /**
     * Stores a profile image on the cloud.
     */
    fun storeProfileImageOnCloud(user: LetsGoUser, pictureUri: Uri, referencePrefix: String) {
        val ref = pictureUri.lastPathSegment
        CloudStorage.uploadFile("$referencePrefix${ref}", pictureUri).addOnSuccessListener {
            // delete old profile picture
            if (user.profileImageRef != null) {
                CloudStorage.deleteFile(ProfileActivity.PROFILE_PICTURE_PREFIX_CLOUD + user.profileImageRef!!)
            }
            // store new profile picture
            user.profileImageRef = referencePrefix + ref
            user.uploadUserData()
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    /**
     *  Tries to download a profile picture from the cloud and stores it in the provided file and
     *  in the provided ImageView on success.
     */
    fun getProfileImageFromCloud(ref: String?, file: File, profileImage: ImageView) {
        if (ref != null) {
            CloudStorage.downloadFile("${ProfileActivity.PROFILE_PICTURE_PREFIX_CLOUD}$ref", file).addOnSuccessListener {
                profileImage.setImageURI(file.toUri())
            }
        }
    }


}