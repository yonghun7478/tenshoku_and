package com.example.tokitoki.ui.screen


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeGenderAction
import com.example.tokitoki.ui.constants.AboutMeGenderConstants
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.state.AboutMeGenderEvent
import com.example.tokitoki.ui.state.AboutMeGenderState
import com.example.tokitoki.ui.state.Gender
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeGenderViewModel

@Composable
fun AboutMeGenderScreen(
    onAboutMeScreen: () -> Unit = {},
    onAboutMeBirthDayScreen: () -> Unit = {},
    viewModel: AboutMeGenderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeGenderContents(
        uiState = uiState,
        aboutMeGenderAction = viewModel::aboutMeGenderAction,
    )

    LaunchedEffect(Unit) {
        viewModel.init()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeGenderEvent.ACTION -> {
                    when (event.action) {
                        AboutMeGenderAction.FEMALE_CLICK -> {
                            viewModel.onGenderSelected(Gender.FEMALE)
                        }

                        AboutMeGenderAction.MALE_CLICK -> {
                            viewModel.onGenderSelected(Gender.MALE)
                        }

                        AboutMeGenderAction.NEXT -> {
                            if (viewModel.checkGender())
                                onAboutMeBirthDayScreen()
                            else
                                viewModel.updateShowDialogState(true)
                        }

                        AboutMeGenderAction.PREVIOUS -> {
                            onAboutMeScreen()
                        }

                        AboutMeGenderAction.NOTHING -> {

                        }

                        AboutMeGenderAction.DIALOG_DISMISS -> {
                            viewModel.updateShowDialogState(false)
                        }

                        AboutMeGenderAction.DIALOG_OK -> {
                            viewModel.updateShowDialogState(false)
                        }
                    }
                    Log.d(AboutMeGenderConstants.TAG, "uiEvent.action ${event.action}")
                }

                AboutMeGenderEvent.NOTHING -> {}
            }
        }
    }
}

@Composable
fun AboutMeGenderContents(
    uiState: AboutMeGenderState = AboutMeGenderState(),
    aboutMeGenderAction: (AboutMeGenderAction) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.ABOUT_ME_GENDER_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AboutMeGenderIndicator(
            modifier = Modifier.padding(top = 30.dp),
            total = 20,
            current = 1
        )

        AboutMeGenderTitle(
            modifier = Modifier.padding(top = 10.dp)
        )

        AboutMeGenderSelector(
            modifier = Modifier.padding(top = 30.dp),
            selectedGender = uiState.selectedGender,
            aboutMeGenderAction = aboutMeGenderAction
        )

        Spacer(modifier = Modifier.weight(1f))

        AboutMeGenderBottomMenu(
            modifier = Modifier.padding(10.dp),
            aboutMeGenderAction = aboutMeGenderAction
        )
    }

    if (uiState.showDialog) {
        AboutMeGenderErrorDialog(
            message = stringResource(R.string.about_me_validate_gender),
            aboutMeGenderAction = aboutMeGenderAction
        )
    }
}

@Composable
fun AboutMeGenderIndicator(
    modifier: Modifier = Modifier,
    total: Int = 0,
    current: Int = 0,
    circleSize: Int = 6,
    activeCircleSize: Int = 25,
    spaceSize: Int = 5
) {
    var offset = 0f
    var centerPos = 0
    if (total % 2 == 0) {
        centerPos = total / 2
        offset = ((spaceSize / 2) + (circleSize / 2)).toFloat()
    } else {
        centerPos = (total + 1) / 2
    }

    if (current > centerPos) {
        offset += (circleSize + spaceSize) * (centerPos - current).toFloat() - circleSize / 2
    } else if (current < centerPos) {
        offset += (circleSize + spaceSize) * (centerPos - current).toFloat() + circleSize / 2
    }


    Row(
        modifier = modifier.offset(x = offset.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..total) {
            if (i == current) {
                Box(
                    modifier = Modifier
                        .size(activeCircleSize.dp)
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
            } else {
                Box(
                    modifier = Modifier
                        .size(circleSize.dp)
                        .clip(CircleShape)
                        .background(
                            color = LocalColor.current.lightGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            if (i != total)
                Spacer(modifier = Modifier.width(spaceSize.dp))
        }
    }
}

@Composable
fun AboutMeGenderTitle(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.about_me_gender_title),
        color = LocalColor.current.black,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AboutMeGenderSelector(
    modifier: Modifier = Modifier,
    selectedGender: Gender = Gender.NONE,
    aboutMeGenderAction: (AboutMeGenderAction) -> Unit = {}
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(color = if (selectedGender == Gender.MALE) LocalColor.current.blue else LocalColor.current.lightGray)
                .clickable {
                    aboutMeGenderAction(AboutMeGenderAction.MALE_CLICK)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .height(40.dp),
                    painter = painterResource(id = R.drawable.mans_face_flat_style),
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.about_me_gender_male),
                    fontSize = 12.sp,
                    color = if (selectedGender == Gender.MALE) LocalColor.current.white else LocalColor.current.black
                )
            }
        }

        Spacer(modifier = Modifier.width(30.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(color = if (selectedGender == Gender.FEMALE) LocalColor.current.blue else LocalColor.current.lightGray)
                .clickable {
                    aboutMeGenderAction(AboutMeGenderAction.FEMALE_CLICK)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .height(40.dp),
                    painter = painterResource(id = R.drawable.woman_with_long_black_hair),
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.about_me_gender_female),
                    fontSize = 12.sp,
                    color = if (selectedGender == Gender.FEMALE) LocalColor.current.white else LocalColor.current.black
                )
            }
        }
    }
}

@Composable
fun AboutMeGenderBottomMenu(
    modifier: Modifier = Modifier,
    aboutMeGenderAction: (AboutMeGenderAction) -> Unit = {}

) {
    Row(modifier = modifier) {
        AboutMeGenderNavigationBtn(isNext = false, aboutMeGenderAction = aboutMeGenderAction)
        Spacer(modifier = Modifier.weight(1f))
        AboutMeGenderNavigationBtn(isNext = true, aboutMeGenderAction = aboutMeGenderAction)
    }
}

@Composable
fun AboutMeGenderNavigationBtn(
    isNext: Boolean = false,
    aboutMeGenderAction: (AboutMeGenderAction) -> Unit = {}
) {
    Button(
        onClick = { aboutMeGenderAction(if (isNext) AboutMeGenderAction.NEXT else AboutMeGenderAction.PREVIOUS) },
        modifier = Modifier
            .clip(CircleShape)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape
            )
            .size(50.dp)
            .testTag(if (isNext) TestTags.ABOUT_ME_GENDER_NEXT_BTN else TestTags.ABOUT_ME_GENDER_PREVIOUS_BTN),
        colors = ButtonDefaults.buttonColors(containerColor = if (isNext) LocalColor.current.blue else LocalColor.current.lightGray),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = if (isNext) R.drawable.baseline_keyboard_arrow_right_24 else R.drawable.baseline_keyboard_arrow_left_24),
            contentDescription = "Next",
            tint = if (isNext) LocalColor.current.white else LocalColor.current.black,
        )
    }
}

@Composable
fun AboutMeGenderErrorDialog(
    message: String = "",
    aboutMeGenderAction: (AboutMeGenderAction) -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { aboutMeGenderAction(AboutMeGenderAction.DIALOG_DISMISS) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = { aboutMeGenderAction(AboutMeGenderAction.DIALOG_OK) }
            ) {
                Text(
                    color = LocalColor.current.white,
                    text = stringResource(id = R.string.register_error_dialog_ok)
                )
            }
        },
        containerColor = LocalColor.current.white,
    )
}

@Preview(showBackground = true)
@Composable
fun AboutMeGenderContentPreview() {
    TokitokiTheme {
        val uiState = AboutMeGenderState(selectedGender = Gender.FEMALE)
        AboutMeGenderContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeGenderNavigationBtnPreview() {
    TokitokiTheme {
        AboutMeGenderNavigationBtn()
    }
}

