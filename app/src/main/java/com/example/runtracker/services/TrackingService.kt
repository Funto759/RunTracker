package com.example.runtracker.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavDeepLinkBuilder
import androidx.room.InvalidationTracker
import com.example.runtracker.MainActivity
import com.example.runtracker.R
import com.example.runtracker.util.Constants
import com.example.runtracker.util.Constants.ACTION_PAUSE_SERVICE
import com.example.runtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runtracker.util.Constants.ACTION_STOP_SERVICE
import com.example.runtracker.util.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {

    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val pathPoint = MutableLiveData<Polylines>()
    }

    fun postInitialValues(){
        isTracking.postValue(false)
        pathPoint.postValue(mutableListOf())
    }

    private fun addPolyLine() = pathPoint.value?.apply {
        add(mutableListOf())
        pathPoint.postValue(this)
    } ?: pathPoint.postValue(mutableListOf(mutableListOf()))

    private fun addPathPoints(location: Location?){
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoint.value.apply {
                this!!.last().add(position)
                pathPoint.postValue(this)
            }
        }
    }

    val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!){
                result.locations.let { locations ->
                    for (location in locations){
                        addPathPoints(location)
                        Timber.d("New Location: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationRequest(isTracking: Boolean){
        if (isTracking){
            if (TrackingUtility.hasPermissionLocation(this)){
                val request = com.google.android.gms.location.LocationRequest().apply {
                    interval = 5000L
                    fastestInterval = 2000L
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallBack,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
        }
    }
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationRequest(it)
        })
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("Start service...")
                   if (this.isFirstRun == true){
                       startForegroundService()
                       Timber.d("Start ForeGround Service...")
                       isFirstRun = false
                   } else{
                       Timber.d("Resuming service...")
                       print("Resuming service..")
                       startForegroundService()
                   }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause service")
                    print("Pause service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop service")
                    print("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

  private fun pauseService(){
      isTracking.postValue(false)
  }

    fun startForegroundService(){

        addPolyLine()
        isTracking.postValue(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent()).build()

        startForeground(Constants.NOTIFICATION_ID, notificationBuilder)
    }

    fun getMainActivityPendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph) // Replace with your actual navigation graph
            .setDestination(R.id.trackingFragment) // Replace with your actual TrackingFragment ID
            .createPendingIntent()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = android.app.NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            android.app.NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}