package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.request.SimulateTransactionRequest
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.network.safeCall
import com.mobileweb3.cosmostools.wallet.WalletInteractor

class TransferInteractor(
    private val api: Api,
    private val walletInteractor: WalletInteractor,
) {

    suspend fun simulateTransaction(): Result<SimulateTransactionResponse?> {
        val currentAccount = walletInteractor.getSelectedAccount()
        val currentNetwork = walletInteractor.getCurrentNetwork()!!

        val currentAddress = walletInteractor.getSelectedAccount()?.address
            ?: return Result.failure(Exception("Can not simulateTransaction when current account is null"))

        return safeCall {
            api.simulateTransaction(
                SimulateTransactionRequest(
                    amount = "0.002",
                    from = currentAddress,
                    to = currentAddress,
                    memo = "",
                    key = currentAccount!!.key,
                    chainId = currentNetwork.chainId
                )
            )
        }
    }
}