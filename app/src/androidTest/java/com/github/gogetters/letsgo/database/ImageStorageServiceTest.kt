package com.github.gogetters.letsgo.database

import android.net.Uri
import android.widget.ImageView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.user.LetsGoUser
import com.google.firebase.storage.FileDownloadTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.CompletableFuture

@RunWith(AndroidJUnit4::class)
class ImageStorageServiceTest {

    @Test
    fun getProfilePictureDoesNotCallCloudWithNullRef() {
        mockkObject(CloudStorage)
        val ref: String? = null
        val imageView = mockk<ImageView>()
        val file = mockk<File>()

        ImageStorageService.getProfileImageFromCloud("", ref, file, imageView)
        verify(exactly = 0) { CloudStorage.downloadFile("", file) }
    }

    @Test
    fun getProfilePictureCallsCloudWithNonNullRef() {
        mockkObject(CloudStorage)
        val ref: String = "Hello"
        val imageView = mockk<ImageView>()
        val file = mockk<File>()

        every { CloudStorage.downloadFile(ref, file) } returns CompletableFuture()

        ImageStorageService.getProfileImageFromCloud("", ref, file, imageView)
        verify(exactly = 1) { CloudStorage.downloadFile(ref, file) }
    }

    @Test
    fun storeProfileImageCallsStoreFunction() {
        mockkObject(CloudStorage)
        mockkObject(Database)
        val uid = "1"
        val user = LetsGoUser(uid)
        val uri = mockk<Uri>()
        val ref = "Test"
        every { uri.lastPathSegment } returns ref
        every { CloudStorage.uploadFile(ref, uri) } returns CompletableFuture()
        ImageStorageService.storeProfileImageOnCloud(user, uri, "")
        verify(exactly = 1) { CloudStorage.uploadFile(ref, uri) }
    }
}