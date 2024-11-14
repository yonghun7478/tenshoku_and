package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun TokitokiNavGraph(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = TokitokiDestinations.SIGN_IN_ROUTE,
    navAction: TokitokiNavigationActions = remember(navController) {
        TokitokiNavigationActions(navController)
    },
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(TokitokiDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                onRegisterWithEmail = {
                    navAction.navigateToRegisterWithEmail()
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

        composable(TokitokiDestinations.ABOUT_ME_BIRTHDAY_ROUTE) {
            AboutMeBirthdayScreen(
                onAboutMeGenderScreen = {
                    navController.navigateUp()
                },
                onAboutMeNameScreen = {
                    navAction.navigateToAboutMeName()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_NAME_ROUTE) {
            AboutMeNameScreen(
                onAboutMeBirthdayScreen = {
                    navController.navigateUp()
                },
                onAboutMeSecondScreen = {
                    navAction.navigateToAboutMeSecond()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_SECOND_ROUTE) {
            AboutMeSecondScreen(
                onAboutMeInterestScreen = {
                    navAction.navigateToAboutMeInterest()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_INTEREST_ROUTE) {
            AboutMeInterestScreen(
                onAboutMeSecondScreen = {
                    navController.navigateUp()
                },
                onAboutMeThirdScreen = {
                    navAction.navigateToAboutMeThird()
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

        composable(TokitokiDestinations.ABOUT_ME_PHOTO_UPLOAD_ROUTE) {
            AboutMePhotoUploadScreen(
                onAboutMeProfInputScreen = {
                    navAction.navigateToAboutMeProfInput()
                },
                onAboutMeThirdScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_PROF_INPUT_ROUTE) {
            AboutMeProfInputScreen(
                onAboutMePhotoUploadScreen = {
                    navController.navigateUp()
                },
                onAboutMeProfScreen = {

                }
            )
        }

        composable(TokitokiDestinations.ABOUT_ME_MY_PROFILE_ROUTE) {
            AboutMeMyProfileScreen(
                onIntroduceLikePageScreen = {},
                onAboutMeProfInputScreen = {}
            )
        }
    }
}
