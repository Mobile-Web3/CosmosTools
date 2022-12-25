package com.mobileweb3.cosmostools.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Cacheable<T> {

    private val publicSubscribersFlow = MutableStateFlow<T?>(null)

    private val mutex = Mutex()

    val updates: StateFlow<T?> = publicSubscribersFlow

    val cached get() = publicSubscribersFlow.value

    suspend fun request(checkCache: Boolean = true, update: suspend () -> Result<T>): Result<T> {
        if (checkCache) {
            cached?.let { return Result.success(it) }
        }
        mutex.withLock {
            if (checkCache) {
                cached?.let { return Result.success(it) }
            }
            return update()
                .onSuccess { publicSubscribersFlow.emit(it) }
        }
    }

    fun reset() {
        publicSubscribersFlow.tryEmit(null)
    }
}