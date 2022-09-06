package com.mobileweb3.cosmostools.android.screens.wallet

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.mobileweb3.cosmostools.android.screens.wallet.views.AddWalletView
import com.mobileweb3.cosmostools.android.screens.wallet.views.EmptyWalletView
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.composables.AccountCard
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.android.utils.toBitmap
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.displayedAddress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun WalletScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    enableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 56.dp)
    ) {
        if (state.value.currentAccount == null) {
            FillSpacer()

            EmptyWalletView(walletStore, navController)
        } else {
            val currentAccount = state.value.currentAccount!!

            AddWalletView(walletStore, navController)

            VerticalSpacer()

            val openAddressDialog = remember { mutableStateOf(false) }
            AccountCard(
                account = currentAccount,
                showOptions = true,
                clickable = true,
                modifier = Modifier.padding(horizontal = 16.dp),
                borderColor = PrimaryColor,
                onSourceClicked = {
                    walletStore.dispatch(WalletAction.RevealAddressSource(currentAccount))
                    navController.navigate("pin_code")
                },
                onAccountClicked = {
                    openAddressDialog.value = true
                }
            )

            val accountAddress = currentAccount.address!!
            val qrCodeBitMatrix = QRCodeWriter().encode(accountAddress, BarcodeFormat.QR_CODE, 400, 400)
            val qrCodeBitmap = qrCodeBitMatrix.toBitmap()

            if (openAddressDialog.value) {
                ShareAndCopyDialog(accountAddress, context, clipboardManager, qrCodeBitmap) {
                    openAddressDialog.value = false
                }
            }

            VerticalSpacer()
        }

        FillSpacer()

        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            if (state.value.currentNetwork != null) {
                FillSpacer()

                NetworkCard(
                    network = state.value.currentNetwork!!,
                    modifier = Modifier.width(100.dp),
                    borderColor = MaterialTheme.colors.primary,
                    onPaletteChanged = null,
                    onNetworkClicked = {
                        walletStore.dispatch(WalletAction.OpenSwitchNetwork)
                        navController.navigate("switch")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ShareAndCopyDialog(
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
                    Text("Share QR")
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
                        startActivity(context, shareIntent, Bundle())
                    }
                ) {
                    Text("Share text")
                }

                HorizontalSpacer(4.dp)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        Toast.makeText(context, "Address copied!", Toast.LENGTH_LONG).show()
                        clipboardManager.setText(AnnotatedString(accountAddress))
                    }
                ) {
                    Text("Copy")
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
            startActivity(context, Intent.createChooser(shareIntent, "Choose an app"), Bundle())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}