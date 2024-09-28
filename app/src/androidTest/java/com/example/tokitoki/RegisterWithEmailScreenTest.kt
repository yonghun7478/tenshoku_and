package com.example.tokitoki

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.TokitokiDestinations
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.screen.TokitokiNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegisterWithEmailScreenTest {
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
    fun registerWithEmailScreenIsDisplayed() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph()
            }
        }

        composeTestRule.onNodeWithText(activity.getString(R.string.mail_btn_text)).performClick()
        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS).assertIsDisplayed()
    }

    @Test
    fun registerWithEmailScreenDialogIsDisplayed() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.REGISTER_WITH_EMAIL_ROUTE)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_TEXT_FIELD).performTextInput("yonghun")
        composeTestRule.onNodeWithText("yonghun").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.register_btn_title)).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.validate_error_msg)).assertIsDisplayed()

        composeTestRule.onNodeWithText("確認").isDisplayed()
        composeTestRule.onNodeWithText("確認").performClick()
    }
}