package com.example.tokitoki

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.tokitoki.ui.constants.TestTags
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

        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph()
            }
        }
    }

    @Test
    fun registerWithEmailScreenIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.mail_btn_text)).performClick()
        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS).assertIsDisplayed()
    }

    @Test
    fun registerWithEmailScreenDialogIsDisplayed() {
        composeTestRule.onNodeWithText(activity.getString(R.string.mail_btn_text)).performClick()
        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_TEXT_FIELD).performTextInput("yonghun")
        composeTestRule.onNodeWithText("yonghun").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.register_btn_title)).performClick()

        composeTestRule.onNodeWithText(activity.getString(R.string.validate_error_msg)).assertIsDisplayed()
    }
}