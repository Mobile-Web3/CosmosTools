package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.WarningColor

@Composable
fun WarningTextView(text: String) {
    Text(
        text = text,
        color = WarningColor,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}