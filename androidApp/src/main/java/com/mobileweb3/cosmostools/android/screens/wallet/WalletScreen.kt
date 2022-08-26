package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar

@Composable
fun WalletScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Toolbar(title = "Wallet")
    }
}