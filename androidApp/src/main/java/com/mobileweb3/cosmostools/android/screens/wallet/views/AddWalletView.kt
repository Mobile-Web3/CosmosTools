package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.resources.Routes.PIN_CODE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SELECT_NETWORKS_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.CREATE_WALLET_OPTION_TITLE
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_WALLET_FROM_MNEMONIC_OPTION_TITLE
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_WALLET_FROM_PRIVATE_KEY_OPTION_TITLE
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun AddWalletView(
    walletStore: WalletStore,
    navController: NavController,
    showAsColumn: Boolean
) {
    val state = walletStore.observeState().collectAsState()

    val pinCodeCheckBeforeSelectNetworks = {
        if (state.value.pinState.userHasPin) {
            navController.navigate(SELECT_NETWORKS_SCREEN_ROUTE)
        } else {
            navController.navigate(PIN_CODE_SCREEN_ROUTE)
        }
    }

    if (showAsColumn) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AddWalletItem(
                modifier = Modifier.fillMaxWidth(),
                title = CREATE_WALLET_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.CreateWallet)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                }
            )
            AddWalletItem(
                modifier = Modifier.fillMaxWidth(),
                title = RESTORE_WALLET_FROM_MNEMONIC_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreWalletByMnemonic)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                }
            )
            AddWalletItem(
                modifier = Modifier.fillMaxWidth(),
                title = RESTORE_WALLET_FROM_PRIVATE_KEY_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreWalletByPrivateKey)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                }
            )
        }
    } else {
        Row {
            AddWalletItem(
                title = CREATE_WALLET_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.CreateWallet)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                },
                modifier = Modifier.weight(1f)
            )

            AddWalletItem(
                title = RESTORE_WALLET_FROM_MNEMONIC_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreWalletByMnemonic)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                },
                modifier = Modifier.weight(1f)
            )

            AddWalletItem(
                title = RESTORE_WALLET_FROM_PRIVATE_KEY_OPTION_TITLE,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreWalletByPrivateKey)
                    pinCodeCheckBeforeSelectNetworks.invoke()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}