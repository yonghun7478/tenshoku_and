package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.state.AboutMeEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeViewModel

@Composable
fun AboutMeScreen(
    viewModel: AboutMeViewModel = hiltViewModel(),
    onAboutMeGenderScreen: () -> Unit = {},
) {
    AboutMeContents(
        aboutMeAction = viewModel::aboutMeAction
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is AboutMeEvent.NOTHING -> {

                }
                is AboutMeEvent.ACTION -> {
                    when (uiEvent.action) {
                        AboutMeAction.SUBMIT -> {
                            onAboutMeGenderScreen()
                        }

                        AboutMeAction.NOTHING -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutMeContents(
    aboutMeAction: (AboutMeAction) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .testTag(TestTags.ABOUT_ME_CONTENTS)
    ) {
        StepIcon(
            modifier = Modifier.padding(top = 60.dp, start = 20.dp),
            1,
            4
        )

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 20.dp),
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.about_me_title),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        AboutMeBottomLayout(
            aboutMeAction = aboutMeAction
        )
    }
}

@Composable
fun StepIcon(
    modifier: Modifier = Modifier,
    stepPos: Int,
    maxStep: Int
) {

    val list = remember(maxStep + 1) { MutableList(maxStep + 1) { false } }
    list[stepPos] = true

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        list.forEachIndexed { index, value ->
            if (index == 0) return@forEachIndexed

            if (value) {
                Text(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    LocalColor.current.blue,
                                    LocalColor.current.white
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(6.dp),
                    text = "Step${index}",
                    color = LocalColor.current.white,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    LocalColor.current.blue,
                                    LocalColor.current.white
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun AboutMeBottomLayout(
    modifier: Modifier = Modifier,
    aboutMeAction: (AboutMeAction) -> Unit = {}
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.lightGray),
            onClick = {
                aboutMeAction(AboutMeAction.SUBMIT)
            }
        ) {
            Text(
                text = stringResource(R.string.about_me_next),
                color = LocalColor.current.black,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeContentsPreView() {
    TokitokiTheme {
        AboutMeContents()
    }
}

@Preview(showBackground = true)
@Composable
fun StepIconPreView() {
    TokitokiTheme {
        StepIcon(stepPos = 1, maxStep = 4)
    }
}
