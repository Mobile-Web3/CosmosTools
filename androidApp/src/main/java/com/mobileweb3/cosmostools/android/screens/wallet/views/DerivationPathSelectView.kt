package com.mobileweb3.cosmostools.android.screens.wallet.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmostools.resources.Constants.DERIVATION_PATHS
import com.mobileweb3.cosmostools.resources.Strings.SELECT_DERIVE_PATH_MESSAGE

@Composable
fun DerivationPathSelectView(
    hdPath: Int?,
    onPathSelected: (Int) -> Unit
) {
    val path = remember { mutableStateOf(hdPath ?: 0) }
    val isOpen = remember { mutableStateOf(false) }
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (Int) -> Unit = {
        path.value = it
        onPathSelected.invoke(it)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            OutlinedTextField(
                readOnly = true,
                value = SELECT_DERIVE_PATH_MESSAGE + path.value,
                onValueChange = { path.value = it.toInt() },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownHDPathSelection(
                requestToOpen = isOpen.value,
                paths = DERIVATION_PATHS,
                openCloseOfDropDownList,
                userSelectedString
            )
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}