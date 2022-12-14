package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.mobileweb3.cosmostools.network.response.NetworkResponse

@Composable
fun NetworkCard(
    network: NetworkResponse,
    clickable: Boolean = true,
    modifier: Modifier = Modifier,
    borderColor: Color,
    onPaletteChanged: ((Palette) -> Unit)?,
    onNetworkClicked: ((NetworkResponse) -> Unit)?
) {
    var palette by remember { mutableStateOf<Palette?>(null) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .background(
                brush = getGradientBrush(palette, network.getLogo()),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(clickable) { onNetworkClicked?.invoke(network) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer()

            Avatar(
                url = network.getLogo(),
                widthHeightDp = 50.dp,
                onPaletteChanged = {
                    palette = it
                    onPaletteChanged?.invoke(it)
                }
            )

            VerticalSpacer()

            NetworkTitle(
                network = network,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = borderColor
            )
        }
    }
}