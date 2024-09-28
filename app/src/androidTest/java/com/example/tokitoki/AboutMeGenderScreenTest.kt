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
class AboutMeGenderScreenTest {
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
    fun aboutMeGenderScreenIsDisplayed() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph()
            }
        }

        composeTestRule.onNodeWithText(activity.getString(R.string.mail_btn_text)).performClick()
        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_TEXT_FIELD).performTextInput("yonghun@gmail.com")
        composeTestRule.onNodeWithText("yonghun@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.register_btn_title)).performClick()

        composeTestRule.onNodeWithTag(TestTags.EMAIL_VERIFICATION_TEXT_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.EMAIL_VERIFICATION_TEXT_FIELD).performTextInput("123456")

        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_CONTENTS).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_AGE_CHECKBOX).performClick()
        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_POLICY_CHECKBOX).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.agreement_confirmation_submit)).performClick()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithText("次へ").performClick()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_GENDER_CONTENTS).assertIsDisplayed()
    }

    @Test
    fun aboutMeGenderScreenMenuIsClicked() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.ABOUT_ME_GENDER_ROUTE)
            }
        }

        composeTestRule.onNodeWithText("男性").isDisplayed()
        composeTestRule.onNodeWithText("男性").performClick()
        composeTestRule.onNodeWithText("女性").isDisplayed()
        composeTestRule.onNodeWithText("女性").performClick()
    }

    @Test
    fun aboutMeGenderScreenDialogIsDisplayed() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.ABOUT_ME_GENDER_ROUTE)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()

        composeTestRule.onNodeWithText("確認").isDisplayed()
        composeTestRule.onNodeWithText("確認").performClick()
        //birthdayコード予定
    }

    @Test
    fun aboutMeGenderScreenBackBtnClicked() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.ABOUT_ME_GENDER_ROUTE)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_PREVIOUS_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_PREVIOUS_BTN).performClick()
    }

    @Test
    fun aboutMeGenderScreenNextBtnClicked() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.ABOUT_ME_GENDER_ROUTE)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()
    }
}