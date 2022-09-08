package com.mobileweb3.cosmostools.android.screens.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mobileweb3.cosmostools.android.screens.wallet.views.EnterPinCode
import com.mobileweb3.cosmostools.android.ui.composables.FillSpacer
import com.mobileweb3.cosmostools.android.ui.composables.Toolbar
import com.mobileweb3.cosmostools.resources.Routes.PIN_CODE_SCREEN_ROUTE
import com.mobileweb3.cosmostools.wallet.PinEnterState
import com.mobileweb3.cosmostools.wallet.PinPurpose
import com.mobileweb3.cosmostools.wallet.WalletAction
import com.mobileweb3.cosmostools.wallet.WalletStore

@Composable
fun PinCodeScreen(
    navController: NavHostController,
    walletStore: WalletStore
) {
    val state = walletStore.observeState().collectAsState()
    val pinCodeState = state.value.pinState
    val pinCodePurpose = state.value.pinState.pinPurpose

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(title = pinCodePurpose.title, navController)

        FillSpacer()

        Row {
            FillSpacer()

            when (pinCodePurpose) {
                is PinPurpose.Set -> {
                    if (pinCodePurpose.firstPinFilled) {
                        EnterPinCode(pinCodePurpose.confirmPin, pinCodeState)
                    } else {
                        EnterPinCode(pinCodePurpose.firstPin, pinCodeState)
                    }
                }
                is PinPurpose.Check -> {
                    EnterPinCode(pinCodePurpose.enteredPin, pinCodeState)
                }
            }

            if (pinCodeState.enterState is PinEnterState.Success) {
                if (navController.currentBackStackEntry?.destination?.route == PIN_CODE_SCREEN_ROUTE) {
                    navController.popBackStack()
                    navController.navigate(pinCodeState.pinPurpose.nextRoute)
                }
            }

            FillSpacer()
        }

        FillSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            NumberView(number = 1, walletStore = walletStore)

            NumberView(number = 2, walletStore = walletStore)

            NumberView(number = 3, walletStore = walletStore)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            NumberView(number = 4, walletStore = walletStore)

            NumberView(number = 5, walletStore = walletStore)

            NumberView(number = 6, walletStore = walletStore)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            NumberView(number = 7, walletStore = walletStore)

            NumberView(number = 8, walletStore = walletStore)

            NumberView(number = 9, walletStore = walletStore)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) { }

            NumberView(number = 0, walletStore = walletStore)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        walletStore.dispatch(WalletAction.PinCodeDeleteSymbol)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FillSpacer()
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Delete",
                    modifier = Modifier.weight(1f)
                )
                FillSpacer()
            }
        }
    }
}

@Composable
fun RowScope.NumberView(
    number: Int,
    walletStore: WalletStore
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable {
                walletStore.dispatch(WalletAction.PinCodeNewSymbol(number))
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FillSpacer()
        Text(
            text = number.toString(),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h4
        )
        FillSpacer()
    }
}