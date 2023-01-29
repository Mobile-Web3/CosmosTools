package com.mobileweb3.cosmostools.android.notifications

import android.content.*
import android.widget.Toast

const val COPY_TRANSACTION_HASH_ACTION = "COPY_TRANSACTION_HASH_ACTION"
const val COPY_TRANSACTION_HASH_VALUE = "COPY_TRANSACTION_HASH_VALUE"

class CopyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val txHash = intent.getStringExtra(COPY_TRANSACTION_HASH_VALUE)

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Transaction hash", txHash)
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Transaction hash copied!", Toast.LENGTH_SHORT).show()
    }
}