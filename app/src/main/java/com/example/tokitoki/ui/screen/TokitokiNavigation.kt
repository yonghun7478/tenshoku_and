package com.example.tokitoki.ui.screen

import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.TokitokiScreens.SIGN_IN_SCREEN

private object TokitokiScreens {
    const val SIGN_IN_SCREEN = "signInScreen"
}

object TokitokiArgs{
}

object TokitokiDestinations {
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
}

class TokitokiNavigationActions(private val navController: NavHostController) {
}
