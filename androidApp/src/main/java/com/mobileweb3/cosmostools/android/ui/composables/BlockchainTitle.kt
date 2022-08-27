package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.crypto.Network

@Composable
fun NetworkTitle(
    network: Network,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    backgroundColor: Color = MaterialTheme.colors.primary
) {
    Text(
        text = network.pretty_name,
        color = Color.White,
        maxLines = 1,
        textAlign = TextAlign.Center,
        style = textStyle,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
    )
}