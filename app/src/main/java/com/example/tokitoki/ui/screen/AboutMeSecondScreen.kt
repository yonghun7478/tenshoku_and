package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeSecondAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.icons.TkStepIcon
import com.example.tokitoki.ui.state.AboutMeSecondEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeSecondViewModel

@Composable
fun AboutMeSecondScreen(
    viewModel: AboutMeSecondViewModel = hiltViewModel(),
    onAboutMeInterestScreen: () -> Unit = {},
) {
    AboutMeSecondContents(
        aboutMeSecondAction = viewModel::aboutMeSecondAction
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is AboutMeSecondEvent.NOTHING -> {

                }

                is AboutMeSecondEvent.ACTION -> {
                    when (uiEvent.action) {
                        AboutMeSecondAction.SUBMIT -> {
                            onAboutMeInterestScreen()
                        }

                        AboutMeSecondAction.NOTHING -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutMeSecondContents(
    aboutMeSecondAction: (AboutMeSecondAction) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.ABOUT_ME_SECOND_CONTENTS)
    ) {
        TkStepIcon(
            modifier = Modifier.padding(top = 60.dp, start = 20.dp),
            2,
            4
        )

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp),
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.about_me_title),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        AboutMeSecondBottomLayout(
            aboutMeSecondAction = aboutMeSecondAction
        )
    }
}

@Composable
fun AboutMeSecondBottomLayout(
    modifier: Modifier = Modifier,
    aboutMeSecondAction: (AboutMeSecondAction) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val canvasColor = LocalColor.current.blue

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight / 4)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = screenHeight / 4),
            onDraw = {
                drawCircle(color = canvasColor, radius = screenWidth.toPx())
            }
        )

        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            backgroundColor = LocalColor.current.lightGray,
            text = stringResource(R.string.about_me_next),
            action = aboutMeSecondAction,
            actionParam = AboutMeSecondAction.SUBMIT
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeSecondContentsPreView() {
    TokitokiTheme {
        AboutMeSecondContents()
    }
}
