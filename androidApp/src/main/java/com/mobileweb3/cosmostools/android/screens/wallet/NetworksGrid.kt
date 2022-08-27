package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.wallet.CreateWalletNetwork

@Composable
fun SelectNetworksGrid(
    networks: List<CreateWalletNetwork>?,
    modifier: Modifier = Modifier,
    onNetworkClicked: (CreateWalletNetwork, Boolean) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
    ) {
        items(networks?.toViewItems() ?: emptyList()) { item ->
            when (item) {
                CreateWalletNetworkViewItem.Empty -> {

                }
                is CreateWalletNetworkViewItem.Data -> {
                    val selectedColor = if (item.createWalletNetwork.selected) {
                        SelectedColor
                    } else {
                        MaterialTheme.colors.primary
                    }
                    NetworkCard(
                        network = item.createWalletNetwork.network,
                        modifier = Modifier.padding(2.dp),
                        selectedColor = selectedColor,
                        onNetworkClicked = {
                            onNetworkClicked.invoke(item.createWalletNetwork, !item.createWalletNetwork.selected)
                        }
                    )
                }
            }
        }
    }
}

private fun List<CreateWalletNetwork>.toViewItems(): List<CreateWalletNetworkViewItem> {
    if (this.isEmpty()) {
        return listOf(CreateWalletNetworkViewItem.Empty)
    }

    return map { CreateWalletNetworkViewItem.Data(it) }
}