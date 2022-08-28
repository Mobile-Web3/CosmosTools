package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.crypto.Network

data class WalletState(
    val currentNetwork: Network?,
    val currentWallet: String?,
    val addressSelectionState: AddressSelectionState? = null,
    val generatedMnemonicState: GeneratedMnemonicState? = null,
    val switchWalletState: SwitchWalletState? = null
) : State

data class AddressSelectionState(
    val description: String,
    val displayedNetworks: List<NetworkWithSelection>,
    val action: String,
    val actionButtonEnabled: Boolean,
    val selectedCount: Int,
    val searchQuery: String = "",
    val nextRoute: String
)

data class GeneratedMnemonicState(
    val generatedMnemonicTitle: String,
    val resultMnemonicTitle: String,
    val mnemonicResult: MnemonicResult
)

data class MnemonicResult(
    val entropy: ByteArray,
    val mnemonic: List<String>
)

data class Address(
    val network: Network,
    val address: String,
    val balance: String
)

data class SwitchWalletState(
    val networks: List<NetworkWithSelection>,
    val wallets: List<WalletWithSelection>,
    val searchQuery: String = ""
)

data class NetworkWithSelection(
    var selected: Boolean,
    val network: Network
)

data class WalletWithSelection(
    var selected: Boolean,
    val wallet: String
)