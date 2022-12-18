package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.core.entity.Account

data class TransferState(
    val data: TransferData
) : State

data class TransferData(
    val from: Account,
    val to: String? = null,
    val amount: Double = 0.0
)