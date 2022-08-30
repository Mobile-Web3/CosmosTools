package com.mobileweb3.cosmostools.shared

import java.lang.System

actual object System {

    actual fun getCurrentMillis(): Long {
        return System.currentTimeMillis()
    }
}