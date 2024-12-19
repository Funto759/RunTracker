package com.example.runtracker.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.navigation.NavDeepLinkBuilder
import com.example.runtracker.MainActivity
import com.example.runtracker.R
import com.example.runtracker.util.Constants
import com.example.runtracker.util.Constants.ACTION_PAUSE_SERVICE
import com.example.runtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runtracker.util.Constants.ACTION_STOP_SERVICE
import timber.log.Timber


class TrackingService : LifecycleService() {

    var isFirstRun = true

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
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
                   }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause service")
                    print("Pause service")
                }

                ACTION_STOP_SERVICE -> {
                    Timber.d("Stop service")
                    print("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun startForegroundService(){
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