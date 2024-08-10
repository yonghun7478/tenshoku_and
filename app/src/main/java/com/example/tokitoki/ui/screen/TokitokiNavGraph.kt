package com.example.tokitoki.ui.screen

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
import androidx.navigation.navDeepLink
import kotlinx.coroutines.CoroutineScope

@Composable
fun TokitokiNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = TokitokiDestinations.MAIN_ROUTE,
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
        composable(TokitokiDestinations.MAIN_ROUTE) {

            val data = navController.currentBackStackEntry?.savedStateHandle?.get<String>("data") ?: ""

            MainScreen(
                fromSecondScreenTitle = data,
                nextScreen = {
                    throw RuntimeException("Test Crash") // Force a crash

                    navAction.navigateToSecondScreen("test")
            })
        }
        composable(
            route = TokitokiDestinations.SECOND_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.USER_NAME_ARGS) { type = NavType.StringType ; defaultValue = ""}),
            deepLinks = listOf(navDeepLink { uriPattern = TokitokiDestinations.SECOND_ROUTE_DEEPLINK })
        ) {entry ->
            val name:String = entry.arguments?.getString(TokitokiArgs.USER_NAME_ARGS) ?: ""
            SecondScreen(name = name, backAction = {
                navController.previousBackStackEntry?.savedStateHandle?.set("data", "Data from SecondScreen")
                navController.navigateUp()
            })
        }
    }
}