package com.mobileweb3.cosmostools.wallet

import com.mobileweb3.cosmostools.app.Action
import com.mobileweb3.cosmostools.app.Effect
import com.mobileweb3.cosmostools.app.State
import com.mobileweb3.cosmostools.app.Store
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.mockNetworks
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class WalletState(
    val currentNetwork: Network?,
    val currentWallet: String?,
    val createWalletState: CreateWalletState?
) : State

sealed class CreateWalletState(val title: String) {

    data class AddressSelection(
        val description: String,
        val createWalletNetworks: List<CreateWalletNetwork>,
        val action: String,
        val createButtonEnabled: Boolean,
        val selectedCount: Int
    ) : CreateWalletState("Select networks")

    class CreatedWallet(
        val createdAddress: String? = null,
        val mnemonic: List<String>? = null
    ) : CreateWalletState("Created wallet")
}

data class CreateWalletNetwork(
    var selected: Boolean,
    val network: Network
)

sealed class WalletAction : Action {
    object CreateWallet : WalletAction()

    object RestoreWalletByMnemonic : WalletAction()

    object RestoreWalletByPrivateKey : WalletAction()

    class SearchNetworkQueryChanged(val query: String) : WalletAction()

    class SelectNetworkForCreation(
        val createWalletNetwork: CreateWalletNetwork,
        val selected: Boolean
    ) : WalletAction()

    object SelectAllNetworks : WalletAction()

    object UnselectAllNetworks : WalletAction()

    object ActionAfterNetworksSelected : WalletAction()

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
            currentWallet = interactor.getCurrentWallet(),
            createWalletState = null
        )
    )
    private val sideEffect = MutableSharedFlow<WalletSideEffect>()

    private var walletAction: WalletAction? = null
    private var resultNetworks = getInitSelectionNetworks()
    private var currentSearchNetworkQuery = ""

    init {
        requestWalletInfo(state.value.currentWallet)
    }

    private fun requestWalletInfo(currentWallet: String?) {
        if (currentWallet == null) {
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
                    action = "Create"
                )
            }
            WalletAction.RestoreWalletByMnemonic -> {
                walletAction = WalletAction.RestoreWalletByMnemonic

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from entered mnemonic:",
                    action = "Restore"
                )
            }
            WalletAction.RestoreWalletByPrivateKey -> {
                walletAction = WalletAction.RestoreWalletByPrivateKey

                initSelectNetworks(
                    description = "Select networks for which addresses will be restored from private key:",
                    action = "Restore"
                )
            }
            is WalletAction.SearchNetworkQueryChanged -> {
                currentSearchNetworkQuery = action.query

                newState = oldState.copy(
                    createWalletState = (oldState.createWalletState as CreateWalletState.AddressSelection).copy(
                        createWalletNetworks = getNetworksByQuery(),
                        createButtonEnabled = resultNetworks.any { it.selected }
                    )
                )
            }
            is WalletAction.SelectNetworkForCreation -> {
                resultNetworks.find { it == action.createWalletNetwork }?.selected = action.selected

                newState = oldState.copy(
                    createWalletState = (oldState.createWalletState as CreateWalletState.AddressSelection).copy(
                        createWalletNetworks = getNetworksByQuery(),
                        createButtonEnabled = resultNetworks.any { it.selected },
                        selectedCount = getSelectedNetworksCount()
                    )
                )
            }
            is WalletAction.SelectAllNetworks -> {
                resultNetworks.forEach { it.selected = true }

                newState = oldState.copy(
                    createWalletState = (oldState.createWalletState as CreateWalletState.AddressSelection).copy(
                        createWalletNetworks = getNetworksByQuery(),
                        createButtonEnabled = true,
                        selectedCount = resultNetworks.size
                    )
                )
            }
            is WalletAction.UnselectAllNetworks -> {
                resultNetworks.forEach { it.selected = false }

                newState = oldState.copy(
                    createWalletState = (oldState.createWalletState as CreateWalletState.AddressSelection).copy(
                        createWalletNetworks = getNetworksByQuery(),
                        createButtonEnabled = false,
                        selectedCount = 0
                    )
                )
            }
            WalletAction.ActionAfterNetworksSelected -> {

            }
        }

        if (newState != oldState) {
            Napier.d(tag = "WalletStore", message = "NewState: $newState")
            state.value = newState
        }
    }

    private fun getInitSelectionNetworks(): List<CreateWalletNetwork> {
        return mockNetworks.map {
            CreateWalletNetwork(
                selected = it == state.value.currentNetwork,
                network = it
            )
        }
    }

    private fun initSelectNetworks(
        description: String,
        action: String
    ) {
        resultNetworks = getInitSelectionNetworks()

        state.value = WalletState(
            currentNetwork = interactor.getCurrentNetwork(),
            currentWallet = interactor.getCurrentWallet(),
            createWalletState = CreateWalletState.AddressSelection(
                description = description,
                createWalletNetworks = resultNetworks,
                createButtonEnabled = resultNetworks.any { it.selected },
                action = action,
                selectedCount = 1
            )
        )
    }

    private fun getNetworksByQuery(): List<CreateWalletNetwork> {
        return resultNetworks.filter {
            it.network.pretty_name.startsWith(currentSearchNetworkQuery, true)
        }
    }

    private fun getSelectedNetworksCount(): Int {
        return resultNetworks.filter { it.selected }.size
    }

}