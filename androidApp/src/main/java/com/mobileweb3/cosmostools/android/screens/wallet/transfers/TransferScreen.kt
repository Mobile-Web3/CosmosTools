package com.mobileweb3.cosmostools.android.screens.wallet.transfers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.TransferButton
import com.mobileweb3.cosmostools.android.screens.wallet.views.WarningTextView
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.WarningColor
import com.mobileweb3.cosmostools.android.ui.composables.*
import com.mobileweb3.cosmostools.android.utils.copy
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.response.SendTransactionResponse
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.resources.Strings.DEFAULT_ERROR_MESSAGE
import com.mobileweb3.cosmostools.resources.Strings.SIMULATE_DEFAULT_ERROR
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.displayedAddress
import com.mobileweb3.cosmostools.wallet.transfer.TransferAction
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore

@Composable
fun TransferScreen(
    navController: NavHostController,
    transferStore: TransferStore, launchEffectCallback: () -> Unit
) {
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
        Toolbar(title = "Transfer", navController = navController)

        EditableTextFieldWithoutRemember(
            title = "From",
            onTextChanged = {},
            readOnly = true,
            text = state.value.data.from?.address?.displayedAddress() ?: "",
            maxLines = 1,
            trailingIcon = {}
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

                    //TODO scan QR
//                    IconButton(
//                        onClick = {
//
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.QrCode2,
//                            contentDescription = "Content Copy",
//                            tint = MaterialTheme.colors.primary
//                        )
//                    }
                }
            },
            maxLines = 2
        )

        VerticalSpacer()

        BalanceView(
            balance = state.value.balance,
            onRefreshBalance = {
                transferStore.dispatch(TransferAction.RefreshBalance)
            }
        )

        AmountTextField(
            title = "Amount",
            text = state.value.data.amount,
            onTextChanged = {
                transferStore.dispatch(TransferAction.OnAmountEdited(it))
            }
        )

        VerticalSpacer()

        FeeAndSendButtonView(
            simulate = state.value.simulate,
            send = state.value.send,
            selectedFeeIndex = state.value.selectedFeeIndex,
            onFeeClicked = { transferStore.dispatch(TransferAction.OnFeeSelected(it)) },
            onRefreshFee = { transferStore.dispatch(TransferAction.RefreshFees) },
            onSendClicked = { transferStore.dispatch(TransferAction.Send) }
        )
    }
}

@Composable
fun BalanceView(
    balance: RequestStatus<GetBalanceResponse>,
    onRefreshBalance: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (balance) {
            is RequestStatus.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is RequestStatus.Error -> {
                DefaultError(
                    balance.description.message ?: "Balance loading error",
                    onRefresh = onRefreshBalance
                )
            }
            is RequestStatus.Data -> {
                Text("Available balance: ${balance.data.availableAmount}")
            }
        }
    }
}

@Composable
fun FeeAndSendButtonView(
    simulate: RequestStatus<SimulateTransactionResponse>,
    send: RequestStatus<SendTransactionResponse>?,
    selectedFeeIndex: Int,
    onFeeClicked: (Int) -> Unit,
    onRefreshFee: () -> Unit,
    onSendClicked: () -> Unit,
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
                DefaultError(
                    message = simulate.description.message ?: SIMULATE_DEFAULT_ERROR,
                    onRefresh = onRefreshFee
                )
            }
            is RequestStatus.Data -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Fee")

                    VerticalSpacer()

                    FeeSwitch(simulate.data, selectedFeeIndex, onFeeClicked)

                    VerticalSpacer()

                    SendStatusView(
                        send = send,
                        onSendClicked = onSendClicked
                    )
                }
            }
        }
    }
}

@Composable
fun SendStatusView(
    send: RequestStatus<SendTransactionResponse>?,
    onSendClicked: () -> Unit
) {
    VerticalSpacer()

    when (send) {
        is RequestStatus.Data -> {
            val clipboardManager = LocalClipboardManager.current

            Text(
                text = "Transaction sent! Click to copy.\n" +
                        "Transaction hash: ${send.data.txHash}",
                modifier = Modifier.clickable {
                    clipboardManager.copy(send.data.txHash)
                }
            )
        }
        is RequestStatus.Error -> {
            TransferButton(
                paddingValues = 0.dp,
                onClicked = onSendClicked
            )

            WarningTextView(text = send.description.message ?: "Send transaction error")
        }
        is RequestStatus.Loading -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        null -> {
            TransferButton(
                paddingValues = 0.dp,
                onClicked = onSendClicked
            )
        }
    }
}

@Composable
fun DefaultError(
    message: String,
    onRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                color = WarningColor,
                text = DEFAULT_ERROR_MESSAGE
            )

            HorizontalSpacer()

            Icon(
                modifier = Modifier.clickable {
                    onRefresh()
                },
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh",
                tint = MaterialTheme.colors.primary
            )
        }

        VerticalSpacer()

        Text(message)
    }
}

@Composable
fun FeeSwitch(
    response: SimulateTransactionResponse,
    selectedFeeIndex: Int,
    onFeeClicked: (Int) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
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