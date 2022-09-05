package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.PrimaryColor
import com.mobileweb3.cosmostools.android.ui.PrimaryColorAlpha50
import com.mobileweb3.cosmostools.android.ui.WarningColor
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.wallet.PinEnterState
import com.mobileweb3.cosmostools.wallet.PinState

@Composable
fun EnterPinCode(
    enteredPinCode: String,
    pinState: PinState
) {
    Column {
        Row(
            modifier = Modifier.height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FillSpacer()

            for (index in 0..3) {
                val symbol = enteredPinCode.getOrNull(index)
                PinCodeCell(symbol != null)
            }

            FillSpacer()
        }

        Text(
            text = pinState.pinPurpose.message,
            textAlign = TextAlign.Center,
            color = if (pinState.enterState is PinEnterState.Error) {
                WarningColor
            } else {
                Color.White
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PinCodeCell(filled: Boolean) {
    Column(
        modifier = Modifier.width(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
                .background(
                    color = if (filled) {
                        PrimaryColor
                    } else {
                        PrimaryColorAlpha50
                   },
                    shape = RoundedCornerShape(100.dp)
                )
        )
    }
}

@Composable
private fun NumberPinCodeCell(number: Char) {
    Column(
        modifier = Modifier.width(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number.toString(),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h4,
            color = PrimaryColor
        )
    }
}
