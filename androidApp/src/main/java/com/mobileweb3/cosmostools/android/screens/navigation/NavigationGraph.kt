package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobileweb3.cosmostools.android.screens.stats.StatsScreen
import com.mobileweb3.cosmostools.android.screens.tools.ToolsScreen
import com.mobileweb3.cosmostools.android.screens.validators.ValidatorsScreen
import com.mobileweb3.cosmostools.android.screens.wallet.WalletScreen
import com.mobileweb3.cosmostools.app.MainStore

@Composable
fun NavigationGraph(
    navController: NavHostController,
    store: MainStore
) {
    NavHost(navController, startDestination = BottomNavItem.Validators.route) {
        composable(BottomNavItem.Validators.route) {
            ValidatorsScreen()
        }
        composable(BottomNavItem.Stats.route) {
            StatsScreen()
        }
        composable(BottomNavItem.Tools.route) {
            ToolsScreen()
        }
        composable(BottomNavItem.Wallet.route) {
            WalletScreen()
        }
    }
}