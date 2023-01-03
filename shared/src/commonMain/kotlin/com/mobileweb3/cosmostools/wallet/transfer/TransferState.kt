package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.shared.RequestStatus

data class TransferState(
    val data: TransferData,
    val simulate: RequestStatus<SimulateTransactionResponse>,
    val selectedFeeIndex: Int
) : State

data class TransferData(
    val from: Account?,
    val to: String,
    val amount: Double = 0.0
)