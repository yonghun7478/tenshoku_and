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
import com.example.tokitoki.backup.assertBackgroundColor
import com.example.tokitoki.backup.assertFontColorIsEqualTo
import com.example.tokitoki.backup.assertFontSizeIsEqualTo
import com.example.tokitoki.backup.assertFontWeightIsEqualTo
import com.example.tokitoki.backup.assertHasDrawable
import com.example.tokitoki.ui.screen.SignInSubLinks
import com.example.tokitoki.ui.screen.SignInSupportLink
import com.example.tokitoki.ui.screen.SignMenuBtn
import com.example.tokitoki.ui.screen.SignMenuOutlinedBtn
import com.example.tokitoki.ui.screen.TopLogo
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.constants.SignInAction
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
    fun topLogoDisplaysImageAndTextCorrectly() {
        composeTestRule.setContent {
            TokitokiTheme {
                TokitokiNavGraph()
            }
        }
    }
}