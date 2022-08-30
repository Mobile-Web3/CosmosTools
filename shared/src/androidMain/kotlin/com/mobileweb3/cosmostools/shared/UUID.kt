package com.mobileweb3.cosmostools.shared

import java.util.UUID

actual object UUID {

    actual fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}