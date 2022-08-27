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
    val currentWallet: String?,
    val createWalletState: CreateWalletState?
) : State

data class CreateWalletState(
    val createWalletNetworks: List<CreateWalletNetwork>,
    val createdAddress: String? = null,
    val mnemonic: List<String>? = null
)

data class CreateWalletNetwork(
    var selected: Boolean,
    val network: Network
)

sealed class WalletAction : Action {
    class SearchNetworkQueryChanged(val query: String): WalletAction()

    class SelectNetworkForCreation(
        val createWalletNetwork: CreateWalletNetwork,
        val selected: Boolean
    ) : WalletAction()

}

sealed class WalletSideEffect : Effect {
    data class Message(val text: String) : WalletSideEffect()
}

class WalletStore(
    private val interactor: WalletInteractor
) : Store<WalletState, WalletAction, WalletSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val resultNetworks = mockNetworks.map {
        CreateWalletNetwork(
            selected = false, // network == currentNetwork,
            network = it
        )
    }
    private val state = MutableStateFlow(
        WalletState(
            currentWallet = interactor.getCurrentWallet(),
            createWalletState = CreateWalletState(
                createWalletNetworks = resultNetworks
            )
        )
    )
    private val sideEffect = MutableSharedFlow<WalletSideEffect>()

    init {
        requestWalletInfo(state.value.currentWallet)
    }

    private fun requestWalletInfo(currentWallet: String?) {
        if (currentWallet == null) {
            return
        }
    }

    override fun observeState(): StateFlow<WalletState> = state

    override fun observeSideEffect(): Flow<WalletSideEffect> = sideEffect

    override fun dispatch(action: WalletAction) {
        Napier.d(tag = "WalletStore", message = "Action: $action")

        val oldState = state.value

        var newState = oldState

        when (action) {
            is WalletAction.SearchNetworkQueryChanged -> {
                newState = oldState.copy(
                    createWalletState = CreateWalletState(
                        createWalletNetworks = resultNetworks.filter {
                            it.network.pretty_name.startsWith(action.query, true)
                        }
                    )
                )
            }
            is WalletAction.SelectNetworkForCreation -> {
                //state.value.createWalletState?.createWalletNetworks?.find { it == action.createWalletNetwork }?.selected = action.selected
                resultNetworks.find { it == action.createWalletNetwork }?.selected = action.selected
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "WalletStore", message = "NewState: $newState")
            state.value = newState
        }
    }

}