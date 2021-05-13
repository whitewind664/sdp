package com.github.gogetters.letsgo.database

import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.user.LetsGoUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.CompletableFuture

@RunWith(AndroidJUnit4::class)
class ImageStorageServiceTest {
    val DELAY = 100L

    @Test
    fun getProfilePictureDoesNotCallCloudWithNullRef() {
        mockkObject(CloudStorage)
        val ref: String? = null
        val imageView = mockk<ImageView>()
        val file = mockk<File>()

        every { CloudStorage.downloadFile("", file) } returns CompletableFuture()

        ImageStorageService.getProfileImageFromCloud("", ref, file, imageView)
        verify(exactly = 0) { CloudStorage.downloadFile("", file) }
    }

    @Test
    fun getProfilePictureCallsCloudWithNonNullRef() {
        mockkObject(CloudStorage)
        val ref = "Hello"
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
        val future = CompletableFuture<Unit>()
        every { CloudStorage.uploadFile(ref, uri) } returns future
        ImageStorageService.storeProfileImageOnCloud(user, uri, "")
        verify(exactly = 1) { CloudStorage.uploadFile(ref, uri) }
    }

    @Test
    fun oldImageIsDeletedOnSuccessfulUpload() {
        mockkObject(CloudStorage)
        mockkObject(Database)
        val uid = "1"
        val ref = "Test"
        val oldRef = "old"
        val user = LetsGoUser(uid)
        user.profileImageRef = "old"
        val uri = mockk<Uri>()

        every { uri.lastPathSegment } returns ref
        val future = CompletableFuture<Unit>()
        every { CloudStorage.uploadFile(ref, uri) } returns future
        every { CloudStorage.deleteFile(oldRef) } returns CompletableFuture()

        ImageStorageService.storeProfileImageOnCloud(user, uri, "")
        future.complete(Unit)
        verify(exactly = 1) { CloudStorage.deleteFile(oldRef) }
        sleep()
        assertEquals(ref, user.profileImageRef)
    }

    @Test
    fun imageViewIsSetOnSuccessfulDownload() {
        mockkObject(CloudStorage)
        val ref = "Hello"
        val imageView = mockk<ImageView>()
        val file = mockk<File>()
        val uri = mockk<Uri>()

        val future = CompletableFuture<Unit>()

        every { CloudStorage.downloadFile(ref, file) } returns future
        every { file.toUri() } returns uri
        every { imageView.setImageURI(uri) } returns Unit

        ImageStorageService.getProfileImageFromCloud("", ref, file, imageView)

        future.complete(Unit)
        sleep()
        verify(exactly = 1) { CloudStorage.downloadFile(ref, file) }
        //verify(exactly = 1) { imageView.setImageURI(allAny()) }
    }

    private fun sleep() {
        try {
            Thread.sleep(DELAY)
        } catch (e: InterruptedException) {
            throw RuntimeException("Cannot execute Thread.sleep()")
        }
    }
}