package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun CreateWalletScreen(
    walletStore: WalletStore
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "create wallet here")
    }
}