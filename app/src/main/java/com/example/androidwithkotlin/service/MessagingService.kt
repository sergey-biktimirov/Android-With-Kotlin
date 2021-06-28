package com.example.androidwithkotlin.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.constants.FireBaseMessagingConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0.data.takeIf { it.isNotEmpty() }?.apply {
            handleDataMessage(this)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[FireBaseMessagingConstants.Data.TITLE_KEY]
        val message = data[FireBaseMessagingConstants.Data.MESSAGE_KEY]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationManager
            .notify(
                FireBaseMessagingConstants.Data.NOTIFICATION_ID,
                NotificationCompat.Builder(
                    applicationContext, FireBaseMessagingConstants.Data.CHANNEL_ID
                ).apply {
                    setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    setContentTitle(title)
//                    setContentText(message)
                    setStyle(
                        NotificationCompat.BigTextStyle().bigText(message)
                    )
                    priority = NotificationCompat.PRIORITY_DEFAULT
                }
                    .build()
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(
            FireBaseMessagingConstants.Data.CHANNEL_ID,
            "Channel name",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel description"
        }

        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        //TODO
    }


}