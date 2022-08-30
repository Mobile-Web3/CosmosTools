package com.mobileweb3.cosmostools.core.entity

import com.mobileweb3.cosmostools.shared.UUID
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    var id: Long,
    val uuid: String,
    val nickName: String? = null,
    val isFavourite: String? = null,
    var address: String? = null,
    var network: String? = null,
    var hasPrivateKey: Boolean? = null,
    var resource: String? = null,
    var spec: String? = null,
    var fromMnemonic: Boolean? = null,
    var derivationHDPath: Int? = null,
    var fullDerivationPath: String? = null,
    val isValidator: Boolean? = null,
    val sequenceNumber: Int? = null,
    val accountNumber: Int? = null,
    val fetchTime: Long? = null,
    var mnemonicSize: Int? = null,
    var importTime: Long? = null,
    val lastTotal: String? = null,
    val sortOrder: Long? = null,
    val newBip44: Boolean? = null,
    var customPath: Int? = null
) {

    companion object {
        fun newInstance(id: Long): Account {
            return Account(
                id = id,
                uuid = UUID.generateUUID()
            )
        }
    }
}