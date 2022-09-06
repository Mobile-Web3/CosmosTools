package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.wallet.AddressSource
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun RevealSourceScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    Text(text = (state.value.revealSourceState!!.addressSource as AddressSource.Mnemonic).words.joinToString(" "))
}