package com.example.tokitoki

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tokitoki.ui.screen.SignInScreen
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.viewmodel.SignInViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SignInScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private val activity get() = composeTestRule.activity

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testSignInContent_textDisplayed() {

        val viewModel = SignInViewModel()
        // Start the app
        composeTestRule.setContent {
            TokitokiTheme {
                SignInScreen(viewModel = viewModel)
            }
        }

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.sign_in_new_member_help_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.resources.getString(R.string.terms_of_service))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.resources.getString(R.string.privacy_policy))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.resources.getString(R.string.cookie_policy))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.sign_in_new_member_help_title)).performClick()
    }
}