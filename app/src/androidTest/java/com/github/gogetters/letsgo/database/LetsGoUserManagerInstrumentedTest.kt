package com.github.gogetters.letsgo.database

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
//class LetsGoUserManagerInstrumentedTest {
//
//    private val TAG = "FirestoreTest"
//    private val db = Firebase.firestore
//
//    @Test
//    fun testUploadUserData() {
//        val user = Tasks.await(LetsGoUserManager.currentUser())
//
//        user.nick = "metaTinker"
//        user.first = "Michael"
//        user.last = "Roust"
//        user.city = "Lausanne"
//        user.country = "Switzerland"
//
//        Tasks.await(LetsGoUserManager.uploadUserData(user))
//    }
//
//    @Test
//    fun testDownloadUserData() {
//        val user = Tasks.await(LetsGoUserManager.currentUser())
//
//        Log.d(TAG, "SUCCESS testDownloadUserData : $user")
//    }
//}