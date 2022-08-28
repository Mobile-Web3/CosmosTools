package com.mobileweb3.cosmostools.android.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun disableScreenshot() {
    LocalContext.current.getActivity()?.window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
}

@Composable
fun enableScreenshot() {
    LocalContext.current.getActivity()?.window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}