package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.resources.Strings.EMPTY_WALLET_SCREEN_MESSAGE
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun EmptyWalletView(
    walletStore: WalletStore,
    navController: NavController
) {
    Text(
        text = EMPTY_WALLET_SCREEN_MESSAGE,
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    AddWalletView(walletStore, navController, showAsColumn = false)
}