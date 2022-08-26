package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.FillSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
fun RowScope.FillSpacer() {
    Spacer(modifier = Modifier.weight(1f))
}