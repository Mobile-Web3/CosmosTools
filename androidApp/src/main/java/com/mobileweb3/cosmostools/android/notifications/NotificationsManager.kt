package com.mobileweb3.cosmostools.android.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mobileweb3.cosmostools.android.R

object NotificationsManager {

    private const val transactionsChannelName = "Transactions"
    private const val transactionsChannelId = "10001"
    private const val transactionsChannelPriority = 5
    private var notificationId = 0

    fun registerChannels(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val notificationChannel = NotificationChannelCompat.Builder(transactionsChannelId, transactionsChannelPriority)
                .setName(transactionsChannelName)
                .build()
            createNotificationChannel(notificationChannel)
        }
    }

    fun showTransactionPush(context: Context, isSuccess: Boolean, txHash: String?) {
        with(NotificationManagerCompat.from(context)) {
            val title = if (isSuccess) {
                "Transaction is successful!"
            } else {
                "Transaction is failed!"
            }

            val content = if (txHash != null) {
                "Hash: $txHash"
            } else null

            val copyIntent = Intent(COPY_TRANSACTION_HASH_ACTION).apply {
                putExtra(COPY_TRANSACTION_HASH_VALUE, txHash)
            }
            val copyPendingIntent = PendingIntent.getBroadcast(context, 0, copyIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            val builder = NotificationCompat.Builder(context, transactionsChannelId)
                .setSmallIcon(R.drawable.ic_bottom_wallet)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .addAction(NotificationCompat.Action(R.drawable.ic_push_copy, "Copy", copyPendingIntent))

            if (content != null) {
                builder
                    .setContentText(content)
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(content)
                    )
            }

            notify(notificationId++, builder.build())
        }
    }
}