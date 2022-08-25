package com.mobileweb3.cosmostools.android.screens.navigation

import androidx.annotation.DrawableRes
import com.mobileweb3.cosmostools.android.R

sealed class BottomNavItem(val title: String, @DrawableRes val iconRes: Int, val route: String) {

    object Validators : BottomNavItem("Validators", R.drawable.ic_bottom_validators, "validators")
    object Stats : BottomNavItem("Stats", R.drawable.ic_bottom_stats, "stats")
    object Tools : BottomNavItem("Tools", R.drawable.ic_bottom_tools, "tools")
    object Wallet : BottomNavItem("Wallet", R.drawable.ic_bottom_wallet, "wallet")
}