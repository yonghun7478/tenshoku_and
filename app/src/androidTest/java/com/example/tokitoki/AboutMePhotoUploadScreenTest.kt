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
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AboutMePhotoUploadScreenTest {
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
    fun aboutMePhotoUploadScreenIsDisplayed() = runBlocking {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph()
            }
        }

        composeTestRule.onNodeWithText(activity.getString(R.string.mail_btn_text)).performClick()
        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.REGISTER_WITH_EMAIL_TEXT_FIELD)
            .performTextInput("yonghun@gmail.com")
        composeTestRule.onNodeWithText("yonghun@gmail.com").assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.register_btn_title))
            .performClick()

        composeTestRule.onNodeWithTag(TestTags.EMAIL_VERIFICATION_TEXT_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.EMAIL_VERIFICATION_TEXT_FIELD)
            .performTextInput("123456")

        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_CONTENTS).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_AGE_CHECKBOX).performClick()
        composeTestRule.onNodeWithTag(TestTags.AGREEMENT_CONFIRMATION_POLICY_CHECKBOX)
            .performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.agreement_confirmation_submit))
            .performClick()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithText("次へ").performClick()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_GENDER_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_GENDER_MALE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_GENDER_MALE).performClick()

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_BIRTHDAY_TEXT_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_BIRTHDAY_TEXT_FIELD)
            .performTextInput("19911211")

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_NAME_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_NAME_TEXT_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_NAME_TEXT_FIELD)
            .performTextInput("yongyong")

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_SECOND_CONTENTS).assertIsDisplayed()
        composeTestRule.onNodeWithText("次へ").performClick()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_TAG_CONTENTS).assertIsDisplayed()

        composeTestRule.onNodeWithText("ヨガ1").assertIsDisplayed()
        composeTestRule.onNodeWithText("ヨガ1").performClick()

        composeTestRule.onNodeWithText("ヨガ2").assertIsDisplayed()
        composeTestRule.onNodeWithText("ヨガ2").performClick()

        composeTestRule.onNodeWithText("ヨガ3").assertIsDisplayed()
        composeTestRule.onNodeWithText("ヨガ3").performClick()

        // UI가 안정 상태가 될 때까지 기다림
        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN).performClick()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_THIRD_CONTENTS).assertIsDisplayed()
        composeTestRule.onNodeWithText("次へ").performClick()

        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_UPLOAD_PHOTO_CONTENTS).assertIsDisplayed()

        return@runBlocking
    }

    @Test
    fun aboutMePhotoUploadScreenBottomDialogClick() = runBlocking {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph(startDestination = TokitokiDestinations.ABOUT_ME_PHOTO_UPLOAD_ROUTE)
            }
        }
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_UPLOAD_PHOTO_UPLOAD_INPUT_BOX).isDisplayed()
        composeTestRule.onNodeWithTag(TestTags.ABOUT_ME_UPLOAD_PHOTO_UPLOAD_INPUT_BOX)
            .performClick()

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText("引目写真で雰囲気を伝えましょう").assertIsDisplayed()

        return@runBlocking
    }
}