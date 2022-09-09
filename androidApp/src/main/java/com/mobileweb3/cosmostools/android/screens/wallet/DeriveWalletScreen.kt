package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.DerivationPathSelectView
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.composables.AccountAddress
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.resources.Routes.WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_SCREEN_GENERATING_ADDRESSES_MESSAGE
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_SCREEN_SAVE_ADDRESSES_OPTION
import com.mobileweb3.cosmostools.wallet.CreateAddressMethod
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun DeriveWalletScreen(
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
            title = state.value.deriveWalletState?.title,
            navController = navController
        )

        if (state.value.deriveWalletState?.createAddressMethod is CreateAddressMethod.FromMnemonic) {
            DerivationPathSelectView(
                hdPath = state.value.deriveWalletState?.derivationHDPath,
                onPathSelected = {
                    walletStore.dispatch(WalletAction.HDPathChanged(it))
                }
            )
        }

        if (state.value.deriveWalletState?.generating == true) {
            Text(
                text = DERIVE_SCREEN_GENERATING_ADDRESSES_MESSAGE,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        } else {
            state.value.deriveWalletState?.let { deriveWalletState ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(deriveWalletState.resultAddresses) { createdAddress ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .border(2.dp, PrimaryColor, RoundedCornerShape(10.dp))
                        ) {
                            NetworkCard(
                                modifier = Modifier.width(100.dp),
                                network = createdAddress.network,
                                clickable = false,
                                borderColor = PrimaryColor,
                                onPaletteChanged = null,
                                onNetworkClicked = null
                            )

                            HorizontalSpacer()

                            Column {
                                VerticalSpacer()

                                AccountAddress(createdAddress.address)

                                if (deriveWalletState.createAddressMethod is CreateAddressMethod.FromMnemonic
                                    && createdAddress.fullDerivationPath != null
                                ) {
                                    VerticalSpacer()

                                    Text(text = createdAddress.fullDerivationPath!!)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 4.dp, bottom = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = createdAddress.balance)

                                    FillSpacer()

                                    Text(
                                        text = createdAddress.importedStatus.text,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(createdAddress.importedStatus.textColor),
                                        modifier = Modifier
                                            .background(
                                                color = Color(createdAddress.importedStatus.backgroundColor),
                                                shape = RoundedCornerShape(100.dp)
                                            )
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            FillSpacer()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        walletStore.dispatch(WalletAction.SaveGeneratedAddressesButtonClicked)
                        navController.popBackStack(WALLET_SCREEN_ROUTE, false)
                    }
                ) {
                    Text(
                        text = DERIVE_SCREEN_SAVE_ADDRESSES_OPTION,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            VerticalSpacer()
        }
    }
}