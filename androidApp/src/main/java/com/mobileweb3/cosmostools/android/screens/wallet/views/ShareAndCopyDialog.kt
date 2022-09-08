package com.mobileweb3.cosmostools.android.screens.wallet.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.utils.copy
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.resources.Strings.COPY_ADDRESS_OPTION
import com.mobileweb3.cosmostools.resources.Strings.SHARE_QR_OPTION
import com.mobileweb3.cosmostools.resources.Strings.SHARE_TEXT_OPTION
import com.mobileweb3.cosmostools.resources.Strings.SUCCESS_COPY_MESSAGE
import com.mobileweb3.cosmostools.wallet.displayedAddress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareAndCopyDialog(
    accountAddress: String,
    context: Context,
    clipboardManager: ClipboardManager,
    qrCodeBitmap: Bitmap,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissRequest() },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = accountAddress.displayedAddress(),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = qrCodeBitmap.asImageBitmap(),
                    contentDescription = null
                )
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        saveAndShareQr(context, accountAddress, qrCodeBitmap)
                    }
                ) {
                    Text(SHARE_QR_OPTION)
                }

                HorizontalSpacer(4.dp)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, accountAddress)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        ContextCompat.startActivity(context, shareIntent, Bundle())
                    }
                ) {
                    Text(SHARE_TEXT_OPTION)
                }

                HorizontalSpacer(4.dp)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        context.toast(SUCCESS_COPY_MESSAGE)
                        clipboardManager.copy(accountAddress)
                    }
                ) {
                    Text(COPY_ADDRESS_OPTION)
                }
            }
        }
    )
}

private fun saveAndShareQr(context: Context, accountAddress: String, qrCodeBitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // don't forget to make the directory
        val stream = FileOutputStream("$cachePath/${accountAddress}.png") // overwrites this image every time
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "${accountAddress}.png")
        val contentUri = FileProvider.getUriForFile(
            context,
            "com.mobileweb3.cosmostools.fileprovider",
            newFile
        )

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.setDataAndType(
                contentUri,
                context.contentResolver.getType(contentUri)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            ContextCompat.startActivity(context, Intent.createChooser(shareIntent, "Choose an app"), Bundle())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}