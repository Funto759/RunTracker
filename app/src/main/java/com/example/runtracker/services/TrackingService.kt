package com.example.runtracker.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
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
import dagger.hilt.android.AndroidEntryPoint
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>
@AndroidEntryPoint
class TrackingService : LifecycleService() {

    var isFirstRun = true
    var serviceKilled = false

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val timeInSeconds = MutableLiveData<Long>()
    private var isTimerEnabled = true
    private var lapTime = 0L
    private var totalTime = 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L
    private var time = "00:00:00"

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var currentNotificationBuilder: NotificationCompat.Builder



    companion object{
        val timeInMilli = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoint = MutableLiveData<Polylines>()
    }

    fun startTimer() {
        addPolyLine()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeInMilli.postValue(totalTime + lapTime)
                if (timeInMilli.value!! > lastSecondTimeStamp + 1000L) {
                    timeInSeconds.postValue(timeInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(50L)
                }
            totalTime += lapTime
        }
        }

    fun postInitialValues(){
        isTracking.postValue(false)
        pathPoint.postValue(mutableListOf())
        timeInMilli.postValue(0L)
        timeInSeconds.postValue(0L)
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
        currentNotificationBuilder = baseNotificationBuilder
        Timber.d("onCreate")
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationRequest(it)
            updateNotification(it)
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
                      startTimer()
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
      isTimerEnabled = false
  }

    private fun stopService(){
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           stopForeground(STOP_FOREGROUND_REMOVE)

        } else {
    stopForeground(true)

        }
        stopSelf()
    }

    fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        timeInSeconds.observe(this, Observer{
            val notification = currentNotificationBuilder.setContentText(TrackingUtility.getFormattedStopwatchTime(it * 1000L))
        notificationManager.notify(Constants.NOTIFICATION_ID,notification.build())        })
        startTimer()
        isTracking.postValue(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }



        startForeground(Constants.NOTIFICATION_ID, baseNotificationBuilder.build())
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

    fun updateNotification(isTracking: Boolean) {
        val notificationActionState = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = Constants.ACTION_PAUSE_SERVICE
                }
            PendingIntent.getService(this,1,pauseIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
            }else {
                val resumeIntent = Intent(this, TrackingService::class.java).apply {
                    action = Constants.ACTION_START_OR_RESUME_SERVICE
                }
            PendingIntent.getService(this,1,resumeIntent,FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
            }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        currentNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic_pause_black_24dp,notificationActionState,pendingIntent)
        notificationManager.notify(Constants.NOTIFICATION_ID,currentNotificationBuilder.build())
        }
}