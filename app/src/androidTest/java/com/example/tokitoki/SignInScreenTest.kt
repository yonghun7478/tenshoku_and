package com.example.tokitoki

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.ui.screen.SignInScreen
import com.example.tokitoki.ui.screen.TopLogo
import com.example.tokitoki.ui.theme.TokitokiTheme
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

        // 이미지가 존재하는지 확인
        composeTestRule.onNodeWithTag("TopLogo")
            .onChildAt(0) // 첫 번째 자식 (Image)
            .assertIsDisplayed()
            .assertHeightIsEqualTo(70.dp)

        // 텍스트가 존재하고, 내용이 맞는지 확인
        composeTestRule.onNodeWithTag("TopLogo")
            .onChildAt(1) // 두 번째 자식 (Text)
            .assertIsDisplayed()
            .assertTextEquals(composeTestRule.activity.getString(R.string.logo_name))
            .assertFontSizeIsEqualTo(52.sp)
            .assertFontWeightIsEqualTo(FontWeight.Bold)
            .assertFontColorIsEqualTo(Color(0xFF3b3b3b))
    }

    @Test
    fun menuDisplaysCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                SignInScreen()
            }
        }

        composeTestRule.onNodeWithTag("SignMenuBtn")
            .onChildAt(0) // 첫 번째 자식 (Image)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("SignMenuBtn")
            .onChildAt(1) // 두 번째 자식 (Text)
            .assertIsDisplayed()
            .assertTextEquals(composeTestRule.activity.getString(R.string.logo_name))
    }
}