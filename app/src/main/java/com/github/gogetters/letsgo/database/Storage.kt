package com.github.gogetters.letsgo.database

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import java.io.File

interface Storage {
    /**
     * Allows to upload a local file to the cloud storage.
     * @param ref: The path where we want to store the file on storage
     * @param uri: the uri of the local file
     */
    fun uploadFile(ref: String, uri: Uri): UploadTask

    /**
     * Tries to download a file from the storage and store it in the given local file.
     */
    fun downloadFile(ref: String, localFile: File): FileDownloadTask

    /**
     * Deletes the file of the given reference
     */
    fun deleteFile(ref: String): Task<Void>
}