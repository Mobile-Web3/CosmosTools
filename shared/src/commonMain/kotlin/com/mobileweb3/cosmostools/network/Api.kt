package com.mobileweb3.cosmostools.network

import com.mobileweb3.cosmostools.network.request.*
import com.mobileweb3.cosmostools.network.response.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

const val BASE_URL = "https://mobileweb3.tech/api/"

class Api(private val httpClient: HttpClient) {

    suspend fun getBalance(request: GetBalanceRequest): BaseResponse<GetBalanceResponse?> {
        return defaultRequest("balance/check") {
            it.setBody(request)
        }
    }

    suspend fun getNetworks(): BaseResponse<List<NetworkResponse>?> {
        return defaultRequest("chains/all")
    }

    suspend fun simulateTransaction(request: SimulateTransactionRequest): BaseResponse<SimulateTransactionResponse?> {
        return defaultRequest("transaction/simulate") {
            it.setBody(request)
        }
    }

    suspend fun createMnemonic(request: MnemonicCreateRequest): BaseResponse<String> {
        return defaultRequest("account/mnemonic") {
            it.setBody(request)
        }
    }

    suspend fun createAddresses(request: AccountCreateRequest): BaseResponse<AccountCreateResponse> {
        return defaultRequest("account/create") {
            it.setBody(request)
        }
    }

    suspend fun restoreAddresses(request: AccountRestoreRequest): BaseResponse<AccountRestoreResponse> {
        return defaultRequest("account/restore") {
            it.setBody(request)
        }
    }

    private suspend inline fun <reified T> defaultRequest(
        url: String,
        noinline block: ((HttpRequestBuilder) -> Unit)? = null
    ): BaseResponse<T> {
        return httpClient.post(url) {
            header("Content-Type", "application/json")
            block?.invoke(this)
        }.body()
    }
}