package com.github.gogetters.letsgo.database

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.CompletionException

@RunWith(AndroidJUnit4::class)
class CloudStorageTest : EmulatedFirebaseTest() {

    @get:Rule
    var exceptionRule: ExpectedException = ExpectedException.none()

    @Test
    fun testUploadFile() {
        exceptionRule.expect(CompletionException::class.java)
        CloudStorage.uploadFile("/test", Uri.EMPTY).join()
    }

    @Test
    fun testDownloadFile() {
        exceptionRule.expect(CompletionException::class.java)
        CloudStorage.downloadFile("/test", File("test.txt")).join()
    }

    @Test
    fun testDeleteFile() {
        exceptionRule.expect(CompletionException::class.java)
        CloudStorage.deleteFile("test.txt").join()
    }
}