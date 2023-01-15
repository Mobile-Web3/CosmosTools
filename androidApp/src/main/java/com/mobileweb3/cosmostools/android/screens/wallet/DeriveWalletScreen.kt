package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.mobileweb3.cosmostools.android.ui.composables.*
import com.mobileweb3.cosmostools.android.utils.enableScreenshot
import com.mobileweb3.cosmostools.resources.Routes.WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_SCREEN_SAVE_ADDRESSES_OPTION
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.AccountCreateEntity
import com.mobileweb3.cosmostools.wallet.DeriveWalletState
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

        when (state.value.deriveWalletState?.accountCreateRequest) {
            is RequestStatus.Data -> {
                val deriveWalletState = state.value.deriveWalletState!!

                DeriveWalletContent(
                    deriveWalletState = deriveWalletState,
                    accountCreateEntity = deriveWalletState.accountCreateRequest.dataOrNull!!,
                    onPathSelected = {
                        walletStore.dispatch(WalletAction.HDPathChanged(it))
                    },
                    onSaveClicked = {
                        walletStore.dispatch(WalletAction.SaveGeneratedAddressesButtonClicked)
                        navController.popBackStack(WALLET_SCREEN_ROUTE, false)
                    }
                )
            }
            is RequestStatus.Error -> {
                FullScreenError(
                    onRetryClicked = { walletStore.dispatch(WalletAction.DeriveWallet) }
                )
            }
            is RequestStatus.Loading,
            null -> {
                FullScreenLoading()
            }
        }
    }
}

@Composable
fun ColumnScope.DeriveWalletContent(
    deriveWalletState: DeriveWalletState,
    accountCreateEntity: AccountCreateEntity,
    onPathSelected: (hdPath: Int) -> Unit,
    onSaveClicked: () -> Unit
) {
    deriveWalletState.derivationHDPath?.let {
        DerivationPathSelectView(
            hdPath = deriveWalletState.derivationHDPath,
            onPathSelected = { onPathSelected(it) }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(accountCreateEntity.networksWithAddresses) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(2.dp, PrimaryColor, RoundedCornerShape(10.dp))
            ) {
                NetworkCard(
                    modifier = Modifier.width(100.dp),
                    network = item.network,
                    clickable = false,
                    borderColor = PrimaryColor,
                    onPaletteChanged = null,
                    onNetworkClicked = null
                )

                HorizontalSpacer()

                Column {
                    VerticalSpacer()

                    AccountAddress(item.address)

                    item.fullDerivationPath?.let {
                        Text(it)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "item.balance")

                        FillSpacer()

                        Text(
                            text = item.importedStatus.text,
                            fontWeight = FontWeight.Bold,
                            color = Color(item.importedStatus.textColor),
                            modifier = Modifier
                                .background(
                                    color = Color(item.importedStatus.backgroundColor),
                                    shape = RoundedCornerShape(100.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    FillSpacer()

    BottomButtonsRow(onSaveClicked = onSaveClicked)

    VerticalSpacer()
}

@Composable
private fun BottomButtonsRow(
    onSaveClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onSaveClicked() }
        ) {
            Text(
                text = DERIVE_SCREEN_SAVE_ADDRESSES_OPTION,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}