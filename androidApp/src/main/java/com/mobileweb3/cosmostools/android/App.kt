package com.mobileweb3.cosmostools.android

import android.R.attr.password
import android.R.attr.path
import android.app.Application
import com.mobileweb3.cosmostools.core.create
import com.mobileweb3.cosmostools.core.keyStore
import com.mobileweb3.cosmostools.network.api
import com.mobileweb3.cosmostools.repository.BalancesRepository
import com.mobileweb3.cosmostools.repository.NetworksRepository
import com.mobileweb3.cosmostools.wallet.WalletInteractor
import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.transfer.TransferInteractor
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import java.io.FileInputStream
import java.security.KeyStore


class App : Application() {

    private val appModule = module {
        single { BalancesRepository(api) }
        single { NetworksRepository(api) }
        single { WalletInteractor.create(get(), get(), get(), BuildConfig.DEBUG) }
        single { TransferInteractor(api, get()) }
        single { WalletStore(interactor = get()) }
        single { TransferStore(walletInteractor = get(), transferInteractor = get()) }
    }

    override fun onCreate() {
        super.onCreate()

        initKeyStore()
        initKoin()
    }

    private fun initKeyStore() {
        var caFileInputStream = applicationContext.resources.openRawResource(R.raw.certificate)

        val keyStorePassword = "foobar".toCharArray()

        keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            //load(null)
            FileInputStream(path).use { inputStream ->
                load(
                    inputStream,
                    keyStorePassword
                )
            }
            //load(caFileInputStream, keyStorePassword)
        }
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}