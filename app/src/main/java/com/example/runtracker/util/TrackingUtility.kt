package com.example.runtracker.util

import android.content.Context
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest

object TrackingUtility {

    fun hasPermissionLocation(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun calculatePolylineLength(polyline: MutableList<LatLng>): Float {
        if (polyline.size < 2) return 0f // No distance for empty or single-point polyline

        var distance = 0f
        for (i in 0 until polyline.size - 1) { // Iterate up to the second-to-last point
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]
            val result = FloatArray(1)
            android.location.Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    fun getFormattedStopwatchTime(time: Long, showMilliseconds: Boolean = false): String {
        var milliseconds = time
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10

        return if (showMilliseconds) {
            String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    }
