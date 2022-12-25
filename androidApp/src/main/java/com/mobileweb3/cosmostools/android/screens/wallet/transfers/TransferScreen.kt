package com.mobileweb3.cosmostools.android.screens.wallet.transfers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.wallet.displayedAddress
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore

@Composable
fun TransferScreen(navController: NavHostController, transferStore: TransferStore) {
    val state = transferStore.observeState().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        EditableTextField(
            title = "From",
            onTextChanged = {},
            readOnly = true,
            initText = state.value.data.from?.address?.displayedAddress() ?: ""
        )

        VerticalSpacer()

        EditableTextField(
            title = "To",
            onTextChanged = {

            },
            trailingIcon = {
                Row {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Content Copy",
                            tint = MaterialTheme.colors.primary
                        )
                    }

                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode2,
                            contentDescription = "Content Copy",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        )
    }
}