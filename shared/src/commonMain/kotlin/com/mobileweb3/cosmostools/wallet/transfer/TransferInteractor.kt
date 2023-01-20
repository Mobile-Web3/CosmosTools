package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.firebase.FirebaseTokenProvider
import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.request.SendTransactionRequest
import com.mobileweb3.cosmostools.network.request.SendTransactionRequestWithFirebase
import com.mobileweb3.cosmostools.network.request.SimulateTransactionRequest
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.response.SendTransactionResponse
import com.mobileweb3.cosmostools.network.response.SimulateTransactionResponse
import com.mobileweb3.cosmostools.network.safeCall
import com.mobileweb3.cosmostools.repository.BalancesRepository
import com.mobileweb3.cosmostools.wallet.WalletInteractor

class TransferInteractor(
    private val api: Api,
    private val walletInteractor: WalletInteractor,
    private val balancesRepository: BalancesRepository
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

    suspend fun getBalance(address: String?): Result<GetBalanceResponse?> {
        return balancesRepository.getBalance(
            address = address,
            chainId = walletInteractor.getCurrentNetwork()!!.chainId,
            checkCache = false
        )
    }

    suspend fun sendTransaction(
        amount: String,
        from: String,
        to: String,
        memo: String = "",
        key: String,
        chainId: String,
        gasAdjusted: String,
        gasPrice: String
    ): Result<SendTransactionResponse?> {
        val firebaseToken = FirebaseTokenProvider.get()
        return if (firebaseToken == null) {
            safeCall {
                api.sendTransaction(
                    SendTransactionRequest(
                        amount = amount,
                        from = from,
                        to = to,
                        memo = memo,
                        key = key,
                        chainId = chainId,
                        gasAdjusted = gasAdjusted,
                        gasPrice = gasPrice
                    )
                )
            }
        } else {
            safeCall {
                api.sendTransactionWithFirebase(
                    SendTransactionRequestWithFirebase(
                        amount = amount,
                        from = from,
                        to = to,
                        memo = memo,
                        key = key,
                        chainId = chainId,
                        gasAdjusted = gasAdjusted,
                        gasPrice = gasPrice,
                        firebaseToken = firebaseToken
                    )
                )
            }
        }
    }
}