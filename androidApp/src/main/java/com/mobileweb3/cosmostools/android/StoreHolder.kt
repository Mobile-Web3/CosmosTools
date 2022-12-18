package com.mobileweb3.cosmostools.android

import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore

class StoreHolder(
    val walletStore: WalletStore,
    val transferStore: TransferStore
)