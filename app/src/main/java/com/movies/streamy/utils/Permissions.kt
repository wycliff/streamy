package com.movies.streamy.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Permissions {

    const val REQUEST_CODE_LOCATION_PERMISSIONS = 34
    const val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084


    /**
     *  Check permission
     */
    fun locationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun request(activity: Activity?, permission: String, requestCode: Int) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    fun request(activity: Activity?, permissions: List<String>, requestCode: Int) {
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
        }
    }

}
