package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.runtime.Composable
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun MnemonicEditableTitle(
    title: String,
    walletStore: WalletStore
) {
    EditableTextField(
        title = "Mnemonic title",
        initText = title,
        onTextChanged = {
            walletStore.dispatch(WalletAction.MnemonicTitleChanged(it))
        }
    )
}