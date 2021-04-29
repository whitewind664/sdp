package com.github.gogetters.letsgo.map

import com.github.gogetters.letsgo.database.Database
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.CompletableFuture

/**
 * Uses the Firebase database to provide the location sharing service
 */
class DatabaseLocationSharingService(): LocationSharingService() {
    override fun shareMyLocation(location: LatLng): Boolean {
        return Database.shareLocation(location)
    }

    override fun disableLocationSharing() {
        Database.disableLocationSharing()
    }

    override fun getSharedLocations(): CompletableFuture<Map<LatLng, String>> {
        return Database.getAllLocations()
    }
}