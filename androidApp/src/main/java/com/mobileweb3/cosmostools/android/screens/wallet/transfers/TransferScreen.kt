package com.mobileweb3.cosmostools.android.screens.wallet.transfers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextField
import com.mobileweb3.cosmostools.android.ui.composables.EditableTextFieldWithoutRemember
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.displayedAddress
import com.mobileweb3.cosmostools.wallet.transfer.TransferAction
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore

@Composable
fun TransferScreen(transferStore: TransferStore, launchEffectCallback: () -> Unit) {
    LaunchedEffect(launchEffectCallback) {
        transferStore.dispatch(TransferAction.OnNavigate)
    }
    
    val state = transferStore.observeState().collectAsState()
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        EditableTextFieldWithoutRemember(
            title = "From",
            onTextChanged = {},
            readOnly = true,
            text = state.value.data.from?.address?.displayedAddress() ?: "",
            maxLines = 1
        )

        VerticalSpacer()

        EditableTextFieldWithoutRemember(
            title = "To",
            text = state.value.data.to,
            onTextChanged = {
                transferStore.dispatch(TransferAction.OnAddressToEdited(it))
            },
            trailingIcon = {
                Row {
                    IconButton(
                        onClick = {
                            clipboardManager.getText()?.let {
                                transferStore.dispatch(TransferAction.OnAddressToEdited(it.text))
                            }
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
            },
            maxLines = 1
        )

        VerticalSpacer()

        FeeView(
            simulate = state.value.simulate,
            selectedFeeIndex = state.value.selectedFeeIndex,
            onFeeClicked = { transferStore.dispatch(TransferAction.OnFeeSelected(it)) },
            onRefreshFee = { transferStore.dispatch(TransferAction.RefreshFees) }
        )
    }
}

@Composable
fun FeeView(
    simulate: RequestStatus<SimulateTransactionResponse>,
    selectedFeeIndex: Int,
    onFeeClicked: (Int) -> Unit,
    onRefreshFee: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (simulate) {
            is RequestStatus.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is RequestStatus.Error -> {
                Text("Fees getting error")

                HorizontalSpacer()

                Icon(
                    modifier = Modifier.clickable {
                        onRefreshFee()
                    },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh",
                    tint = MaterialTheme.colors.primary
                )
            }
            is RequestStatus.Data -> {
                FeeSwitch(simulate.data, selectedFeeIndex, onFeeClicked)
            }
        }
    }
}

@Composable
fun FeeSwitch(
    response: SimulateTransactionResponse,
    selectedFeeIndex: Int,
    onFeeClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onFeeClicked(0) }
                .background(
                    color = if (selectedFeeIndex == 0) {
                        SelectedColor
                    } else {
                        PrimaryColor
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(4.dp)
            Text("Low")
            VerticalSpacer(2.dp)
            Text(response.lowGasPrice)
            VerticalSpacer(4.dp)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onFeeClicked(1) }
                .background(
                    color = if (selectedFeeIndex == 1) {
                        SelectedColor
                    } else {
                        PrimaryColor
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(4.dp)
            Text("Medium")
            VerticalSpacer(2.dp)
            Text(response.averageGasPrice)
            VerticalSpacer(4.dp)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onFeeClicked(2) }
                .background(
                    color = if (selectedFeeIndex == 2) {
                        SelectedColor
                    } else {
                        PrimaryColor
                    }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(4.dp)
            Text("High")
            VerticalSpacer(2.dp)
            Text(response.highGasPrice)
            VerticalSpacer(4.dp)
        }
    }
}