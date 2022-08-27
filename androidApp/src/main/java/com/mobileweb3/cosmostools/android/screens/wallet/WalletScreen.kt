package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.EmptyWalletView
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun WalletScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 56.dp)
    ) {
        if (state.value.currentWallet == null) {
            FillSpacer()

            EmptyWalletView(walletStore, navController)
        }

        FillSpacer()

        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            if (state.value.currentNetwork != null) {
                FillSpacer()

                NetworkCard(
                    network = state.value.currentNetwork!!,
                    modifier = Modifier.width(100.dp),
                    selectedColor = MaterialTheme.colors.primary,
                    onNetworkClicked = {
                        navController.navigate("switch")
                    }
                )
            }
        }
    }
}