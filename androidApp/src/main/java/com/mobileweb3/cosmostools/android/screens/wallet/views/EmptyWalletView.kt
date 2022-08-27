@file:OptIn(ExperimentalMaterialApi::class)

package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun ColumnScope.EmptyWalletView(
    walletStore: WalletStore,
    navController: NavController
) {
    FillSpacer()

    Text(
        text = "You don't have wallet yet!\n" +
                "Create new or import existing wallet\nby following easy steps.\n" +
                "We don't store your mnemonic phrases\nand private keys!",
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    AddWalletView(
        onCreateWalletClick = {
            navController.navigate("create_wallet")
        },
        onMnemonicClick = {},
        onPrivateKeyClick = {}
    )

    FillSpacer()
}