package com.example.trucksload.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import com.example.trucksload.util.Constants.PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE
import com.example.trucksload.util.Constants.PERMISSION_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions

object Permissions {
    fun hasLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        )

    fun requestsLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "Uprawnienia są wymagane do prawidłowego działania aplikacji",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        )
    }

    fun hasBackgroundLocationPermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.CAMERA
        )
    }

    fun requestsBackgroundLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "Background location permission is essential to this application. Without it we will not be able to provide you with our service.",
            PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.CAMERA
        )
    }

}