package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Address
import com.mobileweb3.cosmostools.crypto.EncryptHelper
import com.mobileweb3.cosmostools.crypto.EncryptHelper.getEncData
import com.mobileweb3.cosmostools.crypto.EncryptHelper.getIvData
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.PrivateKey
import com.mobileweb3.cosmostools.crypto.Utils
import com.mobileweb3.cosmostools.shared.System

sealed class CreateAddressMethod {

    abstract fun create(network: Network, allAccounts: List<Account>): CreatedOrRestoredAddress

    abstract fun applyAccount(
        createdAddress: CreatedOrRestoredAddress,
        account: Account
    ): Account

    data class FromMnemonic(
        private val mnemonicResult: MnemonicResult,
        private val mnemonicTitle: String,
        private val hdPath: Int
    ) : CreateAddressMethod() {

        override fun create(network: Network, allAccounts: List<Account>): CreatedOrRestoredAddress {
            val address = Address.createAddressFromEntropyByNetwork(
                network = network,
                entropy = Utils.byteArrayToHexString(mnemonicResult.entropy),
                path = hdPath,
                customPath = 0
            )

            return CreatedOrRestoredAddress(
                network = network,
                address = address,
                balance = "0.000000 ${network.assets[0].symbol}",
                derivationHDPath = hdPath,
                fullDerivationPath = "m/44/${network.slip44}/0/0/$hdPath",
                importedStatus = if (allAccounts.any { it.address == address }) {
                    ImportedStatus.ImportedAddress
                } else {
                    ImportedStatus.NewAddress
                }
            )
        }

        override fun applyAccount(
            createdAddress: CreatedOrRestoredAddress,
            account: Account
        ): Account {
            return account.apply {
                val encryptResult = EncryptHelper.encrypt(
                    alias = "MNEMONIC_KEY" + this.uuid,
                    resource = Utils.byteArrayToHexString(mnemonicResult.entropy),
                    withAuth = false
                )

                resource = encryptResult.getEncData()
                spec = encryptResult.getIvData()
                address = createdAddress.address
                network = createdAddress.network.pretty_name
                hasPrivateKey = true
                fromMnemonic = true
                mnemonicTitle = this@FromMnemonic.mnemonicTitle
                fullDerivationPath = createdAddress.fullDerivationPath
                derivationHDPath = createdAddress.derivationHDPath
                mnemonicSize = mnemonicResult.mnemonic.filter { it.isNotBlank() && it.isNotEmpty() }.size
                importTime = System.getCurrentMillis()
                //TODO check customPath from other networks
                customPath = 0
            }
        }
    }

    class FromPrivateKey(
        private val privateKey: String,
        private val privateKeyTitle: String
    ) : CreateAddressMethod() {

        override fun create(network: Network, allAccounts: List<Account>): CreatedOrRestoredAddress {
            val address = Address.getDpAddress(network, PrivateKey.generatePubHexFromPrivate(privateKey))

            return CreatedOrRestoredAddress(
                network = network,
                address = address,
                balance = "0.000000 ${network.assets[0].symbol}",
                derivationHDPath = -1,
                fullDerivationPath = "-1",
                importedStatus = if (allAccounts.any { it.address == address }) {
                    ImportedStatus.ImportedAddress
                } else {
                    ImportedStatus.NewAddress
                }
            )
        }

        override fun applyAccount(createdAddress: CreatedOrRestoredAddress, account: Account): Account {
            return account.apply {
                val encryptResult = EncryptHelper.encrypt(
                    alias = "PRIVATE_KEY" + this.uuid,
                    resource = privateKey,
                    withAuth = false
                )

                resource = encryptResult.getEncData()
                spec = encryptResult.getIvData()
                address = createdAddress.address
                network = createdAddress.network.pretty_name
                hasPrivateKey = true
                fromMnemonic = false
                mnemonicTitle = this@FromPrivateKey.privateKeyTitle
                importTime = System.getCurrentMillis()
            }
        }
    }

}