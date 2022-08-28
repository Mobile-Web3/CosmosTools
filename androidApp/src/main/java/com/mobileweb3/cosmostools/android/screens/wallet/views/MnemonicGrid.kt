package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.composables.HorizontalSpacer

@Composable
fun MnemonicGrid(words: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(8.dp),
    ) {
        itemsIndexed(words) { index, item ->
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.primary, RoundedCornerShape(10.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalSpacer(dp = 4.dp)

                Text(text = "${index + 1}. ")

                Text(text = item, color = Color.White)
            }
        }
    }
}