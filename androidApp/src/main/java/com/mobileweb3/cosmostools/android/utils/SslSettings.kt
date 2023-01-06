package com.mobileweb3.cosmostools.android.utils

import android.content.Context
import com.mobileweb3.cosmostools.android.App
import com.mobileweb3.cosmostools.android.R
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

//object SslSettings {
//    fun getKeyStore(): KeyStore {
//        val keyStoreFile = App.openRawResource(R.raw.certificate)
//        val keyStorePassword = "foobar".toCharArray()
//        val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//        keyStore.load(keyStoreFile, keyStorePassword)
//        return keyStore
//    }
//
//    fun getTrustManagerFactory(): TrustManagerFactory? {
//        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//        trustManagerFactory.init(getKeyStore())
//        return trustManagerFactory
//    }
//
//    fun getSslContext(): SSLContext? {
//        val sslContext = SSLContext.getInstance("TLS")
//        sslContext.init(null, getTrustManagerFactory()?.trustManagers, null)
//        return sslContext
//    }
//
//    fun getTrustManager(): X509TrustManager {
//        return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
//    }
//}