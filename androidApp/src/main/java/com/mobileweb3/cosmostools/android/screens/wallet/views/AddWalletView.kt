package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddWalletView(
    onCreateWalletClick: () -> Unit,
    onMnemonicClick: () -> Unit,
    onPrivateKeyClick: () -> Unit
) {
    Row {
        AddWalletItem(
            title = "Create wallet",
            onClick = onCreateWalletClick,
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Mnemonic",
            onClick = onMnemonicClick,
            modifier = Modifier.weight(1f)
        )

        AddWalletItem(
            title = "Private Key",
            onClick = onPrivateKeyClick,
            modifier = Modifier.weight(1f)
        )
    }
}