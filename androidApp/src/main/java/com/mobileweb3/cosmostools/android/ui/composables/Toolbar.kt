package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun Toolbar(
    title: String?,
    navController: NavHostController? = null,
    backToRoute: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (navController != null) {
            IconButton(
                onClick = {
                    if (backToRoute == null) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack(backToRoute, false)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )
            }
        }

        FillSpacer()

        Text(
            text = title ?: "",
            color = Color.White,
            style = MaterialTheme.typography.h6
        )

        FillSpacer()

        if (navController != null) {
            //for balance padding
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.Black,
                    contentDescription = "Back"
                )
            }
        }
    }
}