package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Toolbar(
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        FillSpacer()

        Text(
            text = title,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h5
        )

        FillSpacer()
    }
}