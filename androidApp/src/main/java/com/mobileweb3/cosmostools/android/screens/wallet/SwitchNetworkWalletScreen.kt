package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.ui.composables.SearchTextField
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun SwitchNetworkWalletScreen(
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
            title = "Switch Network and Wallet",
            navController = navHostController
        )

        SearchTextField(
            title = "Search network by title",
            onSearchTextChanged = {
                walletStore.dispatch(WalletAction.SearchNetworkQueryChanged(it))
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SelectNetworksGrid(
                networks = state.value.switchWalletState?.networks,
                columnsCount = 1,
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) { network, selected ->
                walletStore.dispatch(WalletAction.SwitchNetwork(network))
            }
        }
    }
}