package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.android.ui.SelectedColor
import com.mobileweb3.cosmostools.android.ui.composables.AccountCard
import com.mobileweb3.cosmostools.android.ui.composables.NetworkCard
import com.mobileweb3.cosmostools.core.entity.Account
import com.mobileweb3.cosmostools.wallet.AccountWithSelection
import com.mobileweb3.cosmostools.wallet.NetworkWithSelection

@Composable
fun SelectAccountsGrid(
    accounts: List<AccountWithSelection>?,
    modifier: Modifier = Modifier,
    columnsCount: Int,
    onAccountClicked: (Account, Boolean) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsCount),
        modifier = modifier,
    ) {
        items(accounts ?: emptyList()) { item ->
            val selectedColor = if (item.selected) {
                SelectedColor
            } else {
                MaterialTheme.colors.primary
            }
            AccountCard(
                network = item.network,
                account = item.account,
                showOptions = false,
                modifier = Modifier.padding(2.dp),
                borderColor = selectedColor,
                onAccountClicked = {
                    onAccountClicked.invoke(item.account, !item.selected)
                }
            )
        }
    }
}