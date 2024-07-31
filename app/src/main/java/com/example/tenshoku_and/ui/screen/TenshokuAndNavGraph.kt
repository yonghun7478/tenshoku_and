package com.example.tenshoku_and.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope

@Composable
fun TenshokuAndNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = TenshokuAndDestinations.MAIN_ROUTE,
    navAction: TenshokuAndNavigationActions = remember(navController) {
        TenshokuAndNavigationActions(navController)
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
        composable(TenshokuAndDestinations.MAIN_ROUTE) {
            MainScreen(nextScreen = {
                navAction.navigateToSecondScreen("test")
            })
        }
        composable(
            TenshokuAndDestinations.SECOND_ROUTE,
            arguments = listOf(
                navArgument(TenshokuAndArgs.USER_NAME_ARGS) {
                    type = NavType.StringType; defaultValue = ""
                }
            )
        ) {entry ->
            val name:String = entry.arguments?.getString(TenshokuAndArgs.USER_NAME_ARGS) ?: ""
            SecondScreen(name = name)
        }
    }
}