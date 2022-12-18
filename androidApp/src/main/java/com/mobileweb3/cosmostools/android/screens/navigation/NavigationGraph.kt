package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobileweb3.cosmostools.android.StoreHolder
import com.mobileweb3.cosmostools.android.screens.stats.StatsScreen
import com.mobileweb3.cosmostools.android.screens.tools.ToolsScreen
import com.mobileweb3.cosmostools.android.screens.validators.ValidatorsScreen
import com.mobileweb3.cosmostools.android.screens.wallet.DeriveWalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.GeneratedMnemonicScreen
import com.mobileweb3.cosmostools.android.screens.wallet.PinCodeScreen
import com.mobileweb3.cosmostools.android.screens.wallet.RestoreMnemonicScreen
import com.mobileweb3.cosmostools.android.screens.wallet.RestorePrivateKeyScreen
import com.mobileweb3.cosmostools.android.screens.wallet.RevealSourceScreen
import com.mobileweb3.cosmostools.android.screens.wallet.SelectNetworksScreen
import com.mobileweb3.cosmostools.android.screens.wallet.SwitchNetworkWalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.WalletScreen
import com.mobileweb3.cosmostools.android.screens.wallet.transfers.TransferScreen
import com.mobileweb3.cosmostools.resources.Routes.DERIVE_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.GENERATED_MNEMONIC_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.PIN_CODE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.RESTORE_MNEMONIC_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.RESTORE_PRIVATE_KEY_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.REVEAL_SOURCE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SELECT_NETWORKS_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE
import com.mobileweb3.cosmostools.resources.Routes.TRANSFER_SCREEN_ROUTE

@Composable
fun NavigationGraph(
    navController: NavHostController,
    storeHolder: StoreHolder
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
            WalletScreen(navController, storeHolder.walletStore)
        }
        composable(SELECT_NETWORKS_SCREEN_ROUTE) {
            SelectNetworksScreen(navController, storeHolder.walletStore)
        }
        composable(GENERATED_MNEMONIC_SCREEN_ROUTE) {
            GeneratedMnemonicScreen(navController, storeHolder.walletStore)
        }
        composable(RESTORE_MNEMONIC_SCREEN_ROUTE) {
            RestoreMnemonicScreen(navController, storeHolder.walletStore)
        }
        composable(RESTORE_PRIVATE_KEY_SCREEN_ROUTE) {
            RestorePrivateKeyScreen(navController, storeHolder.walletStore)
        }
        composable(DERIVE_WALLET_SCREEN_ROUTE) {
            DeriveWalletScreen(navController, storeHolder.walletStore)
        }
        composable(SWITCH_NETWORK_AND_WALLET_SCREEN_ROUTE) {
            SwitchNetworkWalletScreen(navController, storeHolder.walletStore)
        }
        composable(PIN_CODE_SCREEN_ROUTE) {
            PinCodeScreen(navController, storeHolder.walletStore)
        }
        composable(REVEAL_SOURCE_SCREEN_ROUTE) {
            RevealSourceScreen(navController, storeHolder.walletStore)
        }
        composable(TRANSFER_SCREEN_ROUTE) {
            TransferScreen(navController, storeHolder.transferStore)
        }
    }
}