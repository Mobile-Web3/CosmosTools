package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicEditableTitle
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicGrid
import com.mobileweb3.cosmostools.android.ui.WarningColor
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.disableScreenShot
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun GeneratedMnemonicScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

    disableScreenShot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = "Generated Mnemonic",
            navController = navController
        )

        state.value.generatedMnemonicState?.let {
            MnemonicEditableTitle(title = it.generatedMnemonicTitle, walletStore)

            MnemonicGrid(words = it.mnemonicResult.mnemonic)
        }

        Text(
            text = "Warning! Losing your mnemonics could lead to loss of your assets. " +
                    "We highly recommend keeping your mnemonics offline in a secure location. " +
                    "Never share your mnemonics with anyone else!",
            color = WarningColor,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        FillSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    walletStore.dispatch(WalletAction.UnselectAllNetworks)
                }
            ) {
                Text(
                    text = "Derive Wallet",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        VerticalSpacer()
    }
}