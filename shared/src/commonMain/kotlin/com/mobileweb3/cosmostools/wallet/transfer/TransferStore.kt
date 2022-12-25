package com.mobileweb3.cosmostools.wallet.transfer

import com.mobileweb3.cosmostools.app.Action
import com.mobileweb3.cosmostools.app.Effect
import com.mobileweb3.cosmostools.app.Store
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


}

sealed class TransferSideEffect : Effect {
    data class Message(val text: String) : TransferSideEffect()
}

class TransferStore(
    private val interactor: WalletInteractor
) : Store<TransferState, TransferAction, TransferSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        TransferState(
            data = TransferData(
                from = null
            )
        )
    )
    private val sideEffect = MutableSharedFlow<TransferSideEffect>()

    init {
        launch {
            state.tryEmit(
                value = state.value.copy(
                    data = state.value.data.copy(
                        from = interactor.getSelectedAccount()
                    )
                )
            )
        }
    }

    override fun observeState(): StateFlow<TransferState> {
        return state
    }

    override fun observeSideEffect(): Flow<TransferSideEffect> = sideEffect

    override fun dispatch(action: TransferAction) {
        Napier.d(tag = "TransferStore", message = "Action: $action")

        val oldState = state.value

        var newState = oldState

//        when (action) {
//
//        }

        if (newState != oldState) {
            Napier.d(tag = "TransferStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}