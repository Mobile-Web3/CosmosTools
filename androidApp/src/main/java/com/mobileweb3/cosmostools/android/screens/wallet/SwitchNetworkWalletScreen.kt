package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.SelectAccountsGrid
import com.mobileweb3.cosmostools.android.screens.wallet.views.SelectNetworksGrid
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
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

        EditableTextField(
            title = "Search network by title",
            onTextChanged = {
                walletStore.dispatch(WalletAction.SearchNetworkQueryChanged(it))
            }
        )

        VerticalSpacer(16.dp)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SelectNetworksGrid(
                networks = state.value.switchWalletState?.networks,
                columnsCount = 1,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp)
                    .padding(start = 16.dp)
            ) { network, selected ->
                walletStore.dispatch(WalletAction.SwitchNetwork(network))
            }

            HorizontalSpacer()

            SelectAccountsGrid(
                accounts = state.value.switchWalletState?.accounts,
                columnsCount = 1,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 16.dp),
                onAccountClicked = { account, selected ->
                    walletStore.dispatch(WalletAction.SwitchAccount(account))
                }
            )
        }
    }
}