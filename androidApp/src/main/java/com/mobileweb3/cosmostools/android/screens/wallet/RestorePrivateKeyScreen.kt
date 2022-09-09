package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanOptions.QR_CODE
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextFieldWithoutRemember
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.android.utils.disableScreenshot
import com.mobileweb3.cosmostools.resources.Routes.DERIVE_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_HINT
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_TITLE_HINT
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_MESSAGE
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_PASTE_OPTION
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_RESTORE_OPTION
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_SCAN_OPTION
import com.mobileweb3.cosmostools.resources.Strings.RESTORE_PRIVATE_KEY_SCREEN_TITLE
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
            title = RESTORE_PRIVATE_KEY_SCREEN_TITLE,
            navController = navController
        )

        EditableTextField(
            title = RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_TITLE_HINT,
            initText = state.value.restorePrivateKeyState?.generatedPrivateKeyTitle ?: ""
        ) {
            walletStore.dispatch(WalletAction.PrivateKeyTitleEdited(it))
        }

        VerticalSpacer(16.dp)

        Text(
            text = RESTORE_PRIVATE_KEY_SCREEN_MESSAGE,
            style = MaterialTheme.typography.body1
        )

        EditableTextFieldWithoutRemember(
            title = RESTORE_PRIVATE_KEY_SCREEN_EDITABLE_HINT,
            text = state.value.restorePrivateKeyState?.enteredPrivateKey ?: "",
            maxLines = 3
        ) {
            walletStore.dispatch(WalletAction.PrivateKeyEdited(it))
        }

        FillSpacer()

        val scanLauncher = rememberLauncherForActivityResult(
            contract = ScanContract(),
            onResult = { result ->
                if (result.contents != null) {
                    walletStore.dispatch(WalletAction.PrivateKeyEdited(result.contents))
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    scanLauncher.launch(
                        ScanOptions()
                            .setOrientationLocked(true)
                            .setDesiredBarcodeFormats(QR_CODE)
                    )
                }
            ) {
                Text(
                    text = RESTORE_PRIVATE_KEY_SCREEN_SCAN_OPTION,
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
                    text = RESTORE_PRIVATE_KEY_SCREEN_PASTE_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }

            Button(
                enabled = state.value.restorePrivateKeyState?.privateKeyIsValid ?: false
                        && state.value.restorePrivateKeyState?.enteredPrivateKey?.isNotEmpty() ?: false,
                onClick = {
                    walletStore.dispatch(WalletAction.RestoreFromPrivateKey)
                    navController.navigate(DERIVE_WALLET_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = RESTORE_PRIVATE_KEY_SCREEN_RESTORE_OPTION,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        VerticalSpacer()
    }
}