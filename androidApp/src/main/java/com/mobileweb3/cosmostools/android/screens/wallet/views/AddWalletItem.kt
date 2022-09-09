package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.R
import com.mobileweb3.cosmostools.android.ui.composables.VerticalSpacer

@Composable
fun AddWalletItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(vertical = 8.dp)
    ) {
        Icon(painterResource(id = R.drawable.ic_add_wallet), contentDescription = title)

        VerticalSpacer(4.dp)

        Text(text = title)
    }
}