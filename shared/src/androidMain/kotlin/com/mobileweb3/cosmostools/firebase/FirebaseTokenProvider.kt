package com.mobileweb3.cosmostools.firebase

import com.google.firebase.messaging.FirebaseMessaging

actual object FirebaseTokenProvider {

    actual fun get(): String {
        return FirebaseMessaging.getInstance().token.result
    }
}