package com.example.tokitoki.ui.screen

import android.net.Uri
import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.TokitokiArgs.BIRTHDAY
import com.example.tokitoki.ui.screen.TokitokiArgs.IS_EDIT_MODE
import com.example.tokitoki.ui.screen.TokitokiArgs.NAME
import com.example.tokitoki.ui.screen.TokitokiArgs.SELF_SENTENCE_IDS
import com.example.tokitoki.ui.screen.TokitokiArgs.TAG_IDS
import com.example.tokitoki.ui.screen.TokitokiArgs.URI
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_BIRTHDAY_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_GENDER_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_TAG_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_MY_PROFILE_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_NAME_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_PHOTO_UPLOAD_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_PROF_INPUT_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_SECOND_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.ABOUT_ME_THIRD_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.AGREEMENT_CONFIRMATION_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.EMAIL_VERIFICATION_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.FAVORITE_TAG_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.MAIN_SCREEN
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
    const val ABOUT_ME_TAG_SCREEN = "AboutMeTagScreen"
    const val ABOUT_ME_THIRD_SCREEN = "AboutMeThirdScreen"
    const val ABOUT_ME_PHOTO_UPLOAD_SCREEN = "AboutMePhotoUploadScreen"
    const val ABOUT_ME_PROF_INPUT_SCREEN = "AboutMeProfInputScreen"
    const val ABOUT_ME_MY_PROFILE_SCREEN = "AboutMeMyProfileScreen"
    const val FAVORITE_TAG_SCREEN = "FavoriteTagScreen"
    const val MAIN_SCREEN = "MainScreen"
}

object TokitokiArgs {
    const val URI = "uri"
    const val NAME = "name"
    const val BIRTHDAY = "birthday"
    const val TAG_IDS = "tagIds"
    const val SELF_SENTENCE_IDS = "selfSentenceIds"
    const val IS_EDIT_MODE = "isEditMode"
}

object TokitokiDestinations {
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
    const val REGISTER_WITH_EMAIL_ROUTE = REGISTER_WITH_EMAIL_SCREEN
    const val EMAIL_VERIFICATION_ROUTE = EMAIL_VERIFICATION_SCREEN
    const val AGREEMENT_CONFIRMATION_ROUTE = AGREEMENT_CONFIRMATION_SCREEN
    const val ABOUT_ME_ROUTE = ABOUT_ME_SCREEN
    const val ABOUT_ME_GENDER_ROUTE = ABOUT_ME_GENDER_SCREEN
    const val ABOUT_ME_BIRTHDAY_ROUTE = "$ABOUT_ME_BIRTHDAY_SCREEN?$BIRTHDAY={$BIRTHDAY}"
    const val ABOUT_ME_NAME_ROUTE = "$ABOUT_ME_NAME_SCREEN?$NAME={$NAME}"
    const val ABOUT_ME_SECOND_ROUTE = ABOUT_ME_SECOND_SCREEN
    const val ABOUT_ME_TAG_ROUTE = "$ABOUT_ME_TAG_SCREEN?$TAG_IDS={$TAG_IDS}"
    const val ABOUT_ME_THIRD_ROUTE = ABOUT_ME_THIRD_SCREEN
    const val ABOUT_ME_PHOTO_UPLOAD_ROUTE = "$ABOUT_ME_PHOTO_UPLOAD_SCREEN?$URI={$URI}&$IS_EDIT_MODE={$IS_EDIT_MODE}"
    const val ABOUT_ME_PROF_INPUT_ROUTE = "$ABOUT_ME_PROF_INPUT_SCREEN?$URI={$URI}&$SELF_SENTENCE_IDS={$SELF_SENTENCE_IDS}"
    const val ABOUT_ME_MY_PROFILE_ROUTE = "$ABOUT_ME_MY_PROFILE_SCREEN?$URI={$URI}"
    const val FAVORITE_TAG_ROUTE = FAVORITE_TAG_SCREEN
    const val MAIN_ROUTE = MAIN_SCREEN
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

    fun navigateToAboutMeBirthday(birthday: String = "") {
        navController.navigate("$ABOUT_ME_BIRTHDAY_SCREEN?$BIRTHDAY=${birthday}")
    }

    fun navigateToAboutMeName(name: String = "") {
        navController.navigate("$ABOUT_ME_NAME_SCREEN?$NAME=${name}")
    }

    fun navigateToAboutMeSecond() {
        navController.navigate(ABOUT_ME_SECOND_SCREEN)
    }

    fun navigateToAboutMeTag(args: String = "") {
        navController.navigate("$ABOUT_ME_TAG_SCREEN?$TAG_IDS=${args}")
    }

    fun navigateToAboutMeThird() {
        navController.navigate(ABOUT_ME_THIRD_SCREEN)
    }

    fun navigateToAboutMePhotoUpload(uri: Uri = Uri.EMPTY, isEditMode:Boolean = false) {
        navController.navigate("$ABOUT_ME_PHOTO_UPLOAD_SCREEN?$URI=${Uri.encode(uri.toString())}&$IS_EDIT_MODE=${isEditMode}")
    }

    fun navigateToAboutMeProfInput(uri: Uri = Uri.EMPTY, selfSentenceId: Int = -1) {
        navController.navigate("$ABOUT_ME_PROF_INPUT_SCREEN?$URI=${Uri.encode(uri.toString())}&$SELF_SENTENCE_IDS=${selfSentenceId}")
    }

    fun navigateToAboutMeMyProfile(uri: Uri) {
        navController.navigate("$ABOUT_ME_MY_PROFILE_SCREEN?$URI=${Uri.encode(uri.toString())}")
    }

    fun navigateToFavoriteTag() {
        navController.navigate(FAVORITE_TAG_SCREEN)
    }

    fun navigateToMain() {
        navController.navigate(MAIN_SCREEN)
    }
}
