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
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextFieldWithoutRemember
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun RestorePrivateKeyScreen(
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
            title = "Enter Private Key",
            navController = navController
        )

        EditableTextField("Private key title", state.value.restorePrivateKeyState?.generatedPrivateKeyTitle ?: "") {
            walletStore.dispatch(WalletAction.PrivateKeyTitleEdited(it))
        }

        VerticalSpacer()

        Text(
            text = "Please enter your private key.\n" +
                    "The private key is a 66-digit string starting with 0x",
            style = MaterialTheme.typography.body1
        )

        EditableTextFieldWithoutRemember(
            title = "Insert Private key",
            text = state.value.restorePrivateKeyState?.enteredPrivateKey ?: "",
            maxLines = 3
        ) {
            walletStore.dispatch(WalletAction.PrivateKeyEdited(it))
        }

        FillSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    //todo scan
                }
            ) {
                Text(
                    text = "Scan",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
                onClick = {
                    val textFromClipboard = clipboardManager.getText()?.toString()
                    textFromClipboard?.let {
                        walletStore.dispatch(WalletAction.PastePrivateKeyFromClipboard(it))
                    }
                }
            ) {
                Text(
                    text = "Paste",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
                enabled = state.value.restorePrivateKeyState?.privateKeyIsValid ?: false
                        && state.value.restorePrivateKeyState?.enteredPrivateKey?.isNotEmpty() ?: false,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreFromPrivateKey)
                    navController.navigate("derive_wallet")
                }
            ) {
                Text(
                    text = "Restore",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        VerticalSpacer()
    }
}