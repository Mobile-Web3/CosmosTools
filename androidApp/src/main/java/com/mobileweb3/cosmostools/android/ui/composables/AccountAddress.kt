package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mobileweb3.cosmostools.wallet.displayedAddress

@Composable
fun AccountAddress(
    address: String?
) {
    Text(
        text = address?.displayedAddress() ?: "",
        maxLines = 1,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}