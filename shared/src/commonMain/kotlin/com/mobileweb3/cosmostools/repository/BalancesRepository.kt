package com.mobileweb3.cosmostools.repository

import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.request.GetBalanceRequest
import com.mobileweb3.cosmostools.network.response.GetBalanceResponse
import com.mobileweb3.cosmostools.network.safeCall

class BalancesRepository(private val api: Api) {

    private val balancesCache = mutableMapOf<String, Cacheable<GetBalanceResponse>>()

    suspend fun getBalance(
        address: String?,
        chainId: String,
        checkCache: Boolean = true
    ): Result<GetBalanceResponse?> {
        if (address == null) {
            return Result.failure(Exception("Wrong address"))
        }

        val balanceFromCache = balancesCache[address]
        if (checkCache && balanceFromCache != null) {
            return balanceFromCache.request(true) {
                safeCall { api.getBalance(GetBalanceRequest(address, chainId)) }
            }
        }

        balancesCache[address] = Cacheable<GetBalanceResponse>().apply {
            request(false) {
                safeCall { api.getBalance(GetBalanceRequest(address, chainId)) }
            }
        }

        return Result.success(balancesCache[address]?.cached)
    }
}