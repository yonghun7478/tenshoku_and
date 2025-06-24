package com.example.tokitoki.ui.screen

import android.net.Uri
import androidx.navigation.NavHostController
import com.example.tokitoki.ui.screen.TokitokiArgs.BIRTHDAY
import com.example.tokitoki.ui.screen.TokitokiArgs.IS_EDIT_MODE
import com.example.tokitoki.ui.screen.TokitokiArgs.NAME
import com.example.tokitoki.ui.screen.TokitokiArgs.SELF_SENTENCE_IDS
import com.example.tokitoki.ui.screen.TokitokiArgs.TAG_IDS
import com.example.tokitoki.ui.screen.TokitokiArgs.URI
import com.example.tokitoki.ui.screen.TokitokiArgs.USER_ID
import com.example.tokitoki.ui.screen.TokitokiArgs.SCREEN_NAME
import com.example.tokitoki.ui.screen.TokitokiArgs.CATEGORY_ID
import com.example.tokitoki.ui.screen.TokitokiArgs.CATEGORY_NAME
import com.example.tokitoki.ui.screen.TokitokiArgs.IS_FROM_MY_PAGE
import com.example.tokitoki.ui.screen.TokitokiArgs.TAG_ID
import com.example.tokitoki.ui.screen.TokitokiArgs.OTHER_USER_ID
import com.example.tokitoki.ui.screen.TokitokiArgs.SOURCE
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
import com.example.tokitoki.ui.screen.TokitokiScreens.FAVORITE_USERS_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.FAVORITE_TAG_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.MAIN_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.REGISTER_WITH_EMAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.SIGN_IN_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.IINE_SITA_HITO_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.USER_DETAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.TAG_SEARCH_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.CATEGORY_TAG_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.MY_TAG_LIST_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.TAG_DETAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.MESSAGE_DETAIL_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.LIKES_AND_ASHIATO_SCREEN
import com.example.tokitoki.ui.screen.TokitokiScreens.SPLASH_SCREEN

private object TokitokiScreens {
    const val SPLASH_SCREEN = "splashScreen"
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
    const val ASHIATO_SCREEN = "AshiatoScreen"
    const val LIKES_AND_ASHIATO_SCREEN = "LikesAndAshiatoScreen"
    const val FAVORITE_USERS_SCREEN = "FavoriteUsersScreen"
    const val IINE_SITA_HITO_SCREEN = "IineSitaHitoScreen"
    const val USER_DETAIL_SCREEN = "UserDetailScreen"
    const val TAG_SEARCH_SCREEN = "tag_search_screen"
    const val CATEGORY_TAG_SCREEN = "category_tag_screen"
    const val MY_TAG_LIST_SCREEN = "my_tag_list_screen"
    const val TAG_DETAIL_SCREEN = "tag_detail_screen"
    const val MESSAGE_DETAIL_SCREEN = "message_detail_screen"
}

object TokitokiArgs {
    const val URI = "uri"
    const val NAME = "name"
    const val BIRTHDAY = "birthday"
    const val TAG_IDS = "tagIds"
    const val SELF_SENTENCE_IDS = "selfSentenceIds"
    const val IS_EDIT_MODE = "isEditMode"
    const val USER_ID = "userId"
    const val SCREEN_NAME = "screenName"
    const val CATEGORY_ID = "categoryId"
    const val CATEGORY_NAME = "categoryName"
    const val TAG_ID = "tagId"
    const val OTHER_USER_ID = "otherUserId"
    const val SOURCE = "source"
    const val INITIAL_TAB = "initialTab"
    const val IS_FROM_MY_PAGE = "isFromMyPage"
}

object TokitokiDestinations {
    const val SIGN_IN_ROUTE = SIGN_IN_SCREEN
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val REGISTER_WITH_EMAIL_ROUTE = REGISTER_WITH_EMAIL_SCREEN
    const val EMAIL_VERIFICATION_ROUTE = EMAIL_VERIFICATION_SCREEN
    const val AGREEMENT_CONFIRMATION_ROUTE = AGREEMENT_CONFIRMATION_SCREEN
    const val ABOUT_ME_ROUTE = ABOUT_ME_SCREEN
    const val ABOUT_ME_GENDER_ROUTE = ABOUT_ME_GENDER_SCREEN
    const val ABOUT_ME_BIRTHDAY_ROUTE = "$ABOUT_ME_BIRTHDAY_SCREEN?$BIRTHDAY={$BIRTHDAY}"
    const val ABOUT_ME_NAME_ROUTE = "$ABOUT_ME_NAME_SCREEN?$NAME={$NAME}"
    const val ABOUT_ME_SECOND_ROUTE = ABOUT_ME_SECOND_SCREEN
    const val ABOUT_ME_TAG_ROUTE = "$ABOUT_ME_TAG_SCREEN?$TAG_IDS={$TAG_IDS}&$IS_FROM_MY_PAGE={${IS_FROM_MY_PAGE}}"
    const val ABOUT_ME_THIRD_ROUTE = ABOUT_ME_THIRD_SCREEN
    const val ABOUT_ME_PHOTO_UPLOAD_ROUTE = "$ABOUT_ME_PHOTO_UPLOAD_SCREEN?$URI={$URI}&$IS_EDIT_MODE={$IS_EDIT_MODE}"
    const val ABOUT_ME_PROF_INPUT_ROUTE = "$ABOUT_ME_PROF_INPUT_SCREEN?$URI={$URI}&$SELF_SENTENCE_IDS={$SELF_SENTENCE_IDS}"
    const val ABOUT_ME_MY_PROFILE_ROUTE = "$ABOUT_ME_MY_PROFILE_SCREEN?$URI={$URI}&$IS_FROM_MY_PAGE={$IS_FROM_MY_PAGE}"
    const val FAVORITE_TAG_ROUTE = FAVORITE_TAG_SCREEN
    const val FAVORITE_USERS_ROUTE = FAVORITE_USERS_SCREEN
    const val MAIN_ROUTE = MAIN_SCREEN
    const val LIKES_AND_ASHIATO_ROUTE = "$LIKES_AND_ASHIATO_SCREEN?${TokitokiArgs.INITIAL_TAB}={${TokitokiArgs.INITIAL_TAB}}"
    const val IINE_SITA_HITO_ROUTE = IINE_SITA_HITO_SCREEN
    const val USER_DETAIL_ROUTE = "$USER_DETAIL_SCREEN?$USER_ID={$USER_ID}&$SCREEN_NAME={$SCREEN_NAME}"
    const val TAG_SEARCH_ROUTE = TAG_SEARCH_SCREEN
    const val CATEGORY_TAG_ROUTE = "$CATEGORY_TAG_SCREEN?$CATEGORY_ID={$CATEGORY_ID}&$CATEGORY_NAME={$CATEGORY_NAME}"
    const val MY_TAG_LIST_ROUTE = MY_TAG_LIST_SCREEN
    const val TAG_DETAIL_ROUTE = "$TAG_DETAIL_SCREEN?$TAG_ID={$TAG_ID}"
    const val MESSAGE_DETAIL_ROUTE = "$MESSAGE_DETAIL_SCREEN?$OTHER_USER_ID={$OTHER_USER_ID}&$SOURCE={$SOURCE}"
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

    fun navigateToAboutMeTag(args: String = "", isFromMyPage: Boolean = false) {
        navController.navigate("$ABOUT_ME_TAG_SCREEN?$TAG_IDS=${args}&$IS_FROM_MY_PAGE=${isFromMyPage}")
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

    fun navigateToAboutMeMyProfile(uri: Uri, isFromMyPage: Boolean = false) {
        navController.navigate("$ABOUT_ME_MY_PROFILE_SCREEN?$URI=${Uri.encode(uri.toString())}&$IS_FROM_MY_PAGE=${isFromMyPage}")
    }

    fun navigateToFavoriteTag() {
        navController.navigate(FAVORITE_TAG_SCREEN)
    }

    fun navigateToMain() {
        navController.navigate(MAIN_SCREEN)
    }

    fun navigateToLikesAndAshiato(initialTab: String? = null) {
        navController.navigate("${TokitokiScreens.LIKES_AND_ASHIATO_SCREEN}?${TokitokiArgs.INITIAL_TAB}=${Uri.encode(initialTab ?: "null")}")
    }

    fun navigateToFavoriteUsers() {
        navController.navigate(FAVORITE_USERS_SCREEN)
    }

    fun navigateToIineSitaHito() {
        navController.navigate(IINE_SITA_HITO_SCREEN)
    }

    fun navigateToUserDetail(userId: String, screenName: String) {
        navController.navigate("$USER_DETAIL_SCREEN?$USER_ID=${userId}&$SCREEN_NAME=${screenName}")
    }

    fun navigateToTagSearch() {
        navController.navigate(TokitokiScreens.TAG_SEARCH_SCREEN)
    }

    fun navigateToCategoryTag(categoryId: String, categoryName: String) {
        navController.navigate("$CATEGORY_TAG_SCREEN?$CATEGORY_ID=${categoryId}&$CATEGORY_NAME=${categoryName}")
    }

    fun navigateToMyTagList() {
        navController.navigate(TokitokiScreens.MY_TAG_LIST_SCREEN)
    }

    fun navigateToTagDetail(tagId: String) {
        navController.navigate("$TAG_DETAIL_SCREEN?$TAG_ID=${tagId}")
    }

    fun navigateToMessageDetail(otherUserId: String, source: String?) {
        navController.navigate("$MESSAGE_DETAIL_SCREEN?$OTHER_USER_ID=${otherUserId}&$SOURCE=${source}")
    }

    fun navigateToSignInAndClearBackStack() {
        navController.navigate(TokitokiDestinations.SIGN_IN_ROUTE) {
            popUpTo(TokitokiDestinations.SPLASH_ROUTE) { inclusive = true }
        }
    }

    fun navigateToMainAndClearBackStack() {
        navController.navigate(TokitokiDestinations.MAIN_ROUTE) {
            popUpTo(TokitokiDestinations.SPLASH_ROUTE) { inclusive = true }
        }
    }
}
