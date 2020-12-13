package com.supernova.bkashmanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.service.HistoryService
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.view.activity.SplashActivity


class FirebaseService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        super.onMessageReceived(remoteMessage)
        retrieveData(remoteMessage)
    }

    override fun onNewToken(token: String) {

        super.onNewToken(token)
        Log.d("Device Token", token)
    }

    private fun retrieveData(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data

        if (data.containsKey(Constants.NOTIFICATION_TITLE) && data.containsKey(Constants.NOTIFICATION_MESSAGE)) {

            showNotification(data[Constants.NOTIFICATION_TITLE], data[Constants.NOTIFICATION_MESSAGE])
            refetchTransactions()
        }
    }

    private fun showNotification(title: String?, message: String?) {

        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(applicationContext, SplashActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(applicationContext, Constants.NOTIFICATION_TYPE_BASIC, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.description = application.getString(R.string.channel_description)
            channel.enableLights(true)
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)

            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)

        notificationManager.notify(Constants.NOTIFICATION_TYPE_BASIC, notificationBuilder.build())
    }

    private fun refetchTransactions() {

        val service = Intent(applicationContext, HistoryService::class.java)
        HistoryService.enqueueWork(this, Constants.JOB_FETCH_HISTORIES, service)
    }
}