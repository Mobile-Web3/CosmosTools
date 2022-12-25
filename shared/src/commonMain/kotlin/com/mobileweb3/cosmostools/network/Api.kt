package com.mobileweb3.cosmostools.network

import com.mobileweb3.cosmostools.network.request.GetBalanceRequest
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

const val BASE_URL = "http://95.143.188.89:8090/api/"
class Api(private val httpClient: HttpClient) {

    suspend fun getBalance(request: GetBalanceRequest): BaseResponse<GetBalanceResponse> {
        return defaultRequest("balance/check") {
            it.setBody(request)
        }
    }

    suspend fun getNetworks(): BaseResponse<List<NetworkResponse>> {
        return httpClient.post("chains/all").body()
    }

    private suspend fun <T> defaultRequest(
        url: String,
        block: ((HttpRequestBuilder) -> Unit)? = null
    ): BaseResponse<T> {
        return httpClient.post(url) {
            block?.invoke(this)
        }.body()
    }
}