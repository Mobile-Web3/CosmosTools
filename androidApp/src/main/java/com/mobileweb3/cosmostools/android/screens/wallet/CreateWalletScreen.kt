package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.ui.composables.SearchTextField
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.wallet.CreateWalletNetwork
import com.mobileweb3.cosmostools.wallet.WalletAction
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
            .padding(top = 24.dp)
    ) {
        Toolbar(title = "Select Networks", navController = navHostController)

        Text(
            "Select networks for which addresses will be created from a single mnemonic:",
            modifier = Modifier.padding(16.dp)
        )

        SearchTextField(
            title = "Search network by title",
            onSearchTextChanged = {
                walletStore.dispatch(WalletAction.SearchNetworkQueryChanged(it))
            }
        )

        SelectNetworksGrid(
            networks = state.value.createWalletState?.createWalletNetworks,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) { network, selected ->
            walletStore.dispatch(WalletAction.SelectNetworkForCreation(network, selected))
        }
    }
}

sealed class CreateWalletNetworkViewItem {

    object Empty : CreateWalletNetworkViewItem()

    data class Data(val createWalletNetwork: CreateWalletNetwork) : CreateWalletNetworkViewItem()
}