package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun TokitokiNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = TokitokiDestinations.SIGN_IN_ROUTE,
    navAction: TokitokiNavigationActions = remember(navController) {
        TokitokiNavigationActions(navController)
    },
    modifier: Modifier = Modifier,
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(TokitokiDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                registerWithEmailClick = {
                    navAction.navigateToRegisterWithEmail()
                }
            )
        }
        composable(TokitokiDestinations.REGISTER_WITH_EMAIL_ROUTE) {
            RegisterWithEmailScreen(
                registerWithEmailOnClick = {
                    navAction.navigateToEmailVerification()
                }
            )
        }
        composable(TokitokiDestinations.EMAIL_VERIFICATION_ROUTE) {
            EmailVerificationScreen()
        }
    }
}
