package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobileweb3.cosmostools.android.screens.stats.StatsScreen
import com.mobileweb3.cosmostools.android.screens.tools.ToolsScreen
import com.mobileweb3.cosmostools.android.screens.validators.ValidatorsScreen
import com.mobileweb3.cosmostools.android.screens.wallet.DeriveWalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.GeneratedMnemonicScreen
import com.mobileweb3.cosmostools.android.screens.wallet.SelectNetworksScreen
import com.mobileweb3.cosmostools.android.screens.wallet.SwitchNetworkWalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.WalletScreen
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
        composable("select_networks") {
            SelectNetworksScreen(navController, walletStore)
        }
        composable("generated_mnemonic") {
            GeneratedMnemonicScreen(navController, walletStore)
        }
        composable("derive_wallet") {
            DeriveWalletScreen(navController, walletStore)
        }
        composable("switch") {
            SwitchNetworkWalletScreen(navController, walletStore)
        }
    }
}