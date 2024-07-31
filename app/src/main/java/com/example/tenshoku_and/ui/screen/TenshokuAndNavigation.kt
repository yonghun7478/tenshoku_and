package com.example.tenshoku_and.ui.screen

import androidx.navigation.NavHostController
import com.example.tenshoku_and.ui.screen.TenshokuAndArgs.USER_NAME_ARGS
import com.example.tenshoku_and.ui.screen.TenshokuAndScreens.MAIN_SCREEN
import com.example.tenshoku_and.ui.screen.TenshokuAndScreens.SECOND_SCREEN

private object TenshokuAndScreens {
    const val MAIN_SCREEN = "mainScreen"
    const val SECOND_SCREEN = "secondScreen"
}

object TenshokuAndArgs{
    const val USER_NAME_ARGS = "userName"
}

object TenshokuAndDestinations {
    const val MAIN_ROUTE = MAIN_SCREEN
    const val SECOND_ROUTE = "$SECOND_SCREEN/{$USER_NAME_ARGS}"
}

class TenshokuAndNavigationActions(private val navController: NavHostController) {
    fun navigateToSecondScreen(userName: String) {
        navController.navigate(if(userName.isNotEmpty())"$SECOND_SCREEN/$userName" else SECOND_SCREEN)
    }
}
