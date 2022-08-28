package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.SelectNetworksGrid
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.wallet.AddressSelectionState
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun SelectNetworksScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

    enableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = "Select Networks",
            navController = navController
        )

        if (state.value.addressSelectionState != null) {
            SelectNetworksContent(
                navController = navController,
                walletStore = walletStore,
                addressState = state.value.addressSelectionState!!
            )
        }
    }
}

@Composable
fun ColumnScope.SelectNetworksContent(
    navController: NavHostController,
    walletStore: WalletStore,
    addressState: AddressSelectionState
) {
    Text(
        text = addressState.description,
        modifier = Modifier.padding(16.dp)
    )

    EditableTextField(
        title = "Search network by title",
        onTextChanged = {
            walletStore.dispatch(WalletAction.SearchNetworkQueryChanged(it))
        }
    )

    SelectNetworksGrid(
        networks = addressState.displayedNetworks,
        columnsCount = 3,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) { network, selected ->
        walletStore.dispatch(WalletAction.SelectNetworkForCreation(network, selected))
    }

    FillSpacer()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
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

        Button(
            enabled = addressState.actionButtonEnabled,
            onClick = {
                walletStore.dispatch(WalletAction.ActionAfterNetworksSelected)
                navController.navigate(addressState.nextRoute)
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