package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.utils.toast
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.resources.Strings
import com.mobileweb3.cosmostools.resources.Strings.DELETE_WALLET_DIALOG_MESSAGE_PT1
import com.mobileweb3.cosmostools.resources.Strings.DELETE_WALLET_DIALOG_MESSAGE_PT2
import com.mobileweb3.cosmostools.resources.Strings.DELETE_WALLET_DIALOG_OPTION_ADDRESS
import com.mobileweb3.cosmostools.resources.Strings.DELETE_WALLET_DIALOG_OPTION_SOURCE
import com.mobileweb3.cosmostools.resources.Strings.DELETE_WALLET_DIALOG_TITLE
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.displayedAddress

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteWalletDialog(
    currentAccount: Account,
    sourceTitle: String?,
    walletStore: WalletStore,
    onOptionPressed: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissRequest() },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = DELETE_WALLET_DIALOG_TITLE,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = DELETE_WALLET_DIALOG_MESSAGE_PT1 + currentAccount.address?.displayedAddress() +
                            DELETE_WALLET_DIALOG_MESSAGE_PT2 + sourceTitle
                )
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        walletStore.dispatch(WalletAction.DeleteAddress(currentAccount))
                        context.toast(Strings.DELETE_WALLET_DIALOG_SUCCESS_ADDRESS_DELETE)
                        onOptionPressed?.invoke()
                        onDismissRequest()
                    }
                ) {
                    Text(DELETE_WALLET_DIALOG_OPTION_ADDRESS)
                }

                HorizontalSpacer(4.dp)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        walletStore.dispatch(WalletAction.DeleteSource(currentAccount))
                        context.toast(currentAccount.sourceTitle + Strings.DELETE_WALLET_DIALOG_SUCCESS_SOURCE_DELETE)
                        onOptionPressed?.invoke()
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = DELETE_WALLET_DIALOG_OPTION_SOURCE + sourceTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}