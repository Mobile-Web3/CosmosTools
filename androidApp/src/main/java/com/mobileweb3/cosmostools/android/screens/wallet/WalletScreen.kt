package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import com.mobileweb3.cosmostools.android.screens.wallet.views.AddWalletView
import com.mobileweb3.cosmostools.android.screens.wallet.views.EmptyWalletView
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.getGradientBrush
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.displayedAddress

@Composable
fun WalletScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    var palette by remember { mutableStateOf<Palette?>(null) }

    enableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 56.dp)
    ) {
        if (state.value.currentWallet == null) {
            FillSpacer()

            EmptyWalletView(walletStore, navController)
        } else {
            AddWalletView(walletStore, navController)

            VerticalSpacer()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(width = 2.dp, color = PrimaryColor, shape = RoundedCornerShape(10.dp))
                    .background(
                        brush = getGradientBrush(palette, state.value.currentNetwork?.getLogo()),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                VerticalSpacer()

                Text(
                    text = "${state.value.currentWallet?.displayedAddress()}",
                    modifier = Modifier.padding(16.dp)
                )

                VerticalSpacer()
            }


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
                    borderColor = MaterialTheme.colors.primary,
                    onPaletteChanged = { palette = it },
                    onNetworkClicked = {
                        walletStore.dispatch(WalletAction.OpenSwitchNetwork)
                        navController.navigate("switch")
                    }
                )
            }
        }
    }
}