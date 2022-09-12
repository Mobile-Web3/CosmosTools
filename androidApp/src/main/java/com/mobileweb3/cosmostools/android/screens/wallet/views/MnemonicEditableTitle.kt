package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.runtime.Composable
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.resources.Strings.MNEMONIC_EDITABLE_FIELD_HINT
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun MnemonicEditableTitle(
    title: String?,
    walletStore: WalletStore
) {
    EditableTextField(
        title = MNEMONIC_EDITABLE_FIELD_HINT,
        initText = title ?: "",
        onTextChanged = {
            walletStore.dispatch(WalletAction.MnemonicTitleChanged(it))
        }
    )
}