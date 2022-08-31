package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Network

data class WalletState(
    val currentNetwork: Network?,
    val currentAccount: Account?,
    val addressSelectionState: AddressSelectionState? = null,
    val generatedMnemonicState: GeneratedMnemonicState? = null,
    val restoreMnemonicState: RestoreMnemonicState? = null,
    val deriveWalletState: DeriveWalletState? = null,
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

data class RestoreMnemonicState(
    val update: Boolean,
    val generatedMnemonicTitle: String,
    val resultMnemonicTitle: String,
    val enteredMnemonic: MutableList<String>,
    val deriveWalletEnabled: Boolean
)

data class MnemonicResult(
    val entropy: ByteArray,
    val mnemonic: List<String>
)

data class DeriveWalletState(
    val generating: Boolean,
    val derivationHDPath: Int,
    val resultAddresses: List<CreatedAddress>
)

data class CreatedAddress(
    val network: Network,
    val address: String,
    val derivationHDPath: Int,
    val fullDerivationPath: String,
    val balance: String
)

fun String.displayedAddress(): String {
    return "${take(12)}...${takeLast(6)}"
}

data class SwitchWalletState(
    val networks: List<NetworkWithSelection>,
    val accounts: List<AccountWithSelection>,
    val searchQuery: String = ""
)

data class NetworkWithSelection(
    var selected: Boolean,
    val network: Network
)

data class AccountWithSelection(
    var selected: Boolean,
    val account: Account
)