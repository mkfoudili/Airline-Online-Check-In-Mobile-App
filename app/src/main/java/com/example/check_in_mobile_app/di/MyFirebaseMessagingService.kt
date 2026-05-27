package com.example.check_in_mobile_app.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.domain.usecase.notification.RegisterFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Service to handle Firebase Cloud Messaging tokens and incoming push notifications.
 * Integrated with the domain layer to register tokens and handle app routing via extras.
 */
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var registerFcmTokenUseCase: RegisterFcmTokenUseCase

    // Coroutine scope tied to the service lifecycle for asynchronous tasks
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New FCM token generated: $token")
        sendTokenToBackend(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received from: ${remoteMessage.from}")

        // 1. Extract content (priority: notification payload -> data payload -> fallback)
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: getString(R.string.app_name)
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: ""

        // 2. Extract specific routing data from the backend 'data' spec
        val type = remoteMessage.data["type"]
        val screen = remoteMessage.data["screen"]
        val bookingId = remoteMessage.data["bookingId"]

        // 3. Trigger local system notification
        showNotification(title, body, type, screen, bookingId, remoteMessage.data)
    }

    private fun sendTokenToBackend(token: String) {
        serviceScope.launch {
            registerFcmTokenUseCase(token).onSuccess {
                Log.d("FCM", "Token successfully registered on backend")
            }.onFailure {
                Log.e("FCM", "Failed to register token: ${it.message}")
            }
        }
    }

    private fun showNotification(
        title: String,
        body: String,
        type: String?,
        screen: String?,
        bookingId: String?,
        data: Map<String, String>
    ) {
        val channelId = "fcm_default_channel"
        val notificationId = System.currentTimeMillis().toInt()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel for Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Flight Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for flight status and check-in reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create Intent for MainActivity with routing extras
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // Add specific routing fields
            putExtra("type", type)
            putExtra("screen", screen)
            putExtra("bookingId", bookingId)
            // Add all data payload for flexible handling
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // System requirement for a small icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
