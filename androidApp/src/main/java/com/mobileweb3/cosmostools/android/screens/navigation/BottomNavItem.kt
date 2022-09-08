package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.mobileweb3.cosmostools.resources.Routes.STATS_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.TOOLS_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.VALIDATORS_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Strings.STATS_SCREEN_TITLE
import com.mobileweb3.cosmostools.resources.Strings.TOOLS_SCREEN_TITLE
import com.mobileweb3.cosmostools.resources.Strings.VALIDATORS_SCREEN_TITLE
import com.mobileweb3.cosmostools.resources.Strings.WALLET_SCREEN_TITLE

sealed class BottomNavItem(val title: String, val vector: ImageVector, val route: String) {

    object Validators : BottomNavItem(VALIDATORS_SCREEN_TITLE, Icons.Filled.People, VALIDATORS_SCREEN_ROUTE)
    object Stats : BottomNavItem(STATS_SCREEN_TITLE, Icons.Filled.QueryStats, STATS_SCREEN_ROUTE)
    object Tools : BottomNavItem(TOOLS_SCREEN_TITLE, Icons.Filled.Menu, TOOLS_SCREEN_ROUTE)
    object Wallet : BottomNavItem(WALLET_SCREEN_TITLE, Icons.Filled.Wallet, WALLET_SCREEN_ROUTE)
}