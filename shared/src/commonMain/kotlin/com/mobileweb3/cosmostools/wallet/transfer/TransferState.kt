package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.response.SendTransactionResponse
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.shared.RequestStatus

data class TransferState(
    val data: TransferData,
    val simulate: RequestStatus<SimulateTransactionResponse>,
    val balance: RequestStatus<GetBalanceResponse>,
    val selectedFeeIndex: Int,
    val send: RequestStatus<SendTransactionResponse>?
) : State

data class TransferData(
    val from: Account?,
    val to: String,
    val amount: String
)