package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.crypto.Network
import com.mobileweb3.cosmostools.crypto.mockNetworks
import com.mobileweb3.cosmostools.wallet.displayedAddress

@Composable
fun AccountCard(
    account: Account,
    clickable: Boolean = true,
    modifier: Modifier = Modifier,
    borderColor: Color,
    onAccountClicked: ((Account) -> Unit)?
) {
    //todo выпилится, когда сети будут приходить с сервака
    val accountNetwork = mockNetworks.find { it.pretty_name == account.network }!!

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .background(
                brush = getGradientBrush(null, accountNetwork.getLogo()),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(clickable) { onAccountClicked?.invoke(account) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            VerticalSpacer()

            AccountAddress(account.address)

            VerticalSpacer(4.dp)

            Text(text = "0.000000 ${accountNetwork.assets[0].symbol}")

            VerticalSpacer()

            Text(
                text = account.mnemonicTitle ?: "",
                maxLines = 1
            )

            VerticalSpacer(4.dp)

            Text(text = account.fullDerivationPath ?: "")

            VerticalSpacer()
        }
    }
}