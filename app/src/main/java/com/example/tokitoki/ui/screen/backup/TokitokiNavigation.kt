package com.example.tokitoki.ui.screen.backup

import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.backup.TokitokiArgs.USER_NAME_ARGS
import com.example.tokitoki.ui.screen.backup.TokitokiScreens.MAIN_SCREEN
import com.example.tokitoki.ui.screen.backup.TokitokiScreens.SECOND_SCREEN

private object TokitokiScreens {
    const val MAIN_SCREEN = "mainScreen"
    const val SECOND_SCREEN = "secondScreen"
}

object TokitokiArgs{
    const val USER_NAME_ARGS = "userName"
}

object TokitokiDestinations {
    const val MAIN_ROUTE = MAIN_SCREEN
    const val SECOND_ROUTE = "$SECOND_SCREEN/{$USER_NAME_ARGS}"
    const val SECOND_ROUTE_DEEPLINK = "tokitokiapp://$SECOND_SCREEN/{$USER_NAME_ARGS}"
}

class TokitokiNavigationActions(private val navController: NavHostController) {
    fun navigateToSecondScreen(userName: String) {
        navController.navigate(if(userName.isNotEmpty())"$SECOND_SCREEN/$userName" else SECOND_SCREEN)
    }
}
