package com.mobileweb3.cosmostools.android.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobileweb3.cosmostools.android.notifications.NotificationsManager

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("MessagingService", "onMessageReceived")
        super.onMessageReceived(message)

        val txHash = message.data["txHash"]
        val isSuccess = message.data["isSuccess"]
        NotificationsManager.showTransactionPush(this, isSuccess.toBoolean(), txHash)
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("MessagingService", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }
}