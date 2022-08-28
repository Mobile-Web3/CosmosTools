package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.runtime.Composable
import com.mobileweb3.cosmostools.wallet.MnemonicResult

@Composable
fun MnemonicView(
    mnemonicResult: MnemonicResult
) {
    MnemonicGrid(mnemonicResult.mnemonic)
}