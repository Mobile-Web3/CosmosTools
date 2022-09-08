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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicEditableTitle
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicGrid
import com.mobileweb3.cosmostools.android.screens.wallet.views.WarningTextView
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.copy
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.resources.Routes.DERIVE_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.COPY_MNEMONIC_OPTION
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_WALLET_OPTION
import com.mobileweb3.cosmostools.resources.Strings.GENERATED_MNEMONIC_SCREEN_TITLE
import com.mobileweb3.cosmostools.resources.Strings.MNEMONIC_WARNING
import com.mobileweb3.cosmostools.resources.Strings.SUCCESS_COPY_MNEMONIC
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun GeneratedMnemonicScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    disableScreenshot()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = GENERATED_MNEMONIC_SCREEN_TITLE,
            navController = navController
        )

        state.value.generatedMnemonicState?.let {
            MnemonicEditableTitle(title = it.generatedMnemonicTitle, walletStore)

            MnemonicGrid(words = it.mnemonicResult.mnemonic)
        }

        WarningTextView(MNEMONIC_WARNING)

        FillSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    context.toast(SUCCESS_COPY_MNEMONIC)
                    state.value.generatedMnemonicState?.mnemonicResult?.mnemonic?.let {
                        clipboardManager.copy(it.joinToString(" "))
                    }
                }
            ) {
                Text(
                    text = COPY_MNEMONIC_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
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