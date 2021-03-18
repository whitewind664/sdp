package com.github.gogetters.letsgo.utils

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object PermissionUtils {

    /**
     *  Requests the given permission if necessary.
     */
    @JvmStatic
    fun requestPermission(activity: AppCompatActivity, requestId: Int, permission: String, finishActivity: Boolean) {
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