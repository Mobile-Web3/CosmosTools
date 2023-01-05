package com.mobileweb3.cosmostools.repository

import com.mobileweb3.cosmostools.network.Api
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import com.mobileweb3.cosmostools.network.safeCall

class NetworksRepository(private val api: Api) {

    private val networksCache = Cacheable<List<NetworkResponse>>()

    suspend fun getNetworks(): Result<List<NetworkResponse>?> = networksCache.request {
        safeCall { api.getNetworks() }
    }
}