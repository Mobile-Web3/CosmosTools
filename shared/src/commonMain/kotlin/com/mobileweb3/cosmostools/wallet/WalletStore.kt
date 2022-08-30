package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.Action
import com.mobileweb3.cosmostools.app.Effect
import com.mobileweb3.cosmostools.app.Store
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Address
import com.mobileweb3.cosmostools.crypto.EncryptHelper
import com.mobileweb3.cosmostools.crypto.EncryptHelper.getEncData
import com.mobileweb3.cosmostools.crypto.EncryptHelper.getIvData
import com.mobileweb3.cosmostools.crypto.Entropy
import com.mobileweb3.cosmostools.crypto.Mnemonic
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.Utils
import com.mobileweb3.cosmostools.crypto.mockNetworks
import com.mobileweb3.cosmostools.shared.System
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

    class SelectNetworkForCreation(
        val createWalletNetwork: NetworkWithSelection,
        val selected: Boolean
    ) : WalletAction()

    object SelectAllNetworks : WalletAction()

    object UnselectAllNetworks : WalletAction()

    object ActionAfterNetworksSelected : WalletAction()

    class MnemonicTitleChanged(val newTitle: String) : WalletAction()

    object DeriveWallet : WalletAction()

    class HDPathChanged(val hdPath: Int) : WalletAction()

    object SaveGeneratedAddressesButtonClicked : WalletAction()

    object OpenSwitchNetwork : WalletAction()

    class SwitchNetwork(val network: NetworkWithSelection) : WalletAction()

    class SwitchAccount(val account: Account) : WalletAction()

}

sealed class WalletSideEffect : Effect {
    data class Message(val text: String) : WalletSideEffect()
}

class WalletStore(
    private val interactor: WalletInteractor
) : Store<WalletState, WalletAction, WalletSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        WalletState(
            currentNetwork = interactor.getCurrentNetwork(),
            currentAccount = interactor.getSelectedAccount(),
            addressSelectionState = null,
            switchWalletState = null
        )
    )
    private val sideEffect = MutableSharedFlow<WalletSideEffect>()

    private var walletAction: WalletAction? = null
    private var resultNetworks = getInitSelectionNetworks()

    init {
        requestWalletInfo(state.value.currentAccount)
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
                walletAction = WalletAction.CreateWallet

                initSelectNetworks(
                    description = "Select networks for which addresses will be created from a single mnemonic:",
                    action = "Create",
                    nextRoute = "generated_mnemonic"
                )
            }
            WalletAction.RestoreWalletByMnemonic -> {
                walletAction = WalletAction.RestoreWalletByMnemonic

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from entered mnemonic:",
                    action = "Restore",
                    nextRoute = ""
                )
            }
            WalletAction.RestoreWalletByPrivateKey -> {
                walletAction = WalletAction.RestoreWalletByPrivateKey

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from private key:",
                    action = "Restore",
                    nextRoute = ""
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

                if (networksByQuery.none { it.network == interactor.getCurrentNetwork()}) {
                    if (networksByQuery.isNotEmpty()) {
                        updateSwitchWallets(networksByQuery[0].network)
                    }
                }
            }
            is WalletAction.SelectNetworkForCreation -> {
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
                            )
                        )
                    }
                    WalletAction.RestoreWalletByMnemonic -> {

                    }
                    WalletAction.RestoreWalletByPrivateKey -> {

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
                    )
                )
            }
            WalletAction.DeriveWallet -> {
                val createdAddresses = createAddresses(
                    networks = resultNetworks.filter { it.selected }.map { it.network },
                    mnemonic = oldState.generatedMnemonicState!!.mnemonicResult,
                    hdPath = 0
                )

                newState = oldState.copy(
                    deriveWalletState = DeriveWalletState(
                        generating = false,
                        derivationHDPath = 0,
                        resultAddresses = createdAddresses
                    )
                )
            }
            is WalletAction.HDPathChanged -> {
                newState = oldState.copy(
                    deriveWalletState = DeriveWalletState(
                        generating = true,
                        derivationHDPath = action.hdPath,
                        resultAddresses = emptyList()
                    )
                )

                launch {
                    val createdAddresses = createAddresses(
                        networks = resultNetworks.filter { it.selected }.map { it.network },
                        mnemonic = oldState.generatedMnemonicState!!.mnemonicResult,
                        hdPath = action.hdPath
                    )

                    state.tryEmit(
                        state.value.copy(
                            deriveWalletState = DeriveWalletState(
                                generating = false,
                                derivationHDPath = action.hdPath,
                                resultAddresses = createdAddresses
                            )
                        )
                    )
                }
            }
            WalletAction.SaveGeneratedAddressesButtonClicked -> {
                val mnemonicState = state.value.generatedMnemonicState
                val addressesToSave = state.value.deriveWalletState?.resultAddresses

                if (mnemonicState == null || addressesToSave == null) {
                    return
                }

                launch {
                    addressesToSave.forEach { createdAddress ->
                        val newAccount = Account.newInstance(id = interactor.getIdForNewAccount()).apply {
                            val encryptResult = EncryptHelper.encrypt(
                                alias = "MNEMONIC_KEY" + this.uuid,
                                resource = Utils.byteArrayToHexString(mnemonicState.mnemonicResult.entropy),
                                withAuth = false
                            )

                            resource = encryptResult.getEncData()
                            spec = encryptResult.getIvData()
                            address = createdAddress.address
                            network = createdAddress.network.pretty_name
                            hasPrivateKey = true
                            fromMnemonic = true
                            mnemonicTitle = mnemonicState.resultMnemonicTitle
                            fullDerivationPath = createdAddress.fullDerivationPath
                            derivationHDPath = createdAddress.derivationHDPath
                            mnemonicSize = 24
                            importTime = System.getCurrentMillis()
                            //TODO check customPath from other networks
                            customPath = 0
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

                    state.tryEmit(state.value.copy(
                        currentAccount = action.account
                    ))

                    updateSwitchWallets(it)
                }
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
        mnemonic: MnemonicResult,
        hdPath: Int
    ): List<CreatedAddress> {
        return networks.map {
            val createdAddress = Address.createAddressFromEntropyByNetwork(
                network = it,
                entropy = Utils.byteArrayToHexString(mnemonic.entropy),
                path = hdPath,
                customPath = 0
            )

            CreatedAddress(
                network = it,
                address = createdAddress,
                balance = "0.000000 ${it.assets[0].symbol}",
                derivationHDPath = hdPath,
                fullDerivationPath = "m/44/${it.slip44}/0/0/$hdPath"
            )
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

            val accounts = interactor.getAllAccounts(network).map {
                AccountWithSelection(
                    selected = it.id == selectedAccountId?.id,
                    account = it
                )
            }

            state.tryEmit(state.value.copy(
                switchWalletState = state.value.switchWalletState?.copy(
                    accounts = accounts
                )
            ))
        }
    }

    private fun initSelectNetworks(
        description: String,
        action: String,
        nextRoute: String
    ) {
        resultNetworks = getInitSelectionNetworks()

        state.value = state.value.copy(
            addressSelectionState = AddressSelectionState(
                description = description,
                displayedNetworks = resultNetworks,
                actionButtonEnabled = resultNetworks.any { it.selected },
                action = action,
                selectedCount = 1,
                nextRoute = nextRoute
            )
        )
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