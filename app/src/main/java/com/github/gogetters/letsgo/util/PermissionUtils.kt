package com.github.gogetters.letsgo.util

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * Helper methods that can be used to request permissions from the user (e.g., location)
 */
object PermissionUtils {

    /**
     *  Requests the given permission if necessary.
     *  Note: this method can hardly been tested as it contains static methods that
     *  would need to be mocked (which is not easily possible for Android)
     */
    @JvmStatic
    fun requestPermission(activity: AppCompatActivity, requestId: Int, permission: String) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    requestId
            )
        }
    }

    /**
     * Checks if the result contains a [PackageManager.PERMISSION_GRANTED] result for a
     * permission from a runtime permissions request.
     */
    @JvmStatic
    fun isPermissionGranted(grantPermissions: Array<String>, grantResults: IntArray, permission: String): Boolean {
        for (i in grantPermissions.indices) {
            if (permission == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }
}