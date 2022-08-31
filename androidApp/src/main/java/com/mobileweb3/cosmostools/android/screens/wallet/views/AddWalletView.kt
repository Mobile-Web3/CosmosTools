package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun AddWalletView(
    walletStore: WalletStore,
    navController: NavController
) {
    Row {
        AddWalletItem(
            title = "Create wallet",
            onClick = {
                walletStore.dispatch(WalletAction.CreateWallet)
                navController.navigate("select_networks")
            },
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Mnemonic",
            onClick = {
                walletStore.dispatch(WalletAction.RestoreWalletByMnemonic)
                navController.navigate("select_networks")
            },
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Private Key",
            onClick = {
                walletStore.dispatch(WalletAction.RestoreWalletByPrivateKey)
                navController.navigate("select_networks")
            },
            modifier = Modifier.weight(1f)
        )
    }
}