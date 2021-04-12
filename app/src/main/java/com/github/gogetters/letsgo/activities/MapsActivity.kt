package com.github.gogetters.letsgo.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.map.LocationSharingService
import com.github.gogetters.letsgo.util.PermissionUtils.isPermissionGranted
import com.github.gogetters.letsgo.util.PermissionUtils.requestPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    }
    private var permissionDenied = false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSharingService: LocationSharingService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // set listeners for buttons
        val showPlayersButton = findViewById<Button>(R.id.map_button_showPlayers)
        showPlayersButton.setOnClickListener {
            this.getAndDisplayOtherPlayers()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker at EPFL and move the camera
        val epfl = LatLng(46.51899505106699, 6.563449219980816)
        val zoom = 10f
        mMap.addMarker(MarkerOptions().position(epfl).title("Marker at EPFL"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epfl, zoom))

        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableLocation()
        sendLocation()
    }

    /**
     * Allows to set a locationSharingService that is used to share and retrieve locations of users
     */
    fun setLocationSharingService(locationSharingService: LocationSharingService) {
        this.locationSharingService = locationSharingService
    }

    private fun enableLocation() {
        if (!::mMap.isInitialized)
            return

        // check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun sendLocation() {
        try {
            if (!permissionDenied) {
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (!::locationSharingService.isInitialized || location == null) {
                                // TODO display dialog with error message
                            } else {
                                // share the location with other users
                                locationSharingService.shareMyLocation(LatLng(location.latitude, location.longitude))
                            }
                        }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     *  Gets the positions of other users and displays them on the map
     */
    private fun getAndDisplayOtherPlayers() {
        if (!::locationSharingService.isInitialized)
            return

        var otherPlayers: List<LatLng> = locationSharingService.getSharedLocations()
        for (playerPosition in otherPlayers) {
            val marker = mMap.addMarker(MarkerOptions().position(playerPosition))
            marker // TODO add to a map that maps it to userinfo
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        // TODO display information about my user when clicking on own position
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableLocation()
        } else {
            // Permission was denied
            permissionDenied = true
        }
    }

    /**
     *  Show error message when permission was denied
     */
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            // showMissingPermissionError()
            // TODO actually show error
            permissionDenied = false
        }
    }

}