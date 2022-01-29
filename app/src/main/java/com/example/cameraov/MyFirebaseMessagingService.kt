package com.example.cameraov

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val CHANNEL_ID_ONE = "1"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    
    override fun onMessageReceived(p0: RemoteMessage) {
        val title = p0.notification?.title
        val string = p0.notification?.body
    
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID_ONE,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_ONE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(string)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(this).notify(1, notification)
        super.onMessageReceived(p0)
    }
}
