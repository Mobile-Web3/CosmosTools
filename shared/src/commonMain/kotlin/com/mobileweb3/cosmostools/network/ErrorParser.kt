package com.mobileweb3.cosmostools.network

internal object ErrorParser {

    private const val COSMOS_ERROR_CODE_6_INSUFFICIENT_FUNDS = "transaction failed with code 6"
    private const val ERROR_CODE_6_INSUFFICIENT_FUNDS = "Insufficient funds: your amount is smaller than 2000 uatom (code 6)"

    private const val COSMOS_ERROR_KEY_NOT_FOUND = "key not found"
    private const val ERROR_KEY_NOT_FOUND = "Your account balance is too low for get fees"

    fun parse(error: String?): String? {
        return when {
            error == null -> {
                null
            }
            error.contains(COSMOS_ERROR_KEY_NOT_FOUND) -> {
                ERROR_KEY_NOT_FOUND
            }
            error.startsWith(COSMOS_ERROR_CODE_6_INSUFFICIENT_FUNDS) -> {
                ERROR_CODE_6_INSUFFICIENT_FUNDS
            }
            else -> error
        }
    }
}