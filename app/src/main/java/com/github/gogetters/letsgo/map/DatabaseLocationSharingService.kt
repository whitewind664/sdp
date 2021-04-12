package com.github.gogetters.letsgo.map

import com.google.android.gms.maps.model.LatLng

class DatabaseLocationSharingService(): LocationSharingService() {
    override fun shareMyLocation(location: LatLng): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSharedLocations(): Map<LatLng, String> {
        TODO("Not yet implemented")
    }
}