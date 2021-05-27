package com.github.gogetters.letsgo.activities

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.util.PermissionUtils.isPermissionGranted
import com.github.gogetters.letsgo.util.PermissionUtils.requestPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
        private val EPFL: LatLng = LatLng(46.51899505106699, 6.563449219980816)
        private const val INIT_ZOOM = 10f
        private const val TOAST_DURATION = Toast.LENGTH_SHORT
        private const val MARKER_DISPLAY_PADDING = 250

        // indices in the dialog
        private const val DIALOG_CANCEL_IDX = 1
        private const val DIALOG_CHAT_IDX = 0
    }

    private var permissionDenied = false
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userMarkers: Map<Marker, String> = emptyMap()
    private var otherUsersActivated: Boolean = false

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
            this.activateAndUpdateOtherPlayers()
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

        // Move the camera to EPFL
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EPFL, INIT_ZOOM))
        mMap.setOnMarkerClickListener { marker ->
            displayUserInfo(marker.tag as String)
            true
        }

        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableLocation()
        sendLocation()
    }


    private fun enableLocation() {
        if (!::mMap.isInitialized)
            return

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun sendLocation() {
        try {
            if (!permissionDenied) {
                Log.i("MapsActivity", "Create request")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location == null) {
                            Toast.makeText(
                                this,
                                resources.getString(R.string.map_permissionSharingFailed),
                                TOAST_DURATION
                            ).show()
                            Log.v("MapsActivity", "Location could not be shared")
                        } else {
                            // share the location with other users
                            Database.shareLocation(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            )
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
    private fun activateAndUpdateOtherPlayers() {
        otherUsersActivated = true

        Database.getAllLocations().thenApply {
            if (it == null || it.isEmpty()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.map_noPlayersFound),
                    TOAST_DURATION
                ).show()
            } else {
                setOtherPlayers(it)
            }
        }

    }

    private fun setOtherPlayers(updatedUsers: Map<LatLng, String>) {
        if (otherUsersActivated) {
            removeAllOtherPlayers()
            userMarkers = emptyMap()
            var allPositions: LatLngBounds.Builder = LatLngBounds.Builder()
            for ((playerPosition, id) in updatedUsers.entries) {
                Log.d("TEST MAP", "fetched id $id")
                val marker =
                    mMap.addMarker(
                        MarkerOptions().position(playerPosition)
                            .title("$id") //  TODO change to username
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.person_pin))
                    )
                marker.tag = id  // the userId needs to be stored
                userMarkers = userMarkers + Pair(marker, id)
                allPositions.include(marker.position)
            }
            // update the camera zoom
            val cu =
                CameraUpdateFactory.newLatLngBounds(allPositions.build(), MARKER_DISPLAY_PADDING)
            mMap.moveCamera(cu)
        }
    }

    /**
     * Removes all markers of other users from the map
     */
    private fun removeAllOtherPlayers() {
        for ((marker, _) in userMarkers.entries) {
            marker.remove()
        }
        userMarkers = emptyMap()
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        // TODO display information about my user when clicking on own position
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) return
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
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
            Toast.makeText(
                this,
                resources.getString(R.string.map_permissionDeniedError),
                TOAST_DURATION
            ).show()
            permissionDenied = false
        }
    }

    /**
     * Called when clicked on a marker on the map. Displays a dialog that allows to open a chat with a found user.
     */
    private fun displayUserInfo(username: String) {
        val dialogTexts = arrayOf<CharSequence>(
            resources.getString(R.string.map_dialogOpenChat),
            resources.getString(R.string.map_dialogCancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.map_dialogTitlePrefix) + username)
        builder.setItems(dialogTexts, DialogInterface.OnClickListener { dialog, clickedIndex ->
            if (clickedIndex == DIALOG_CANCEL_IDX) {
                dialog.dismiss()
                return@OnClickListener
            }
            // TODO open chat with the user
        })
        builder.show()
    }
}