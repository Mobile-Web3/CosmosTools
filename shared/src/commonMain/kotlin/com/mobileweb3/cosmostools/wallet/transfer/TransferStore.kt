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

    object RefreshBalance : TransferAction()

    class OnFeeSelected(val feeIndex: Int) : TransferAction()

    class OnAddressToEdited(val newAddress: String) : TransferAction()

    class OnAmountEdited(val newAmount: String) : TransferAction()

    object Send : TransferAction()
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
                to = "",
                amount = ""
            ),
            simulate = RequestStatus.Loading(),
            balance = RequestStatus.Loading(),
            selectedFeeIndex = 1,
            send = null
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
                refreshBalance()
                simulateTransaction()
            }
            TransferAction.RefreshFees -> {
                simulateTransaction()
            }
            is TransferAction.RefreshBalance -> {
                refreshBalance()
            }
            is TransferAction.OnFeeSelected -> {
                state.tryEmit(state.value.copy(selectedFeeIndex = action.feeIndex))
            }
            is TransferAction.OnAddressToEdited -> {
                state.tryEmit(state.value.copy(data = state.value.data.copy(to = action.newAddress)))
            }
            is TransferAction.OnAmountEdited -> {
                state.tryEmit(state.value.copy(data = state.value.data.copy(amount = action.newAmount)))
            }
            is TransferAction.Send -> {
                sendTransaction()
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
                    ),
                    send = null
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

    private fun refreshBalance() {
        state.tryEmit(state.value.copy(balance = RequestStatus.Loading()))
        launch {
            transferInteractor.getBalance(state.value.data.from?.address).fold(
                onSuccess = {
                    if (it != null) {
                        state.tryEmit(state.value.copy(balance = RequestStatus.Data(it)))
                    } else {
                        state.tryEmit(state.value.copy(balance = RequestStatus.Error(Exception("Error getting balance"))))
                    }
                },
                onFailure = {
                    state.tryEmit(state.value.copy(balance = RequestStatus.Error(it)))
                }
            )
        }
    }

    private fun sendTransaction() {
        if (state.value.data.to.isEmpty()) {
            state.tryEmit(state.value.copy(send = RequestStatus.Error(Exception("Enter valid to address!"))))
            return
        }

        if (state.value.data.amount.isEmpty()) {
            state.tryEmit(state.value.copy(send = RequestStatus.Error(Exception("Enter valid amount!"))))
            return
        }

        state.tryEmit(state.value.copy(send = RequestStatus.Loading()))
        launch {
            val currentAccount = state.value.data.from!!
            val gasData = state.value.simulate.dataOrNull!!
            val gasPrice = when (state.value.selectedFeeIndex) {
                0 -> gasData.lowGasPrice
                1 -> gasData.averageGasPrice
                else -> gasData.highGasPrice
            }

            transferInteractor.sendTransaction(
                amount = state.value.data.amount,
                from = currentAccount.address,
                to = state.value.data.to,
                memo = "",
                key = currentAccount.key,
                chainId = walletInteractor.getCurrentNetwork()!!.chainId,
                gasAdjusted = gasData.gasAdjusted,
                gasPrice = gasPrice
            ).fold(
                onSuccess = {
                    if (it != null) {
                        state.tryEmit(state.value.copy(send = RequestStatus.Data(it)))
                    } else {
                        state.tryEmit(state.value.copy(send = RequestStatus.Error(Exception("Error sending transaction"))))
                    }
                },
                onFailure = {
                    state.tryEmit(state.value.copy(send = RequestStatus.Error(it)))
                }
            )
        }
    }
}