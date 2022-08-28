package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropdownHDPathSelection(
    requestToOpen: Boolean = false,
    paths: List<Int>,
    request: (Boolean) -> Unit,
    onPathSelected: (Int) -> Unit
) {
    DropdownMenu(
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        paths.forEach { path ->
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    onPathSelected(path)
                }
            ) {
                Text(
                    text = path.toString(),
                    modifier = Modifier
                        .wrapContentWidth()
                )
            }
        }
    }
}