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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicEditableGrid
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicEditableTitle
import com.mobileweb3.cosmostools.android.screens.wallet.views.WarningTextView
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.resources.Routes.DERIVE_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_WALLET_OPTION
import com.mobileweb3.cosmostools.resources.Strings.MNEMONIC_WARNING
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_MNEMONIC_CLEAR_OPTION
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_MNEMONIC_PASTE_OPTION
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_MNEMONIC_SCREEN_TITLE
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun RestoreMnemonicScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    val clipboardManager = LocalClipboardManager.current

    disableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = RESTORE_MNEMONIC_SCREEN_TITLE,
            navController = navController
        )

        state.value.restoreMnemonicState?.let {
            MnemonicEditableTitle(title = it.generatedMnemonicTitle, walletStore)

            MnemonicEditableGrid(walletStore)
        }

        WarningTextView(MNEMONIC_WARNING)

        FillSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    walletStore.dispatch(WalletAction.ClearMnemonic)
                }
            ) {
                Text(
                    text = RESTORE_MNEMONIC_CLEAR_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
                onClick = {
                    val textFromClipboard = clipboardManager.getText()?.toString()
                    textFromClipboard?.let {
                        walletStore.dispatch(WalletAction.PasteMnemonicFromClipboard(it))
                    }
                }
            ) {
                Text(
                    text = RESTORE_MNEMONIC_PASTE_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
                enabled = state.value.restoreMnemonicState?.deriveWalletEnabled ?: false,
                onClick = {
                    walletStore.dispatch(WalletAction.DeriveWallet)
                    navController.navigate(DERIVE_WALLET_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = DERIVE_WALLET_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        VerticalSpacer()
    }
}