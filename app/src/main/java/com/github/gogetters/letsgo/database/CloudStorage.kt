package com.github.gogetters.letsgo.database

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.concurrent.CompletableFuture


/**
 * Class that interacts with the Cloud Storage for Android Service. This is used to store large
 * files, e.g., pictures.
 * For explanations of how Cloud Storage works, see https://firebase.google.com/docs/storage/android/start
 */
class CloudStorage {
    companion object {
        private val storage = Firebase.storage

        /**
         * Allows to upload a local file to the cloud storage.
         * @param ref: The path where we want to store the file on storage
         * @param uri: the uri of the local file
         */
        fun uploadFile(ref: String, uri: Uri): CompletableFuture<Unit> {
            val future = CompletableFuture<Unit>()
            storage.getReference(ref).putFile(uri).addOnSuccessListener { future.complete(Unit) }.addOnFailureListener {future.completeExceptionally(DatabaseException())}
            return future
        }

        /**
         * Tries to download a file from the storage and store it in the given local file.
         */
        fun downloadFile(ref: String, localFile: File): CompletableFuture<Unit> {
            val future = CompletableFuture<Unit>()
            storage.getReference(ref).getFile(localFile).addOnSuccessListener { future.complete(Unit) }.addOnFailureListener { future.completeExceptionally(DatabaseException())}
            return future
        }

        /**
         * Deletes the file of the given reference
         */
        fun deleteFile(ref: String): CompletableFuture<Unit> {
            val future = CompletableFuture<Unit>()
            storage.getReference(ref).delete().addOnSuccessListener { future.complete(Unit) }.addOnFailureListener { future.completeExceptionally(DatabaseException()) }
            return future
        }

        fun convertTaskToFuture(task: Task<Any>) {

        }
    }
}