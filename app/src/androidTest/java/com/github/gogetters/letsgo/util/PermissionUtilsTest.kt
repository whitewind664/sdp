package com.github.gogetters.letsgo.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.gogetters.letsgo.util.PermissionUtils.isPermissionGranted
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class PermissionUtilsTest {
    @Test
    fun isPermissionGrantedRejectsIfNoPermission() {
        // empty case
        var emptyPermissions: Array<String> = emptyArray()
        var emptyPermissionResults: IntArray = IntArray(0)
        assertFalse(isPermissionGranted(emptyPermissions, emptyPermissionResults, Manifest.permission.ACCESS_FINE_LOCATION))

        // non-empty case
        var permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        var permissionResults: IntArray = IntArray(2)
        permissionResults[0] = PackageManager.PERMISSION_GRANTED
        assertFalse(isPermissionGranted(permissions, permissionResults, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @Test
    fun isPermissionGrantedAcceptsIfPermission() {
        var permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var permissionResults: IntArray = IntArray(2)
        permissionResults[0] = 0
        permissionResults[1] = PackageManager.PERMISSION_GRANTED
        assertTrue(isPermissionGranted(permissions, permissionResults, Manifest.permission.ACCESS_COARSE_LOCATION))
    }
}