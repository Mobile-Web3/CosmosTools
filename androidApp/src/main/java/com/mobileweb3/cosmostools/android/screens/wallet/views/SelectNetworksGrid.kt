package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.wallet.NetworkWithSelection
import kotlinx.coroutines.launch

@Composable
fun SelectNetworksGrid(
    networks: List<NetworkWithSelection>?,
    scrollToIndex: Int? = null,
    modifier: Modifier = Modifier,
    columnsCount: Int,
    onNetworkClicked: (NetworkWithSelection, Boolean) -> Unit,
) {
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(columnsCount),
        modifier = modifier,
    ) {
        items(networks ?: emptyList()) { item ->
            val selectedColor = if (item.selected) {
                SelectedColor
            } else {
                MaterialTheme.colors.primary
            }
            NetworkCard(
                network = item.network,
                modifier = Modifier.padding(2.dp),
                borderColor = selectedColor,
                onPaletteChanged = null,
                onNetworkClicked = {
                    onNetworkClicked.invoke(item, !item.selected)
                }
            )
        }
    }

    if (scrollToIndex != null) {
        coroutineScope.launch {
            listState.scrollToItem(scrollToIndex)
        }
    }
}