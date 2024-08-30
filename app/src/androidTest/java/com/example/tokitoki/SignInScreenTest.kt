package com.example.tokitoki

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.ui.screen.SignInSubLinks
import com.example.tokitoki.ui.screen.SignInSupportLink
import com.example.tokitoki.ui.screen.SignMenuBtn
import com.example.tokitoki.ui.screen.SignMenuOutlinedBtn
import com.example.tokitoki.ui.screen.TopLogo
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.SignInAction
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
    fun topLogoDisplaysImageAndTextCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                TopLogo()
            }
        }

        val color = TokitokiColor()

        // 이미지가 존재하는지 확인
        composeTestRule.onNodeWithTag("TopLogo")
            .onChildAt(0) // 첫 번째 자식 (Image)
            .assertIsDisplayed()
            .assertHeightIsEqualTo(70.dp)
            .assertHasDrawable(id = R.drawable.pink_heart_logo)

        // 텍스트가 존재하고, 내용이 맞는지 확인
        composeTestRule.onNodeWithTag("TopLogo")
            .onChildAt(1) // 두 번째 자식 (Text)
            .assertIsDisplayed()
            .assertTextEquals(composeTestRule.activity.getString(R.string.logo_name))
            .assertFontSizeIsEqualTo(52.sp)
            .assertFontWeightIsEqualTo(FontWeight.Bold)
            .assertFontColorIsEqualTo(color.grayColor)
    }

    @Test
    fun loginBtnWithMailCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                SignMenuBtn(
                    text = stringResource(id = R.string.mail_btn_text),
                    textColor = LocalColor.current.white,
                    iconRes = R.drawable.ic_mail,
                    backgroundColor = LocalColor.current.blue,
                    signInAction = SignInAction.LoginWithEmail,
                )
            }
        }

        val color = TokitokiColor()

        composeTestRule
            .onNodeWithTag("SignMenuBtn")
            .assertIsDisplayed()
            .assertTextEquals(activity.resources.getString(R.string.mail_btn_text))
            .assertFontColorIsEqualTo(color.white)
            .assertBackgroundColor(color.blue)
            .assertHasDrawable(R.drawable.ic_mail)

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.mail_btn_text))
            .assertFontSizeIsEqualTo(14.sp)
    }

    @Test
    fun loginBtnWithGoogleCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                SignMenuOutlinedBtn(
                    text = stringResource(id = R.string.google_btn_text),
                    textColor = LocalColor.current.black,
                    backgroundColor = LocalColor.current.white,
                    iconRes = R.drawable.ic_google,
                    signInAction = SignInAction.LoginWithGoogle,
                )
            }
        }

        val color = TokitokiColor()

        composeTestRule
            .onNodeWithTag("SignMenuOutlinedBtn")
            .assertIsDisplayed()
            .assertTextEquals(activity.resources.getString(R.string.google_btn_text))
            .assertFontColorIsEqualTo(color.black)
            .assertBackgroundColor(color.white)
            .assertHasDrawable(R.drawable.ic_google)

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.google_btn_text))
            .assertFontSizeIsEqualTo(14.sp)
    }

    @Test
    fun supportLinkCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                SignInSupportLink()
            }
        }

        val color = TokitokiColor()

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.sign_in_new_member_help_title))
            .assertIsDisplayed()
            .assertFontSizeIsEqualTo(10.sp)
            .assertFontColorIsEqualTo(color.blue)
            .assertTextEquals(composeTestRule.activity.getString(R.string.sign_in_new_member_help_title))
            .assertFontWeightIsEqualTo(FontWeight.Bold)
    }

    @Test
    fun subLinkCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                SignInSubLinks()
            }
        }

        val color = TokitokiColor()

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.terms_of_service))
            .assertIsDisplayed()
            .assertFontSizeIsEqualTo(10.sp)
            .assertFontColorIsEqualTo(color.grayColor)
            .assertTextEquals(composeTestRule.activity.getString(R.string.terms_of_service))

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.privacy_policy))
            .assertIsDisplayed()
            .assertFontSizeIsEqualTo(10.sp)
            .assertFontColorIsEqualTo(color.grayColor)
            .assertTextEquals(composeTestRule.activity.getString(R.string.privacy_policy))

        composeTestRule.onNodeWithText(activity.resources.getString(R.string.cookie_policy))
            .assertIsDisplayed()
            .assertFontSizeIsEqualTo(10.sp)
            .assertFontColorIsEqualTo(color.grayColor)
            .assertTextEquals(composeTestRule.activity.getString(R.string.cookie_policy))
    }
}