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

    if (state.value.revealSourceState == null) {
        return
    }

    val source = when (val addressSource = state.value.revealSourceState!!.addressSource) {
        is AddressSource.Mnemonic -> addressSource.words.joinToString(" ")
        is AddressSource.PrivateKey -> addressSource.key
    }
    Text(text = source)
}