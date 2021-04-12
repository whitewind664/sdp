package com.github.gogetters.letsgo.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 * A class that is responsible for sharing the location between users.
 * Works as an interface between the MapsActivity and the backend.
 */
abstract class LocationSharingService {

    /**
     * Share the location with the database
     */
    abstract fun shareMyLocation(location: LatLng): Boolean

    /**
     * Get all the recent shared location by other users from the database
     */
    abstract fun getSharedLocations(): List<LatLng>
}