package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.mobileweb3.cosmostools.resources.Routes
import com.mobileweb3.cosmostools.resources.Routes.PIN_CODE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings
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

            AddWalletView(walletStore, navController, showAsColumn = false)

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
                    navController.navigate(PIN_CODE_SCREEN_ROUTE)
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
                    currentAccount = currentAccount,
                    walletStore = walletStore,
                    context = context,
                    sourceTitle = currentAccount.sourceTitle,
                    onDismissRequest = { openDeleteDialog.value = false }
                )
            }

            VerticalSpacer()

            Button(
                onClick = {
                    navController.navigate(Routes.TRANSFER_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = Strings.TRANSFER_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
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
                        navController.navigate(SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE)
                    }
                )
            }
        }
    }
}