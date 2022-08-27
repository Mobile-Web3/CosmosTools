package com.mobileweb3.cosmostools.android.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xff735095)
val SelectedColor = Color(0xff4bb153)

private val DarkColors = darkColors(
    primary = PrimaryColor,
    onSecondary = Color.White,
    background = Color.Black,
    onPrimary = Color.White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColors,
        content = {
            Surface(content = content)
        }
    )
}