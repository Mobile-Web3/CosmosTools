package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.SearchTextField
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.wallet.CreateWalletState
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletState
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun ColumnScope.SelectNetworksContent(
    walletStore: WalletStore,
    state: State<WalletState>
) {
    val addressState = state.value.createWalletState as CreateWalletState.AddressSelection

    Text(
        text = addressState.description,
        modifier = Modifier.padding(16.dp)
    )

    SearchTextField(
        title = "Search network by title",
        onSearchTextChanged = {
            walletStore.dispatch(WalletAction.SearchNetworkQueryChanged(it))
        }
    )

    SelectNetworksGrid(
        networks = addressState.createWalletNetworks,
        columnsCount = 3,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) { network, selected ->
        walletStore.dispatch(WalletAction.SelectNetworkForCreation(network, selected))
    }

    FillSpacer()

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                walletStore.dispatch(WalletAction.UnselectAllNetworks)
            }
        ) {
            Text(
                text = "Unselect All",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }

        FillSpacer()

        Button(
            onClick = {
                walletStore.dispatch(WalletAction.SelectAllNetworks)
            }
        ) {
            Text(
                text = "Select All",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }

        FillSpacer()

        Button(
            enabled = addressState.createWalletNetworks.any { it.selected },
            onClick = {
                walletStore.dispatch(WalletAction.ActionAfterNetworksSelected)
            }
        ) {
            Text(
                text = addressState.action,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }
    }

    VerticalSpacer()
}