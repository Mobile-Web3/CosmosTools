package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val vector: ImageVector, val route: String) {

    object Validators : BottomNavItem("Validators", Icons.Filled.People, "validators")
    object Stats : BottomNavItem("Stats", Icons.Filled.QueryStats, "stats")
    object Tools : BottomNavItem("Tools", Icons.Filled.Menu, "tools")
    object Wallet : BottomNavItem("Wallet", Icons.Filled.Wallet, "wallet")
}