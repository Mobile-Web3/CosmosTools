package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.wallet.NetworkWithSelection

@Composable
fun SelectNetworksGrid(
    networks: List<NetworkWithSelection>?,
    modifier: Modifier = Modifier,
    columnsCount: Int,
    onNetworkClicked: (NetworkWithSelection, Boolean) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        modifier = modifier,
    ) {
        items(networks?.toViewItems() ?: emptyList()) { item ->
            when (item) {
                is CreateWalletNetworkViewItem.Empty -> {

                }
                is CreateWalletNetworkViewItem.Data -> {
                    val selectedColor = if (item.networkWithSelection.selected) {
                        SelectedColor
                    } else {
                        MaterialTheme.colors.primary
                    }
                    NetworkCard(
                        network = item.networkWithSelection.network,
                        modifier = Modifier.padding(2.dp),
                        selectedColor = selectedColor,
                        onNetworkClicked = {
                            onNetworkClicked.invoke(item.networkWithSelection, !item.networkWithSelection.selected)
                        }
                    )
                }
            }
        }
    }
}

private fun List<NetworkWithSelection>.toViewItems(): List<CreateWalletNetworkViewItem> {
    if (this.isEmpty()) {
        return listOf(CreateWalletNetworkViewItem.Empty)
    }

    return map { CreateWalletNetworkViewItem.Data(it) }
}