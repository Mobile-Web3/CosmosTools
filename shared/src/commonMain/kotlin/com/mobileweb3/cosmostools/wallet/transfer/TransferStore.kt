package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.app.Action
import com.mobileweb3.cosmostools.app.Effect
import com.mobileweb3.cosmostools.app.Store
import com.mobileweb3.cosmostools.shared.RequestStatus
import com.mobileweb3.cosmostools.wallet.*
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TransferAction : Action {
    object OnNavigate : TransferAction()

    object RefreshFees : TransferAction()

    class OnFeeSelected(val feeIndex: Int) : TransferAction()

    class OnAddressToEdited(val newAddress: String) : TransferAction()
}

sealed class TransferSideEffect : Effect {
    data class Message(val text: String) : TransferSideEffect()
}

class TransferStore(
    private val walletInteractor: WalletInteractor,
    private val transferInteractor: TransferInteractor,
) : Store<TransferState, TransferAction, TransferSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        TransferState(
            data = TransferData(
                from = null,
                to = ""
            ),
            simulate = RequestStatus.Loading(),
            selectedFeeIndex = 1
        )
    )
    private val sideEffect = MutableSharedFlow<TransferSideEffect>()

    init {
        refreshAccount()
    }

    override fun observeState(): StateFlow<TransferState> {
        return state
    }

    override fun observeSideEffect(): Flow<TransferSideEffect> = sideEffect

    override fun dispatch(action: TransferAction) {
        Napier.d(tag = "TransferStore", message = "Action: $action")

        when (action) {
            TransferAction.OnNavigate -> {
                refreshAccount()
                simulateTransaction()
            }
            TransferAction.RefreshFees -> {
                simulateTransaction()
            }
            is TransferAction.OnFeeSelected -> {
                state.tryEmit(state.value.copy(selectedFeeIndex = action.feeIndex))
            }
            is TransferAction.OnAddressToEdited -> {
                state.tryEmit(state.value.copy(data = state.value.data.copy(to = action.newAddress)))
            }
        }
    }

    private fun refreshAccount() {
        launch {
            val currentAccount = walletInteractor.getSelectedAccount()

            state.tryEmit(
                value = state.value.copy(
                    data = state.value.data.copy(
                        from = currentAccount,
                        to = ""
                    )
                )
            )
        }
    }

    private fun simulateTransaction() {
        state.tryEmit(state.value.copy(simulate = RequestStatus.Loading()))
        launch {
            transferInteractor.simulateTransaction().fold(
                onSuccess = {
                    state.tryEmit(state.value.copy(simulate = RequestStatus.Data(it!!)))
                },
                onFailure = {
                    state.tryEmit(state.value.copy(simulate = RequestStatus.Error(it)))
                }
            )
        }
    }
}