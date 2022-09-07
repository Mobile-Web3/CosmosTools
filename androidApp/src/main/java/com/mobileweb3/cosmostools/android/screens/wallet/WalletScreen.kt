package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.mobileweb3.cosmostools.android.screens.wallet.views.AddWalletView
import com.mobileweb3.cosmostools.android.screens.wallet.views.DeleteWalletDialog
import com.mobileweb3.cosmostools.android.screens.wallet.views.EmptyWalletView
import com.mobileweb3.cosmostools.android.screens.wallet.views.ShareAndCopyDialog
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.composables.AccountCard
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.android.utils.toBitmap
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

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
            val openDeleteDialog = remember { mutableStateOf(false) }
            AccountCard(
                account = currentAccount,
                showOptions = true,
                clickable = true,
                modifier = Modifier.padding(horizontal = 16.dp),
                borderColor = PrimaryColor,
                onRevealSourceClicked = {
                    walletStore.dispatch(WalletAction.RevealAddressSource(currentAccount))
                    navController.navigate("pin_code")
                },
                onDeleteClicked = {
                    openDeleteDialog.value = true
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

            if (openDeleteDialog.value) {
                DeleteWalletDialog(
                    accountAddress = accountAddress,
                    sourceTitle = currentAccount.sourceTitle,
                    onDeleteAddress = {
                        walletStore.dispatch(WalletAction.DeleteAddress(currentAccount))
                        context.toast("Address deleted!")
                        openDeleteDialog.value = false
                    },
                    onDeleteSource = {
                        walletStore.dispatch(WalletAction.DeleteSource(currentAccount))
                        context.toast("${currentAccount.sourceTitle} deleted!")
                        openDeleteDialog.value = false
                    },
                    onDismissRequest = { openDeleteDialog.value = false }
                )
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