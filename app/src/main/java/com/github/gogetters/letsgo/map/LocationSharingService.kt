package com.github.gogetters.letsgo.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.CompletableFuture

/**
 * A class that is responsible for sharing the location between users.
 * Works as an interface between the MapsActivity and the backend.
 */
interface LocationSharingService {

    /**
     * Share the location with the database
     */
    fun shareMyLocation(location: LatLng): Boolean

    /**
     * Disables the location sharing
     */
    fun disableLocationSharing()

    /**
     * Get all the recent shared location by other users from the database
     */
    fun getSharedLocations(): CompletableFuture<Map<LatLng, String>>
}