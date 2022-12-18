package com.mobileweb3.cosmostools.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mobileweb3.cosmostools.android.screens.navigation.BottomNavItem
import com.mobileweb3.cosmostools.android.screens.navigation.BottomNavigation
import com.mobileweb3.cosmostools.android.screens.navigation.NavigationGraph
import com.mobileweb3.cosmostools.android.ui.AppTheme
import com.mobileweb3.cosmostools.wallet.WalletStore
import com.mobileweb3.cosmostools.wallet.transfer.TransferStore
import org.koin.android.ext.android.inject

class AppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
            }

            AppTheme {
                ProvideWindowInsets {
                    val scaffoldState = rememberScaffoldState()


                    val walletStore: WalletStore by inject()
                    val transferStore: TransferStore by inject()
                    val storeHolder = StoreHolder(
                        walletStore = walletStore,
                        transferStore = transferStore
                    )

                    val bottomNavItems = listOf(
                        BottomNavItem.Validators,
                        BottomNavItem.Stats,
                        BottomNavItem.Tools,
                        BottomNavItem.Wallet,
                    )

                    Box(
                        Modifier.padding(
                            rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyStart = true,
                                applyTop = false,
                                applyEnd = true,
                                applyBottom = true
                            )
                        )
                    ) {
                        val navController = rememberNavController()

                        val showBottomBar = navController
                            .currentBackStackEntryAsState().value?.destination?.route in bottomNavItems.map { it.route }

                        Scaffold(
                            scaffoldState = scaffoldState,
                            snackbarHost = { hostState ->
                                SnackbarHost(
                                    hostState = hostState,
                                    modifier = Modifier.padding(
                                        rememberInsetsPaddingValues(
                                            insets = LocalWindowInsets.current.systemBars,
                                            applyBottom = true
                                        )
                                    )
                                )
                            },
                            bottomBar = {
                                if (showBottomBar) {
                                    BottomNavigation(navController = navController, bottomNavItems)
                                }
                            }
                        ) {
                            NavigationGraph(navController = navController, storeHolder)
                        }
                    }
                }
            }
        }
    }
}
