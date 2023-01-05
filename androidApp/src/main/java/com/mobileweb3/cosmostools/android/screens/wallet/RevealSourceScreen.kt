package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.DeleteWalletDialog
import com.mobileweb3.cosmostools.android.screens.wallet.views.MnemonicGrid
import com.mobileweb3.cosmostools.android.screens.wallet.views.WarningTextView
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.copy
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.resources.Strings.REVEAL_SOURCE_SCREEN_COPY_OPTION
import com.mobileweb3.cosmostools.resources.Strings.REVEAL_SOURCE_SCREEN_DELETE_OPTION
import com.mobileweb3.cosmostools.resources.Strings.SUCCESS_REVEAL_SOURCE_SCREEN_COPY_OPTION
import com.mobileweb3.cosmostools.wallet.AddressSource
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun RevealSourceScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val state = walletStore.observeState().collectAsState()
    val revealState = state.value.revealSourceState

    disableScreenshot()

    if (revealState == null) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Toolbar(
            title = revealState.account.sourceTitle,
            navController = navController
        )

        when (val addressSource = revealState.addressSource) {
            is AddressSource.Mnemonic -> {
                MnemonicGrid(words = addressSource.words)
            }
            is AddressSource.PrivateKey -> {
                OutlinedTextField(
                    readOnly = true,
                    value = addressSource.key,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                addressSource.key
            }
        }

        WarningTextView(
            text = revealState.addressSource.helpMessage
        )

        FillSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    context.toast(revealState.account.sourceTitle + SUCCESS_REVEAL_SOURCE_SCREEN_COPY_OPTION)
                    clipboardManager.copy(revealState.addressSource.getAsString())
                }
            ) {
                Text(
                    text = REVEAL_SOURCE_SCREEN_COPY_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            HorizontalSpacer()

            val openDeleteDialog = remember { mutableStateOf(false) }
            Button(
                onClick = {
                    openDeleteDialog.value = true
                }
            ) {
                Text(
                    text = REVEAL_SOURCE_SCREEN_DELETE_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            if (openDeleteDialog.value) {
                DeleteWalletDialog(
                    currentAccount = revealState.account,
                    sourceTitle = revealState.account.sourceTitle,
                    walletStore = walletStore,
                    onOptionPressed = {
                        navController.popBackStack()
                    },
                    onDismissRequest = {
                        openDeleteDialog.value = false
                    }
                )
            }
        }

        VerticalSpacer()
    }
}