package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.network.response.NetworkResponse
import com.mobileweb3.cosmostools.shared.RequestStatus

data class WalletState(
    val networks: RequestStatus<List<NetworkResponse>>,


    val currentNetwork: NetworkResponse? = null,
    val currentAccount: Account? = null,
    val addressSelectionState: AddressSelectionState? = null,
    val generatedMnemonicState: GeneratedMnemonicState? = null,
    val restoreMnemonicState: RestoreMnemonicState? = null,
    val restorePrivateKeyState: RestorePrivateKeyState? = null,
    val deriveWalletState: DeriveWalletState? = null,
    val switchWalletState: SwitchWalletState? = null,
    val pinState: PinState = PinState(
        userHasPin = false,
        pinPurpose = PinPurpose.Check(nextRoute = ""),
        enterState = PinEnterState.WaitingForEnter
    ),
    val revealSourceState: RevealSourceState? = null
) : State

data class AddressSelectionState(
    val description: String,
    val allNetworks: List<NetworkWithSelection>,
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
    val mnemonic: RequestStatus<List<String>>
    //val mnemonicResult: MnemonicResult
)

data class RestoreMnemonicState(
    val update: Boolean,
    val generatedMnemonicTitle: String,
    val resultMnemonicTitle: String,
    val enteredMnemonic: MutableList<String>,
    val deriveWalletEnabled: Boolean
)

data class RestorePrivateKeyState(
    val generatedPrivateKeyTitle: String,
    val resultPrivateKeyTitle: String,
    val enteredPrivateKey: String,
    val privateKeyIsValid: Boolean
)

data class DeriveWalletState(
    val derivationHDPath: Int?,
    val accountCreateRequest: RequestStatus<AccountCreateEntity>,
    val title: String?,
)

data class AccountCreateEntity(
    val networksWithAddresses: List<NetworkWithAddress>,
    val key: String
)

data class NetworkWithAddress(
    val address: String,
    val network: NetworkResponse,
    val fullDerivationPath: String?,
    val importedStatus: ImportedStatus
)

sealed class ImportedStatus(val text: String, val backgroundColor: Long, val textColor: Long) {
    object NewAddress : ImportedStatus("New", 0xffEEFBF0, 0xff37CC6F)
    object ImportedAddress : ImportedStatus("Imported", 0xffEBF7FF, 0xff02B1FF)
}

fun String.displayedAddress(): String {
    return "${take(12)}...${takeLast(6)}"
}

data class SwitchWalletState(
    val networks: List<NetworkWithSelection>,
    val accounts: List<AccountWithSelection>,
    val searchQuery: String = "",
    val scrollToIndex: Int?
)

data class NetworkWithSelection(
    var selected: Boolean,
    val network: NetworkResponse
)

data class AccountWithSelection(
    var selected: Boolean,
    val account: Account,
    val network: NetworkResponse
)

data class PinState(
    val userHasPin: Boolean,
    val pinPurpose: PinPurpose,
    val enterState: PinEnterState
)

sealed class PinPurpose(
    val title: String,
    open val nextRoute: String,
    open val message: String
) {
    data class Set(
        val firstPin: String = "",
        val confirmPin: String = "",
        val firstPinFilled: Boolean = false,
        override val nextRoute: String,
        override val message: String = "Do not use simple sequences.\nNo way to recover this, please remember!"
    ) : PinPurpose("Set PIN", nextRoute, message)

    data class Check(
        val enteredPin: String = "",
        override val nextRoute: String,
        override val message: String = "Please enter your PIN to continue\n"
    ) : PinPurpose("Confirm PIN", nextRoute, message)
}

sealed class PinEnterState {
    object WaitingForEnter : PinEnterState()

    object Error : PinEnterState()

    object Success : PinEnterState()
}

data class RevealSourceState(
    val account: Account
)