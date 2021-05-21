package com.github.gogetters.letsgo.map

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.database.EmulatedFirebaseTest
import com.google.android.gms.maps.model.LatLng
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseLocationSharingTest: EmulatedFirebaseTest() {
    private val locationSharingService = DatabaseLocationSharingService()

    @Test // this behavior can be forbidden later
    fun ownSharedLocationIsRetrieved() {
        val location = LatLng(2.3, 3.2)
        locationSharingService.shareMyLocation(location)
        locationSharingService.getSharedLocations().thenAccept {
            assertTrue(it.containsKey(location))
        }
    }

    @Test
    fun unsharedLocationIsNotShown() {
        val location = LatLng(2.3, 3.2)
        locationSharingService.shareMyLocation(location)
        locationSharingService.disableLocationSharing()
        locationSharingService.getSharedLocations().thenAccept {
            assertFalse(it.containsKey(location))
        }
    }

}