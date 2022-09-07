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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.wallet.displayedAddress

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteWalletDialog(
    accountAddress: String?,
    sourceTitle: String?,
    onDeleteAddress: () -> Unit,
    onDeleteSource: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissRequest() },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete Wallet",
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
                    text = "Do you want to just delete address ${accountAddress?.displayedAddress()}" +
                            " or delete all addresses created from $sourceTitle?"
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
                        onDeleteAddress.invoke()
                    }
                ) {
                    Text("Delete address")
                }

                HorizontalSpacer(4.dp)

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDeleteSource.invoke()
                    }
                ) {
                    Text(
                        "Delete $sourceTitle",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}