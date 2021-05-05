package com.github.gogetters.letsgo.database

import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Class that interacts with the Cloud Storage for Android Service. This is used to store large
 * files, e.g., pictures.
 */
class CloudStorage {
    companion object {
        private val storage = Firebase.storage

        private val ONE_MEGABYTE = 1024L * 1024L

        fun uploadFile(ref: String, stream: InputStream): UploadTask {
            return storage.getReference(ref).putStream(stream)
        }

        /**
         * Tries to download a file from the storage and store it in the given local file.
         */
        fun downloadFile(ref: String, localFile: File): FileDownloadTask {
            
            return storage.getReference(ref).getFile(localFile)
        }
    }
}