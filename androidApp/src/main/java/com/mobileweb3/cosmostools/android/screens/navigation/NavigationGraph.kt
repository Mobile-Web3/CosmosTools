package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobileweb3.cosmostools.android.screens.stats.StatsScreen
import com.mobileweb3.cosmostools.android.screens.tools.ToolsScreen
import com.mobileweb3.cosmostools.android.screens.validators.ValidatorsScreen
import com.mobileweb3.cosmostools.android.screens.wallet.CreateWalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.WalletScreen
import com.mobileweb3.cosmostools.app.MainStore
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun NavigationGraph(
    navController: NavHostController,
    walletStore: WalletStore
) {
    NavHost(navController, startDestination = BottomNavItem.Wallet.route) {
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
            WalletScreen(navController, walletStore)
        }
        composable("create_wallet") {
            CreateWalletScreen(walletStore)
        }
    }
}