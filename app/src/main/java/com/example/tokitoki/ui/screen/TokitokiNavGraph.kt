package com.example.tokitoki.ui.screen

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tokitoki.ui.model.MyTagItem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.screen.mytaglist.MyTagListScreen
import com.example.tokitoki.ui.screen.tagdetail.TagDetailScreen
import com.example.tokitoki.ui.screens.message.MessageDetailScreen
import com.example.tokitoki.ui.state.LikesAndAshiatoTab
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel

@Composable
fun TokitokiNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = TokitokiDestinations.SPLASH_ROUTE,
    navAction: TokitokiNavigationActions = remember(navController) {
        TokitokiNavigationActions(navController)
    },
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    
    // Create SharedViewModel at NavGraph level
    val sharedViewModel: SharedPickupViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(TokitokiDestinations.SPLASH_ROUTE) {
            SplashScreen(
                onNavigateToMain = {
                    navAction.navigateToMainAndClearBackStack()
                },
                onNavigateToSignIn = {
                    navAction.navigateToSignInAndClearBackStack()
                }
            )
        }
        composable(TokitokiDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onRegisterWithEmail = {
                    navAction.navigateToRegisterWithEmail()
                },
                onAgreementConfirmation = {
                    navAction.navigateToAboutMe()
                },
                onMainScreen = {
                    navAction.navigateToMain()
                }
            )
        }
        composable(TokitokiDestinations.REGISTER_WITH_EMAIL_ROUTE) {
            RegisterWithEmailScreen(
                onEmailVerification = {
                    navAction.navigateToEmailVerification()
                }
            )
        }
        composable(TokitokiDestinations.EMAIL_VERIFICATION_ROUTE) {
            EmailVerificationScreen(
                onAgreementConfirmationScreen = {
                    navAction.navigateToAgreementConfirmation()
                },
                onMainScreen = {
                    navAction.navigateToMain()
                }
            )
        }
        composable(TokitokiDestinations.AGREEMENT_CONFIRMATION_ROUTE) {
            AgreementConfirmationScreen(
                onAboutMeScreen = {
                    // navAction code ...
                    navAction.navigateToAboutMe()
                }
            )
        }
        composable(TokitokiDestinations.ABOUT_ME_ROUTE) {
            AboutMeScreen(
                onAboutMeGenderScreen = {
                    // navAction code ...
                    navAction.navigateToAboutMeGender()
                }
            )
        }
        composable(TokitokiDestinations.ABOUT_ME_GENDER_ROUTE) {
            AboutMeGenderScreen(
                onAboutMeBirthDayScreen = {
                    navAction.navigateToAboutMeBirthday()
                },
                onAboutMeScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_BIRTHDAY_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.BIRTHDAY) { type = NavType.StringType })
        ) { backStackEntry ->

            val birthDay = backStackEntry.arguments?.getString(TokitokiArgs.BIRTHDAY) ?: ""

            AboutMeBirthdayScreen(
                birthDay = birthDay,
                onAboutMeGenderScreen = {
                    navController.navigateUp()
                },
                onAboutMeNameScreen = {
                    navAction.navigateToAboutMeName()
                },
                onPrevScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_NAME_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.NAME) { type = NavType.StringType })
        ) { backStackEntry ->

            val name = backStackEntry.arguments?.getString(TokitokiArgs.NAME) ?: ""

            AboutMeNameScreen(
                name = name,
                onAboutMeBirthdayScreen = {
                    navController.navigateUp()
                },
                onAboutMeSecondScreen = {
                    navAction.navigateToAboutMeSecond()
                },
                onPrevScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_SECOND_ROUTE) {
            AboutMeSecondScreen(
                onAboutMeTagScreen = {
                    navAction.navigateToAboutMeTag()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_TAG_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.TAG_IDS) { type = NavType.StringType })
        ) { backStackEntry ->
            val tagIdsString = backStackEntry.arguments?.getString(TokitokiArgs.TAG_IDS) ?: ""
            val tagIds: List<MyTagItem> = if (tagIdsString.isEmpty()) {
                listOf()
            } else {
                Gson().fromJson(tagIdsString, object : TypeToken<List<MyTagItem>>() {}.type)
            }

            AboutMeTagScreen(
                tagIds = tagIds,
                onAboutMeSecondScreen = {
                    navController.navigateUp()
                },
                onAboutMeThirdScreen = {
                    navAction.navigateToAboutMeThird()
                },
                onPrevScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_THIRD_ROUTE) {
            AboutMeThirdScreen(
                onAboutMePhotoUploadScreen = {
                    navAction.navigateToAboutMePhotoUpload()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_PHOTO_UPLOAD_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.URI) { type = NavType.StringType },
                navArgument(TokitokiArgs.IS_EDIT_MODE) { type = NavType.BoolType },
            )
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString(TokitokiArgs.URI)
            val uri = Uri.parse(Uri.decode(uriString))
            val isEditMode =
                backStackEntry.arguments?.getBoolean(TokitokiArgs.IS_EDIT_MODE) ?: false

            AboutMePhotoUploadScreen(
                uriParam = uri,
                isEditMode = isEditMode,
                onAboutMeProfInputScreen = {
                    navAction.navigateToAboutMeProfInput(uri = it)
                },
                onAboutMeThirdScreen = {
                    navController.navigateUp()
                },
                onPrevScreen = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("isFromEdit", true)
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "uri",
                        it.toString()
                    )
                    navController.navigateUp()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_PROF_INPUT_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.URI) { type = NavType.StringType },
                navArgument(TokitokiArgs.SELF_SENTENCE_IDS) { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val uriString = backStackEntry.arguments?.getString(TokitokiArgs.URI)
            val uri = Uri.parse(Uri.decode(uriString))

            val selfSentenceId = backStackEntry.arguments?.getInt(TokitokiArgs.SELF_SENTENCE_IDS)

            AboutMeProfInputScreen(
                selfSentenceId = selfSentenceId ?: -1,
                onAboutMePhotoUploadScreen = {
                    navController.navigateUp()
                },
                onAboutMeMyProfileScreen = {
                    navAction.navigateToAboutMeMyProfile(uri)
                },
                onPrevScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            TokitokiDestinations.ABOUT_ME_MY_PROFILE_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.URI) { type = NavType.StringType })
        ) { backStackEntry ->

            val uriStringFromArg = backStackEntry.arguments?.getString(TokitokiArgs.URI)
            val uriFromArg = Uri.parse(Uri.decode(uriStringFromArg))

            val isFromEditMode: Boolean = navController
                .currentBackStackEntry?.savedStateHandle?.get("isFromEdit") ?: false

            val uri = (if (isFromEditMode) Uri.parse(
                Uri.decode(
                    navController
                        .currentBackStackEntry?.savedStateHandle?.get("uri") ?: Uri.EMPTY.toString()
                )

            ) else uriFromArg).apply {

                AboutMeMyProfileScreen(
                    uri = this,
                    onAboutMeProfInputScreen = {
                        navAction.navigateToAboutMeProfInput(selfSentenceId = it)
                    },
                    onAboutMeNameScreen = {
                        navAction.navigateToAboutMeName(it)
                    },
                    onAboutMeBirthDayScreen = {
                        navAction.navigateToAboutMeBirthday(it)
                    },
                    onAboutMeTagScreen = {
                        navAction.navigateToAboutMeTag(it)
                    },
                    onAboutMePhotoUploadScreen = {
                        navAction.navigateToAboutMePhotoUpload(it, true)
                    },
                    onFavoriteTagScreen = {
                        navAction.navigateToFavoriteTag()
                    },
                    onMainScreen = {
                        navAction.navigateToMain()
                    }
                )
            }
        }

        composable(TokitokiDestinations.FAVORITE_TAG_ROUTE) {
            FavoriteTagScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable(TokitokiDestinations.FAVORITE_USERS_ROUTE) {
            FavoriteUsersScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNavigateToUserDetail = { userId, screenName ->
                    navAction.navigateToUserDetail(userId, screenName)
                }
            )
        }

        composable(TokitokiDestinations.MAIN_ROUTE) {
            MainScreen(
                onAshiatoClick = {
                    navAction.navigateToLikesAndAshiato(initialTab = LikesAndAshiatoTab.ASHIATO.name)
                },
                onFavoriteUsersClick = {
                    navAction.navigateToFavoriteUsers()
                },
                onIineSitaHitoClick = {
                    navAction.navigateToIineSitaHito()
                },
                onNavigateToSignIn = {
                    navAction.navigateToSignInAndClearBackStack()
                },
                onNavigateToUserDetail = { userId, screenName ->
                    navAction.navigateToUserDetail(userId, screenName)
                },
                onNavigateToTagSearch = {
                    navAction.navigateToTagSearch()
                },
                onNavigateToMyTagList = {
                    navAction.navigateToMyTagList()
                },
                onNavigateToTagDetail = {
                    navAction.navigateToTagDetail(it)
                },
                onNavigateToMessageDetail = { userId, source ->
                    navAction.navigateToMessageDetail(userId, source)
                },
                onNavigateToAboutMeMyProfile = {
                    val uri = Uri.parse(Uri.decode(it))
                    navAction.navigateToAboutMeMyProfile(uri)
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            TokitokiDestinations.LIKES_AND_ASHIATO_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.INITIAL_TAB) { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val initialTab = backStackEntry.arguments?.getString(TokitokiArgs.INITIAL_TAB)
            LikesAndAshiatoScreen(
                onNavigateToUserProfile = { id, source ->
                    navAction.navigateToUserDetail(userId = id, screenName = source)
                },
                onBackClick = {
                    navController.navigateUp()
                },
                showBackButton = true,
                initialTab = initialTab
            )
        }

        composable(TokitokiDestinations.IINE_SITA_HITO_ROUTE) {
            IineSitaHitoScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNavigateToUserDetail = { userId, screenName ->
                    navAction.navigateToUserDetail(userId, screenName)
                }
            )
        }

        composable(
            TokitokiDestinations.USER_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.USER_ID) { type = NavType.StringType },
                navArgument(TokitokiArgs.SCREEN_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(TokitokiArgs.USER_ID) ?: ""
            val screenName = backStackEntry.arguments?.getString(TokitokiArgs.SCREEN_NAME) ?: ""

            UserDetailScreen(
                selectedUserId = userId,
                screenName = screenName,
                onBackClick = {
                    navController.navigateUp()
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable(TokitokiDestinations.TAG_SEARCH_ROUTE) {
            TagSearchScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToCategory = { categoryId, categoryName ->
                    navAction.navigateToCategoryTag(categoryId, categoryName)
                },
                onNavigateToTagDetail = { tagId ->
                    navAction.navigateToTagDetail(tagId)
                }
            )
        }

        composable(
            TokitokiDestinations.CATEGORY_TAG_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.CATEGORY_ID) { type = NavType.StringType },
                navArgument(TokitokiArgs.CATEGORY_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString(TokitokiArgs.CATEGORY_ID) ?: ""
            val categoryName = backStackEntry.arguments?.getString(TokitokiArgs.CATEGORY_NAME) ?: ""
            CategoryTagScreen(
                categoryId = categoryId,
                categoryName = categoryName,
                onNavigateUp = { navController.navigateUp() },
                onNavigateToTagDetail = { tagId -> navAction.navigateToTagDetail(tagId) }
            )
        }

        composable(TokitokiDestinations.MY_TAG_LIST_ROUTE) {
            MyTagListScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToTagDetail = { tagId -> navAction.navigateToTagDetail(tagId) }
            )
        }

        composable(
            TokitokiDestinations.TAG_DETAIL_ROUTE,
            arguments = listOf(navArgument(TokitokiArgs.TAG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val tagId = backStackEntry.arguments?.getString(TokitokiArgs.TAG_ID) ?: ""
            TagDetailScreen(
                tagId = tagId,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToUserDetail = { userId, screenName ->
                    navAction.navigateToUserDetail(userId, screenName)
                }
            )
        }

        composable(
            TokitokiDestinations.MESSAGE_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(TokitokiArgs.OTHER_USER_ID) { type = NavType.StringType },
                navArgument(TokitokiArgs.SOURCE) { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val otherUserId = backStackEntry.arguments?.getString(TokitokiArgs.OTHER_USER_ID) ?: ""
            val source = backStackEntry.arguments?.getString(TokitokiArgs.SOURCE)
            MessageDetailScreen(
                otherUserId = otherUserId,
                onNavigateUp = {
                    navController.navigateUp()
                },
                source = source
            )
        }
    }
}
