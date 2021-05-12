package com.github.gogetters.letsgo.database.images

import android.net.Uri
import com.github.gogetters.letsgo.database.CloudStorage
import java.io.File

/**
 * Allows to store images on the cloud storage provided by Firebase
 */
class ImageStorageService(val storage: CloudStorage.Companion = CloudStorage) {

    /**
     * Stores a picture on the storage service
     * @param uri: uri of the file to store
     * @return the reference (== address) of the image on the storage service
     */
    fun storePicture(uri: Uri): String {
        return ""
    }

    fun downloadPicture(ref: String, localFile: File) {

    }


}