package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(dp: Dp = 8.dp) {
    Spacer(modifier = Modifier.width(dp))
}