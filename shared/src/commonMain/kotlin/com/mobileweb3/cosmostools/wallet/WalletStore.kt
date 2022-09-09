package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.Action
import com.mobileweb3.cosmostools.app.Effect
import com.mobileweb3.cosmostools.app.Store
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.EncryptHelper
import com.mobileweb3.cosmostools.crypto.Entropy
import com.mobileweb3.cosmostools.crypto.HexUtils
import com.mobileweb3.cosmostools.crypto.Mnemonic
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.PrivateKey
import com.mobileweb3.cosmostools.crypto.mockNetworks
import com.mobileweb3.cosmostools.resources.Constants.PIN_LENGTH
import com.mobileweb3.cosmostools.resources.Routes.GENERATED_MNEMONIC_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.RESTORE_MNEMONIC_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.RESTORE_PRIVATE_KEY_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.REVEAL_SOURCE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SELECT_NETWORKS_SCREEN_ROUTE
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WalletAction : Action {
    object CreateWallet : WalletAction()

    object RestoreWalletByMnemonic : WalletAction()

    object RestoreWalletByPrivateKey : WalletAction()

    class SearchNetworkQueryChanged(val query: String) : WalletAction()

    class SelectNetworkForCreationOrRestore(
        val createWalletNetwork: NetworkWithSelection,
        val selected: Boolean
    ) : WalletAction()

    object SelectAllNetworks : WalletAction()

    object UnselectAllNetworks : WalletAction()

    object ActionAfterNetworksSelected : WalletAction()

    class MnemonicTitleChanged(val newTitle: String) : WalletAction()

    object ClearMnemonic : WalletAction()

    class PasteMnemonicFromClipboard(val text: String) : WalletAction()

    class MnemonicWordEdited(val index: Int, val newText: String) : WalletAction()

    class PrivateKeyTitleEdited(val newTitle: String) : WalletAction()

    class PastePrivateKeyFromClipboard(val text: String) : WalletAction()

    class PrivateKeyEdited(val newPrivateKey: String) : WalletAction()

    object RestoreFromPrivateKey : WalletAction()

    object DeriveWallet : WalletAction()

    class HDPathChanged(val hdPath: Int) : WalletAction()

    object SaveGeneratedAddressesButtonClicked : WalletAction()

    object OpenSwitchNetwork : WalletAction()

    class SwitchNetwork(val network: NetworkWithSelection) : WalletAction()

    class SwitchAccount(val account: Account) : WalletAction()

    class PinCodeNewSymbol(val enteredNumber: Int) : WalletAction()

    object PinCodeDeleteSymbol : WalletAction()

    class RevealAddressSource(val account: Account) : WalletAction()

    class DeleteAddress(val account: Account) : WalletAction()

    class DeleteSource(val account: Account) : WalletAction()
}

sealed class WalletSideEffect : Effect {
    data class Message(val text: String) : WalletSideEffect()
}

private val initMnemonic = listOf(
    "", "", "", "", "", "", "", "",
    "", "", "", "", "", "", "", "",
    "", "", "", "", "", "", "", ""
)

class WalletStore(
    private val interactor: WalletInteractor
) : Store<WalletState, WalletAction, WalletSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        WalletState(
            currentNetwork = interactor.getCurrentNetwork(),
            currentAccount = interactor.getSelectedAccount(),
            addressSelectionState = null,
            switchWalletState = null,
            pinState = PinState(
                userHasPin = false,
                pinPurpose = PinPurpose.Set(nextRoute = ""),
                enterState = PinEnterState.WaitingForEnter
            )
        )
    )
    private val sideEffect = MutableSharedFlow<WalletSideEffect>()

    private var walletAction: WalletAction? = null
    private var resultNetworks = getInitSelectionNetworks()

    init {
        requestWalletInfo(state.value.currentAccount)

        refreshPinState("")
    }

    private fun refreshPinState(nextRoute: String) {
        val userHasPin = interactor.userHasPin()

        state.tryEmit(state.value.copy(
            pinState = state.value.pinState.copy(
                userHasPin = userHasPin,
                pinPurpose = if (userHasPin) {
                    PinPurpose.Check(nextRoute = nextRoute)
                } else {
                    PinPurpose.Set(nextRoute = nextRoute)
                },
                enterState = PinEnterState.WaitingForEnter
            )
        ))
    }

    private fun requestWalletInfo(currentAccount: Account?) {
        if (currentAccount == null) {
            return
        }

        //todo request wallet info
    }

    override fun observeState(): StateFlow<WalletState> = state

    override fun observeSideEffect(): Flow<WalletSideEffect> = sideEffect

    override fun dispatch(action: WalletAction) {
        Napier.d(tag = "WalletStore", message = "Action: $action")

        val oldState = state.value

        var newState = oldState

        when (action) {
            WalletAction.CreateWallet -> {
                refreshPinState(SELECT_NETWORKS_SCREEN_ROUTE)

                walletAction = WalletAction.CreateWallet

                initSelectNetworks(
                    description = "Select networks for which addresses will be created from a single mnemonic:",
                    action = "Create",
                    nextRoute = GENERATED_MNEMONIC_SCREEN_ROUTE
                )
            }
            WalletAction.RestoreWalletByMnemonic -> {
                refreshPinState(SELECT_NETWORKS_SCREEN_ROUTE)

                walletAction = WalletAction.RestoreWalletByMnemonic

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from entered mnemonic:",
                    action = "Restore",
                    nextRoute = RESTORE_MNEMONIC_SCREEN_ROUTE
                )
            }
            WalletAction.RestoreWalletByPrivateKey -> {
                refreshPinState(SELECT_NETWORKS_SCREEN_ROUTE)

                walletAction = WalletAction.RestoreWalletByPrivateKey

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from private key:",
                    action = "Restore",
                    nextRoute = RESTORE_PRIVATE_KEY_SCREEN_ROUTE
                )
            }
            is WalletAction.SearchNetworkQueryChanged -> {
                val networksByQuery = getNetworksByQuery(action.query)

                newState = oldState.copy(
                    addressSelectionState = oldState.addressSelectionState?.copy(
                        displayedNetworks = networksByQuery,
                        actionButtonEnabled = resultNetworks.any { it.selected },
                        searchQuery = action.query
                    ),
                    switchWalletState = SwitchWalletState(
                        networks = networksByQuery,
                        accounts = emptyList(),
                        searchQuery = action.query
                    )
                )

                if (networksByQuery.none { it.network == interactor.getCurrentNetwork() }) {
                    if (networksByQuery.isNotEmpty()) {
                        updateSwitchWallets(networksByQuery[0].network)
                    }
                }
            }
            is WalletAction.SelectNetworkForCreationOrRestore -> {
                resultNetworks.find { it == action.createWalletNetwork }?.selected = action.selected

                newState = oldState.copy(
                    addressSelectionState = oldState.addressSelectionState?.copy(
                        displayedNetworks = getNetworksByQuery(oldState.addressSelectionState.searchQuery),
                        actionButtonEnabled = resultNetworks.any { it.selected },
                        selectedCount = getSelectedNetworksCount()
                    )
                )
            }
            is WalletAction.SelectAllNetworks -> {
                resultNetworks.forEach { it.selected = true }

                newState = oldState.copy(
                    addressSelectionState = oldState.addressSelectionState?.copy(
                        displayedNetworks = getNetworksByQuery(oldState.addressSelectionState.searchQuery),
                        actionButtonEnabled = true,
                        selectedCount = resultNetworks.size
                    )
                )
            }
            is WalletAction.UnselectAllNetworks -> {
                resultNetworks.forEach { it.selected = false }

                newState = oldState.copy(
                    addressSelectionState = oldState.addressSelectionState?.copy(
                        displayedNetworks = getNetworksByQuery(oldState.addressSelectionState.searchQuery),
                        actionButtonEnabled = false,
                        selectedCount = 0
                    )
                )
            }
            WalletAction.ActionAfterNetworksSelected -> {
                when (walletAction) {
                    WalletAction.CreateWallet -> {
                        val mnemonicResult = createMnemonic()
                        val mnemonicTitle = "Mnemonic ${interactor.getMnemonicCounter()}"

                        newState = oldState.copy(
                            generatedMnemonicState = GeneratedMnemonicState(
                                generatedMnemonicTitle = mnemonicTitle,
                                resultMnemonicTitle = mnemonicTitle,
                                mnemonicResult = mnemonicResult
                            ),
                            deriveWalletState = null
                        )
                    }
                    WalletAction.RestoreWalletByMnemonic -> {
                        val mnemonicTitle = "Mnemonic ${interactor.getMnemonicCounter()}"

                        newState = oldState.copy(
                            restoreMnemonicState = RestoreMnemonicState(
                                update = true,
                                generatedMnemonicTitle = mnemonicTitle,
                                resultMnemonicTitle = mnemonicTitle,
                                enteredMnemonic = initMnemonic.toMutableList(),
                                deriveWalletEnabled = false
                            ),
                            deriveWalletState = null
                        )
                    }
                    WalletAction.RestoreWalletByPrivateKey -> {
                        val privateKeyTitle = "Private key ${interactor.getPrivateKeyCounter()}"

                        newState = oldState.copy(
                            restorePrivateKeyState = RestorePrivateKeyState(
                                generatedPrivateKeyTitle = privateKeyTitle,
                                resultPrivateKeyTitle = privateKeyTitle,
                                enteredPrivateKey = "",
                                privateKeyIsValid = false
                            ),
                            deriveWalletState = null
                        )
                    }
                    else -> {
                        //not happen
                    }
                }
            }
            is WalletAction.MnemonicTitleChanged -> {
                newState = oldState.copy(
                    generatedMnemonicState = oldState.generatedMnemonicState?.copy(
                        resultMnemonicTitle = action.newTitle
                    ),
                    restoreMnemonicState = oldState.restoreMnemonicState?.copy(
                        update = !oldState.restoreMnemonicState.update,
                        resultMnemonicTitle = action.newTitle
                    )
                )
            }
            is WalletAction.ClearMnemonic -> {
                newState = oldState.copy(
                    restoreMnemonicState = oldState.restoreMnemonicState?.copy(
                        update = !oldState.restoreMnemonicState.update,
                        enteredMnemonic = initMnemonic.toMutableList(),
                        deriveWalletEnabled = false
                    )
                )
            }
            is WalletAction.PasteMnemonicFromClipboard -> {
                val mnemonicFromText = action.text.split(" ")

                newState = oldState.copy(
                    restoreMnemonicState = oldState.restoreMnemonicState?.copy(
                        update = !oldState.restoreMnemonicState.update,
                        enteredMnemonic = mnemonicFromText.take(24).toMutableList().apply {
                            while (this.count() != 24) {
                                add("")
                            }
                        },
                        deriveWalletEnabled = Mnemonic.isValidMnemonic(mnemonicFromText)
                    )
                )
            }
            is WalletAction.MnemonicWordEdited -> {
                val newEnteredMnemonic = oldState.restoreMnemonicState?.enteredMnemonic?.apply {
                    this[action.index] = action.newText
                } ?: listOf()

                newState = oldState.copy(
                    restoreMnemonicState = oldState.restoreMnemonicState?.copy(
                        update = !oldState.restoreMnemonicState.update,
                        enteredMnemonic = newEnteredMnemonic.toMutableList(),
                        deriveWalletEnabled = Mnemonic.isValidMnemonic(newEnteredMnemonic)
                    )
                )
            }
            is WalletAction.PrivateKeyTitleEdited -> {
                val oldRestorePrivateKeyState = oldState.restorePrivateKeyState

                newState = oldState.copy(
                    restorePrivateKeyState = oldRestorePrivateKeyState?.copy(
                        resultPrivateKeyTitle = action.newTitle,
                        privateKeyIsValid = PrivateKey.isValid(oldRestorePrivateKeyState.enteredPrivateKey)
                    )
                )
            }
            is WalletAction.PastePrivateKeyFromClipboard -> {
                newState = oldState.copy(
                    restorePrivateKeyState = oldState.restorePrivateKeyState?.copy(
                        enteredPrivateKey = action.text,
                        privateKeyIsValid = PrivateKey.isValid(action.text)
                    )
                )
            }
            is WalletAction.PrivateKeyEdited -> {
                newState = oldState.copy(
                    restorePrivateKeyState = oldState.restorePrivateKeyState?.copy(
                        enteredPrivateKey = action.newPrivateKey,
                        privateKeyIsValid = PrivateKey.isValid(action.newPrivateKey)
                    )
                )
            }
            is WalletAction.RestoreFromPrivateKey -> {
                val selectedNetworks = resultNetworks.filter { it.selected }.map { it.network }
                var privateKey = state.value.restorePrivateKeyState?.enteredPrivateKey
                val privateKeyTitle = state.value.restorePrivateKeyState!!.resultPrivateKeyTitle

                if (privateKey != null) {
                    if (privateKey.lowercase().startsWith("0x")) {
                        privateKey = privateKey.substring(2)
                    }

                    val createAddressMethod = CreateAddressMethod.FromPrivateKey(
                        privateKey = privateKey,
                        privateKeyTitle = privateKeyTitle
                    )

                    val allAddresses = interactor.getAllAccounts()
                    val addresses = selectedNetworks.map { network ->
                        createAddressMethod.create(network, allAddresses)
                    }

                    newState = oldState.copy(
                        deriveWalletState = DeriveWalletState(
                            generating = false,
                            derivationHDPath = null,
                            resultAddresses = addresses,
                            title = privateKeyTitle,
                            createAddressMethod = createAddressMethod
                        )
                    )
                }
            }
            WalletAction.DeriveWallet -> {
                val mnemonicTitle = when (walletAction) {
                    WalletAction.CreateWallet -> {
                        oldState.generatedMnemonicState?.resultMnemonicTitle
                    }
                    WalletAction.RestoreWalletByMnemonic -> {
                        oldState.restoreMnemonicState?.resultMnemonicTitle
                    }
                    else -> null
                }
                val mnemonicResult: MnemonicResult? = when (walletAction) {
                    WalletAction.CreateWallet -> {
                        oldState.generatedMnemonicState?.mnemonicResult
                    }
                    WalletAction.RestoreWalletByMnemonic -> {
                        val enteredMnemonic = oldState.restoreMnemonicState?.enteredMnemonic?.filter { it.isNotEmpty() }
                        val entropy = Mnemonic.toEntropy(enteredMnemonic)
                        if (entropy != null || enteredMnemonic == null) {
                            MnemonicResult(
                                entropy = entropy!!,
                                mnemonic = enteredMnemonic!!
                            )
                        } else {
                            null
                        }
                    }
                    else -> null
                }

                mnemonicResult?.let { safeMnemonicResult ->
                    val createAddressMethod = CreateAddressMethod.FromMnemonic(
                        mnemonicResult = safeMnemonicResult,
                        mnemonicTitle = mnemonicTitle!!,
                        hdPath = 0
                    )

                    val createdAddresses = createAddresses(
                        networks = resultNetworks.filter { it.selected }.map { it.network },
                        createAddressMethod = createAddressMethod
                    )

                    //todo load balances from addresses
                    newState = oldState.copy(
                        deriveWalletState = DeriveWalletState(
                            generating = false,
                            derivationHDPath = 0,
                            resultAddresses = createdAddresses,
                            title = mnemonicTitle,
                            createAddressMethod = createAddressMethod
                        )
                    )
                }
            }
            is WalletAction.HDPathChanged -> {
                newState = oldState.copy(
                    deriveWalletState = oldState.deriveWalletState?.copy(
                        generating = true,
                        derivationHDPath = action.hdPath,
                        resultAddresses = emptyList()
                    )
                )

                launch {
                    val createAddressMethod = (oldState.deriveWalletState!!.createAddressMethod as CreateAddressMethod.FromMnemonic).copy(
                        hdPath = action.hdPath
                    )

                    val createdAddresses = createAddresses(
                        networks = resultNetworks.filter { it.selected }.map { it.network },
                        createAddressMethod = createAddressMethod
                    )

                    state.tryEmit(
                        state.value.copy(
                            deriveWalletState = oldState.deriveWalletState.copy(
                                generating = false,
                                derivationHDPath = action.hdPath,
                                resultAddresses = createdAddresses
                            )
                        )
                    )
                }
            }
            WalletAction.SaveGeneratedAddressesButtonClicked -> {
                val createAddressMethod = state.value.deriveWalletState?.createAddressMethod
                val addressesToSave = state.value.deriveWalletState?.resultAddresses
                val mnemonicTitleFromState = state.value.deriveWalletState?.title

                if (createAddressMethod == null || addressesToSave == null || mnemonicTitleFromState == null) {
                    return
                }

                launch {
                    addressesToSave.forEach { createdAddress ->
                        if (createdAddress.importedStatus is ImportedStatus.ImportedAddress) {
                            return@forEach
                        }

                        val newAccount = createAddressMethod.applyAccount(
                            createdAddress = createdAddress,
                            account = Account.newInstance(id = interactor.getIdForNewAccount())
                        )

                        val addressesFromNetwork = interactor.getAllAccountsByNetwork(createdAddress.network)
                        if (addressesFromNetwork.isEmpty()) {
                            interactor.setSelectedAccount(newAccount.id, createdAddress.network)
                        }

                        interactor.saveAccount(newAccount, createdAddress.network)

                        if (interactor.getCurrentNetwork() == createdAddress.network) {
                            interactor.setSelectedAccount(newAccount.id, createdAddress.network)

                            state.tryEmit(
                                state.value.copy(
                                    currentAccount = interactor.getSelectedAccount()
                                )
                            )
                        }
                    }
                }
            }
            WalletAction.OpenSwitchNetwork -> {
                resultNetworks = getInitSelectionNetworks()

                newState = oldState.copy(
                    switchWalletState = SwitchWalletState(
                        networks = resultNetworks,
                        accounts = emptyList()
                    )
                )

                updateSwitchWallets(interactor.getCurrentNetwork())
            }
            is WalletAction.SwitchNetwork -> {
                resultNetworks.forEach {
                    it.selected = it.network == action.network.network
                }

                interactor.setCurrentNetwork(action.network.network)

                newState = oldState.copy(
                    currentNetwork = action.network.network,
                    currentAccount = interactor.getSelectedAccount(action.network.network),
                    switchWalletState = oldState.switchWalletState?.copy(
                        networks = getNetworksByQuery(oldState.switchWalletState.searchQuery),
                        accounts = emptyList()
                    )
                )

                updateSwitchWallets(action.network.network)
            }
            is WalletAction.SwitchAccount -> {
                state.value.currentNetwork?.let {
                    interactor.setSelectedAccount(action.account.id, it)

                    state.tryEmit(
                        state.value.copy(
                            currentAccount = action.account
                        )
                    )

                    updateSwitchWallets(it)
                }
            }
            is WalletAction.PinCodeDeleteSymbol -> {
                val refreshedPinPurpose = when (val pinPurpose = oldState.pinState.pinPurpose) {
                    is PinPurpose.Check -> {
                        pinPurpose.copy(enteredPin = pinPurpose.enteredPin.dropLast(1))
                    }
                    is PinPurpose.Set -> {
                        if (pinPurpose.firstPinFilled) {
                            pinPurpose.copy(confirmPin = pinPurpose.confirmPin.dropLast(1))
                        } else {
                            pinPurpose.copy(firstPin = pinPurpose.firstPin.dropLast(1))
                        }
                    }
                }

                newState = oldState.copy(
                    pinState = oldState.pinState.copy(
                        pinPurpose = refreshedPinPurpose
                    )
                )
            }
            is WalletAction.PinCodeNewSymbol -> {
                val newPinCodeState = when (val pinPurpose = oldState.pinState.pinPurpose) {
                    is PinPurpose.Set -> {
                        if (pinPurpose.firstPinFilled) {
                            val newConfirmEnteredPinCode = pinPurpose.confirmPin + action.enteredNumber

                            if (newConfirmEnteredPinCode.length != PIN_LENGTH) {
                                oldState.pinState.copy(
                                    pinPurpose = pinPurpose.copy(
                                        confirmPin = newConfirmEnteredPinCode
                                    )
                                )
                            } else {
                                if (newConfirmEnteredPinCode == pinPurpose.firstPin) {
                                    val pinToSave = EncryptHelper.encryptPin(newConfirmEnteredPinCode)
                                    interactor.savePinCode(pinToSave)

                                    oldState.pinState.copy(
                                        pinPurpose = pinPurpose.copy(
                                            confirmPin = newConfirmEnteredPinCode
                                        ),
                                        enterState = PinEnterState.Success
                                    )
                                } else {
                                    oldState.pinState.copy(
                                        pinPurpose = pinPurpose.copy(
                                            firstPin = "",
                                            confirmPin = "",
                                            firstPinFilled = false,
                                            message = "PINs are not equal!\nStart again please."
                                        ),
                                        enterState = PinEnterState.Error
                                    )
                                }
                            }
                        } else {
                            val newFirstEnteredPin = pinPurpose.firstPin + action.enteredNumber
                            oldState.pinState.copy(
                                pinPurpose = pinPurpose.copy(
                                    firstPin = newFirstEnteredPin,
                                    firstPinFilled = newFirstEnteredPin.length == PIN_LENGTH,
                                    message = if (newFirstEnteredPin.length == PIN_LENGTH) {
                                        "Repeat entered PIN\n"
                                    } else {
                                        "Do not use simple sequences.\nNo way to recover this, please remember!"
                                    }
                                ),
                                enterState = PinEnterState.WaitingForEnter
                            )
                        }
                    }
                    is PinPurpose.Check -> {
                        val newEnteredPin = pinPurpose.enteredPin + action.enteredNumber

                        if (newEnteredPin.length == PIN_LENGTH) {
                            val userPin = interactor.getPinCode()

                            val verify = EncryptHelper.verifyPin(newEnteredPin, userPin!!)

                            if (verify) {
                                oldState.pinState.copy(
                                    pinPurpose = pinPurpose.copy(
                                        enteredPin = newEnteredPin,
                                        message = "\n"
                                    ),
                                    enterState = PinEnterState.Success
                                )
                            } else {
                                oldState.pinState.copy(
                                    pinPurpose = pinPurpose.copy(
                                        enteredPin = "",
                                        message = "Entered PIN is not correct!\nTry again."
                                    ),
                                    enterState = PinEnterState.Error
                                )
                            }
                        } else {
                            oldState.pinState.copy(
                                pinPurpose = pinPurpose.copy(
                                    enteredPin = newEnteredPin,
                                )
                            )
                        }
                    }
                }

                newState = oldState.copy(
                    pinState = newPinCodeState
                )
            }
            is WalletAction.RevealAddressSource -> {
                val account = action.account

                val addressSourceString = if (account.fromMnemonic == true) {
                    "Mnemonic"
                } else {
                    "Private key"
                }

                newState = oldState.copy(
                    pinState = oldState.pinState.copy(
                        pinPurpose = PinPurpose.Check(
                            nextRoute = REVEAL_SOURCE_SCREEN_ROUTE,
                            message = "Enter PIN to reveal your $addressSourceString\n"
                        ),
                        enterState = PinEnterState.WaitingForEnter
                    )
                )

                launch {
                    val addressSource = if (account.fromMnemonic == true) {
                        val entropy = EncryptHelper.decrypt(
                            alias = "MNEMONIC_KEY" + account.uuid,
                            resource = account.resource!!,
                            spec = account.spec!!
                        )

                        AddressSource.Mnemonic(Mnemonic.getRandomMnemonic(HexUtils.toBytes(entropy)))
                    } else {
                        var privateKey = EncryptHelper.decrypt(
                            alias = "PRIVATE_KEY" + account.uuid,
                            resource = account.resource!!,
                            spec = account.spec!!
                        )

                        if (!privateKey.startsWith("0x") && !privateKey.startsWith("0X")) {
                            privateKey = "0x$privateKey"
                        }

                        AddressSource.PrivateKey(privateKey)
                    }

                    state.tryEmit(state.value.copy(
                        revealSourceState = RevealSourceState(
                            account = account,
                            addressSource = addressSource
                        )
                    ))
                }
            }
            is WalletAction.DeleteAddress -> {
                interactor.deleteAccount(action.account.id)

                newState = oldState.copy(
                    currentAccount = null
                )
            }
            is WalletAction.DeleteSource -> {
                interactor.deleteAccountsBySource(action.account.sourceTitle)

                newState = oldState.copy(
                    currentAccount = null
                )
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "WalletStore", message = "NewState: $newState")
            state.value = newState
        }
    }

    private fun createMnemonic(): MnemonicResult {
        val entropy = Entropy.getEntropy()
        return MnemonicResult(
            entropy = entropy,
            mnemonic = Mnemonic.getRandomMnemonic(entropy)
        )
    }

    private fun createAddresses(
        networks: List<Network>,
        createAddressMethod: CreateAddressMethod
    ): List<CreatedOrRestoredAddress> {
        val allAccounts = interactor.getAllAccounts()

        return networks.map { network ->
            createAddressMethod.create(network, allAccounts)
        }
    }

    private fun getInitSelectionNetworks(): List<NetworkWithSelection> {
        return mockNetworks.map {
            NetworkWithSelection(
                selected = it == state.value.currentNetwork,
                network = it
            )
        }
    }

    private fun updateSwitchWallets(network: Network) {
        launch {
            val selectedAccountId = interactor.getSelectedAccount(network)

            val accounts = interactor.getAllAccountsByNetwork(network).map {
                AccountWithSelection(
                    selected = it.id == selectedAccountId?.id,
                    account = it
                )
            }

            state.tryEmit(
                state.value.copy(
                    switchWalletState = state.value.switchWalletState?.copy(
                        accounts = accounts
                    )
                )
            )
        }
    }

    private fun initSelectNetworks(
        description: String,
        action: String,
        nextRoute: String
    ) {
        resultNetworks = getInitSelectionNetworks()

        state.tryEmit(state.value.copy(
            addressSelectionState = AddressSelectionState(
                description = description,
                displayedNetworks = resultNetworks,
                actionButtonEnabled = resultNetworks.any { it.selected },
                action = action,
                selectedCount = 1,
                nextRoute = nextRoute
            )
        ))
    }

    private fun getNetworksByQuery(query: String?): List<NetworkWithSelection> {
        return resultNetworks.filter {
            it.network.pretty_name.startsWith(query ?: "", true)
        }
    }

    private fun getSelectedNetworksCount(): Int {
        return resultNetworks.filter { it.selected }.size
    }

}