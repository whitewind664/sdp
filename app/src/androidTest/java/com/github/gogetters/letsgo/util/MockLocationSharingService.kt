package com.github.gogetters.letsgo.util

import android.location.Location
import com.github.gogetters.letsgo.map.LocationSharingService
import com.google.android.gms.maps.model.LatLng

class MockLocationSharingService(): LocationSharingService() {
    val EPFL = LatLng(46.51899505106699, 6.563449219980816)
    val CATHEDRALE = LatLng(46.52342698666607, 6.635400551626213)
    private var others: Map<LatLng, String> = mapOf(EPFL to "1", CATHEDRALE to "2")

    override fun shareMyLocation(location: LatLng): Boolean {
        return true
    }

    override fun getSharedLocations(): Map<LatLng, String> {
        return others
    }
}