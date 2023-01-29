package com.mobileweb3.cosmostools.network

import com.mobileweb3.cosmostools.network.request.*
import com.mobileweb3.cosmostools.network.response.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

const val BASE_URL = "https://mobileweb3.tech/api/"
private const val V1 = "v1"

class Api(private val httpClient: HttpClient) {

    suspend fun getBalance(request: GetBalanceRequest): BaseResponse<GetBalanceResponse?> {
        return defaultGetRequest("${V1}/accounts/balance") {
            it.parameter("address", request.address)
            it.parameter("chainId", request.chainId)
        }
    }

    suspend fun createAddresses(request: AccountCreateRequest): BaseResponse<AccountCreateResponse> {
        return defaultPostRequest("${V1}/accounts/create") {
            it.setBody(request)
        }
    }

    suspend fun createMnemonic(request: MnemonicCreateRequest): BaseResponse<String> {
        return defaultPostRequest("${V1}/accounts/mnemonic") {
            it.setBody(request)
        }
    }

    suspend fun restoreAddresses(request: AccountRestoreRequest): BaseResponse<AccountRestoreResponse> {
        return defaultPostRequest("${V1}/accounts/restore") {
            it.setBody(request)
        }
    }

    suspend fun getNetworks(): BaseResponse<List<NetworkResponse>?> {
        return defaultGetRequest("${V1}/chains")
    }

    suspend fun getValidators(request: GetValidatorsRequest): BaseResponse<GetValidatorsResponse> {
        return defaultGetRequest("${V1}/chains/${request.chainId}/validators") {
            it.parameter("limit", request.limit)
            it.parameter("offset", request.offset)
        }
    }

    suspend fun sendTransaction(request: SendTransactionRequest): BaseResponse<SendTransactionResponse?> {
        return defaultPostRequest("${V1}/transactions/send") {
            it.setBody(request)
        }
    }

    suspend fun sendTransactionWithFirebase(request: SendTransactionRequestWithFirebase): BaseResponse<SendTransactionResponse?> {
        return defaultPostRequest("${V1}/transactions/send/firebase") {
            it.setBody(request)
        }
    }

    suspend fun simulateTransaction(request: SimulateTransactionRequest): BaseResponse<SimulateTransactionResponse?> {
        return defaultPostRequest("${V1}/transactions/simulate") {
            it.setBody(request)
        }
    }

    private suspend inline fun <reified T> defaultPostRequest(
        url: String,
        noinline block: ((HttpRequestBuilder) -> Unit)? = null
    ): BaseResponse<T> {
        return httpClient.post(url) {
            header("Content-Type", "application/json")
            block?.invoke(this)
        }.body()
    }

    private suspend inline fun <reified T> defaultGetRequest(
        url: String,
        noinline block: ((HttpRequestBuilder) -> Unit)? = null
    ): BaseResponse<T> {
        return httpClient.get(url) {
            header("Content-Type", "application/json")
            block?.invoke(this)
        }.body()
    }
}