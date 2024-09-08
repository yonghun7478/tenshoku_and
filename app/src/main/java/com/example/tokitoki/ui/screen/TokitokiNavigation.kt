package com.example.tokitoki.ui.screen

import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.TokitokiDestinations.EMAIL_VERIFICATION_ROUTE
import com.example.tokitoki.ui.screen.TokitokiScreens.EMAIL_VERIFICATION_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.REGISTER_WITH_EMAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.SIGN_IN_SCREEN

private object TokitokiScreens {
    const val SIGN_IN_SCREEN = "signInScreen"
    const val REGISTER_WITH_EMAIL_SCREEN = "RegisterWithEmailScreen"
    const val EMAIL_VERIFICATION_SCREEN = "EmailVerificationScreen"
}

object TokitokiArgs{
}

object TokitokiDestinations {
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
    const val REGISTER_WITH_EMAIL_ROUTE = REGISTER_WITH_EMAIL_SCREEN
    const val EMAIL_VERIFICATION_ROUTE = EMAIL_VERIFICATION_SCREEN
}

class TokitokiNavigationActions(private val navController: NavHostController) {
    fun navigateToRegisterWithEmail() {
        navController.navigate(REGISTER_WITH_EMAIL_SCREEN)
    }
    fun navigateToEmailVerification() {
        navController.navigate(EMAIL_VERIFICATION_SCREEN)
    }
}
