package com.mobileweb3.cosmostools.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class SomethingResponse(
    val data: List<Something>
)