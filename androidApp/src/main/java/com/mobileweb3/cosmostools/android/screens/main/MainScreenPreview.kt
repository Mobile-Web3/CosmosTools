package com.mobileweb3.cosmostools.android.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mobileweb3.cosmostools.android.ui.AppTheme
import com.mobileweb3.cosmostools.app.MainStore

@Preview
@Composable
private fun MainScreenPreview() {
    AppTheme {
        MainScreen(MainStore())
    }
}