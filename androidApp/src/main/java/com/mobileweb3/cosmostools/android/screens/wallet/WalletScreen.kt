package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import com.mobileweb3.cosmostools.resources.Routes
import com.mobileweb3.cosmostools.resources.Routes.PIN_CODE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings
import com.mobileweb3.cosmostools.resources.Strings.DEFAULT_ERROR
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletState
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun WalletScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

    enableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 56.dp)
    ) {
        when (state.value.networks) {
            is RequestStatus.Loading -> {
                WalletLoading()
            }
            is RequestStatus.Error -> {
                WalletError(
                    onRetryClicked = { walletStore.dispatch(WalletAction.RetryGetNetworks) }
                )
            }
            is RequestStatus.Data -> {
                WalletData(navController, walletStore, state)
            }
        }
    }
}

@Composable
private fun WalletLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun WalletError(
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(DEFAULT_ERROR)
        IconButton(
            modifier = Modifier.scale(1.2f),
            onClick = { onRetryClicked() }
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun ColumnScope.WalletData(
    navController: NavHostController,
    walletStore: WalletStore,
    state: State<WalletState>
) {
    if (state.value.currentAccount == null) {
        FillSpacer()

        EmptyWalletView(walletStore, navController)
    } else {
        AddWalletView(walletStore, navController, showAsColumn = false)

        VerticalSpacer()

        WalletAccount(
            currentAccount = state.value.currentAccount!!,
            currentNetwork = state.value.currentNetwork!!,
            walletStore = walletStore,
            navController = navController
        )

        VerticalSpacer()

        TransferButton {
            navController.navigate(Routes.TRANSFER_SCREEN_ROUTE)
        }
    }

    FillSpacer()

    state.value.currentNetwork?.let {
        WalletBottomRow(
            network = it,
            onNetworkCardClicked = {
                walletStore.dispatch(WalletAction.OpenSwitchNetwork)
                navController.navigate(SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE)
            }
        )
    }
}

@Composable
fun WalletAccount(
    currentAccount: Account,
    currentNetwork: NetworkResponse,
    walletStore: WalletStore,
    navController: NavHostController,
) {
    val openAddressDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }

    AccountCard(
        network = currentNetwork,
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
        ShareAndCopyDialog(accountAddress, qrCodeBitmap) {
            openAddressDialog.value = false
        }
    }

    if (openDeleteDialog.value) {
        DeleteWalletDialog(
            currentAccount = currentAccount,
            walletStore = walletStore,
            sourceTitle = currentAccount.sourceTitle,
            onDismissRequest = { openDeleteDialog.value = false }
        )
    }
}

@Composable
fun TransferButton(
    onClicked: () -> Unit
) {
    Button(
        onClick = onClicked
    ) {
        Text(
            text = Strings.TRANSFER_OPTION,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun WalletBottomRow(
    network: NetworkResponse,
    onNetworkCardClicked: ((NetworkResponse) -> Unit)?
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        FillSpacer()

        NetworkCard(
            network = network,
            modifier = Modifier.width(100.dp),
            borderColor = MaterialTheme.colors.primary,
            onPaletteChanged = null,
            onNetworkClicked = onNetworkCardClicked
        )
    }
}