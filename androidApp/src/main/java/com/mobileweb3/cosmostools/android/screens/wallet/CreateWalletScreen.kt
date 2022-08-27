package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.wallet.CreateWalletNetwork
import com.mobileweb3.cosmostools.wallet.CreateWalletState
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun CreateWalletScreen(
    navHostController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = state.value.createWalletState?.title,
            navController = navHostController
        )

        when (state.value.createWalletState) {
            is CreateWalletState.AddressSelection -> {
                SelectNetworksContent(
                    walletStore = walletStore,
                    state = state
                )
            }
            is CreateWalletState.CreatedWallet -> {

            }
            null -> {
                //do nothing
            }
        }
    }
}

sealed class CreateWalletNetworkViewItem {

    object Empty : CreateWalletNetworkViewItem()

    data class Data(val createWalletNetwork: CreateWalletNetwork) : CreateWalletNetworkViewItem()
}