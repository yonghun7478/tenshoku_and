package com.example.tokitoki.ui.screen

import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_BIRTHDAY_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_GENDER_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_INTEREST_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_MY_PROFILE_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_NAME_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_PHOTO_UPLOAD_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_PROF_INPUT_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_SECOND_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_THIRD_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.AGREEMENT_CONFIRMATION_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.EMAIL_VERIFICATION_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.REGISTER_WITH_EMAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.SIGN_IN_SCREEN

private object TokitokiScreens {
    const val SIGN_IN_SCREEN = "signInScreen"
    const val REGISTER_WITH_EMAIL_SCREEN = "RegisterWithEmailScreen"
    const val EMAIL_VERIFICATION_SCREEN = "EmailVerificationScreen"
    const val AGREEMENT_CONFIRMATION_SCREEN = "AgreementConfirmationScreen"
    const val ABOUT_ME_SCREEN = "AboutMeScreen"
    const val ABOUT_ME_GENDER_SCREEN = "AboutMeGenderScreen"
    const val ABOUT_ME_BIRTHDAY_SCREEN = "AboutMeBirthdayScreen"
    const val ABOUT_ME_NAME_SCREEN = "AboutMeNameScreen"
    const val ABOUT_ME_SECOND_SCREEN = "AboutMeSecondScreen"
    const val ABOUT_ME_INTEREST_SCREEN = "AboutMeInterestScreen"
    const val ABOUT_ME_THIRD_SCREEN = "AboutMeThirdScreen"
    const val ABOUT_ME_PHOTO_UPLOAD_SCREEN = "AboutMePhotoUploadScreen"
    const val ABOUT_ME_PROF_INPUT_SCREEN = "AboutMeProfInputScreen"
    const val ABOUT_ME_MY_PROFILE_SCREEN = "AboutMeMyProfileScreen"
}

object TokitokiArgs {
}

object TokitokiDestinations {
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
    const val REGISTER_WITH_EMAIL_ROUTE = REGISTER_WITH_EMAIL_SCREEN
    const val EMAIL_VERIFICATION_ROUTE = EMAIL_VERIFICATION_SCREEN
    const val AGREEMENT_CONFIRMATION_ROUTE = AGREEMENT_CONFIRMATION_SCREEN
    const val ABOUT_ME_ROUTE = ABOUT_ME_SCREEN
    const val ABOUT_ME_GENDER_ROUTE = ABOUT_ME_GENDER_SCREEN
    const val ABOUT_ME_BIRTHDAY_ROUTE = ABOUT_ME_BIRTHDAY_SCREEN
    const val ABOUT_ME_NAME_ROUTE = ABOUT_ME_NAME_SCREEN
    const val ABOUT_ME_SECOND_ROUTE = ABOUT_ME_SECOND_SCREEN
    const val ABOUT_ME_INTEREST_ROUTE = ABOUT_ME_INTEREST_SCREEN
    const val ABOUT_ME_THIRD_ROUTE = ABOUT_ME_THIRD_SCREEN
    const val ABOUT_ME_PHOTO_UPLOAD_ROUTE = ABOUT_ME_PHOTO_UPLOAD_SCREEN
    const val ABOUT_ME_PROF_INPUT_ROUTE = ABOUT_ME_PROF_INPUT_SCREEN
    const val ABOUT_ME_MY_PROFILE_ROUTE = ABOUT_ME_MY_PROFILE_SCREEN
}

class TokitokiNavigationActions(private val navController: NavHostController) {
    fun navigateToRegisterWithEmail() {
        navController.navigate(REGISTER_WITH_EMAIL_SCREEN)
    }

    fun navigateToEmailVerification() {
        navController.navigate(EMAIL_VERIFICATION_SCREEN)
    }

    fun navigateToAgreementConfirmation() {
        navController.navigate(AGREEMENT_CONFIRMATION_SCREEN)
    }

    fun navigateToAboutMe() {
        navController.navigate(ABOUT_ME_SCREEN)
    }

    fun navigateToAboutMeGender() {
        navController.navigate(ABOUT_ME_GENDER_SCREEN)
    }

    fun navigateToAboutMeBirthday() {
        navController.navigate(ABOUT_ME_BIRTHDAY_SCREEN)
    }

    fun navigateToAboutMeName() {
        navController.navigate(ABOUT_ME_NAME_SCREEN)
    }

    fun navigateToAboutMeSecond() {
        navController.navigate(ABOUT_ME_SECOND_SCREEN)
    }

    fun navigateToAboutMeInterest() {
        navController.navigate(ABOUT_ME_INTEREST_SCREEN)
    }

    fun navigateToAboutMeThird() {
        navController.navigate(ABOUT_ME_THIRD_SCREEN)
    }

    fun navigateToAboutMePhotoUpload() {
        navController.navigate(ABOUT_ME_PHOTO_UPLOAD_SCREEN)
    }

    fun navigateToAboutMeProfInput() {
        navController.navigate(ABOUT_ME_PROF_INPUT_SCREEN)
    }

    fun navigateToAboutMeMyProfile() {
        navController.navigate(ABOUT_ME_MY_PROFILE_SCREEN)
    }
}
