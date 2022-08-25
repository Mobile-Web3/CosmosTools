package com.mobileweb3.cosmostools.app

import com.mobileweb3.cosmostools.core.wrap

fun MainStore.watchState() = observeState().wrap()
fun MainStore.watchSideEffect() = observeSideEffect().wrap()