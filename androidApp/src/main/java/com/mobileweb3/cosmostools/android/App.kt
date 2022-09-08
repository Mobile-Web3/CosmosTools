package com.mobileweb3.cosmostools.android

import android.app.Application
import com.mobileweb3.cosmostools.core.create
import com.mobileweb3.cosmostools.wallet.WalletInteractor
import com.mobileweb3.cosmostools.wallet.WalletStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    private val appModule = module {
        single { WalletInteractor.create(get(), BuildConfig.DEBUG) }
        single { WalletStore(interactor = get()) }
    }

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}