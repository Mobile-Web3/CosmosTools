package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun EmptyWalletView(
    walletStore: WalletStore,
    navController: NavController
) {
    Text(
        text = "You don't have wallet yet!\n" +
                "Create new or import existing wallet\nby following easy steps.\n" +
                "We don't store your mnemonic phrases\nand private keys!",
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    AddWalletView(
        onCreateWalletClick = {
            walletStore.dispatch(WalletAction.CreateWallet)
            navController.navigate("create_wallet")
        },
        onMnemonicClick = {
            walletStore.dispatch(WalletAction.RestoreWalletByMnemonic)
            navController.navigate("create_wallet")
        },
        onPrivateKeyClick = {
            walletStore.dispatch(WalletAction.RestoreWalletByPrivateKey)
            navController.navigate("create_wallet")
        }
    )
}