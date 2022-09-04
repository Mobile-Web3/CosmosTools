package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun AddWalletView(
    walletStore: WalletStore,
    navController: NavController
) {
    val state = walletStore.observeState().collectAsState()

    val pinCodeCheckBeforeSelectNetworks = {
        if (state.value.pinState.userHasPin) {
            navController.navigate("select_networks")
        } else {
            navController.navigate("pin_code")
        }
    }

    Row {
        AddWalletItem(
            title = "Create wallet",
            onClick = {
                walletStore.dispatch(WalletAction.CreateWallet)
                pinCodeCheckBeforeSelectNetworks.invoke()
            },
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Mnemonic",
            onClick = {
                walletStore.dispatch(WalletAction.RestoreWalletByMnemonic)
                pinCodeCheckBeforeSelectNetworks.invoke()
            },
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Private Key",
            onClick = {
                walletStore.dispatch(WalletAction.RestoreWalletByPrivateKey)
                pinCodeCheckBeforeSelectNetworks.invoke()
            },
            modifier = Modifier.weight(1f)
        )
    }
}