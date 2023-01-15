package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
import com.mobileweb3.cosmostools.android.ui.composables.*
import com.mobileweb3.cosmostools.android.utils.copy
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.crypto.buildMnemonic
import com.mobileweb3.cosmostools.resources.Routes.DERIVE_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.COPY_MNEMONIC_OPTION
import com.mobileweb3.cosmostools.resources.Strings.DERIVE_WALLET_OPTION
import com.mobileweb3.cosmostools.resources.Strings.GENERATED_MNEMONIC_SCREEN_TITLE
import com.mobileweb3.cosmostools.resources.Strings.MNEMONIC_WARNING
import com.mobileweb3.cosmostools.resources.Strings.SUCCESS_COPY_MNEMONIC
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun GeneratedMnemonicScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()

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

        when (state.value.generatedMnemonicState?.mnemonic) {
            is RequestStatus.Data -> {
                MnemonicContent(
                    walletStore = walletStore,
                    mnemonicTitle = state.value.generatedMnemonicState!!.generatedMnemonicTitle,
                    mnemonic = state.value.generatedMnemonicState!!.mnemonic.dataOrNull!!,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            is RequestStatus.Error -> {
                FullScreenError(
                    onRetryClicked = {
                        walletStore.dispatch(WalletAction.RetryCreateMnemonic)
                    }
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
private fun ColumnScope.MnemonicContent(
    walletStore: WalletStore,
    mnemonicTitle: String,
    mnemonic: List<String>,
    onNavigate: (route: String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    MnemonicEditableTitle(title = mnemonicTitle, walletStore)

    MnemonicGrid(words = mnemonic)

    WarningTextView(MNEMONIC_WARNING)

    FillSpacer()

    ButtonsRow(
        onCopyClicked = {
            context.toast(SUCCESS_COPY_MNEMONIC)
            clipboardManager.copy(mnemonic.buildMnemonic())
        },
        onDeriveClicked = {
            walletStore.dispatch(WalletAction.DeriveWallet)
            onNavigate(DERIVE_WALLET_SCREEN_ROUTE)
        }
    )

    VerticalSpacer()
}

@Composable
private fun ButtonsRow(
    onCopyClicked: () -> Unit,
    onDeriveClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = onCopyClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = COPY_MNEMONIC_OPTION,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }

        HorizontalSpacer()

        Button(
            onClick = onDeriveClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = DERIVE_WALLET_OPTION,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}