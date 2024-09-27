package com.example.tokitoki.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.tokitoki.ui.screen.components.etc.TkBottomArrowNavigation
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
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
        TkIndicator(
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

        TkBottomArrowNavigation(
            modifier = Modifier.padding(10.dp),
            action = aboutMeGenderAction,
            previousActionParam = AboutMeGenderAction.PREVIOUS,
            nextActionParam = AboutMeGenderAction.NEXT
        )
    }

    if (uiState.showDialog) {
        TkAlertDialog(
            message = stringResource(R.string.validate_agreement_error_msg),
            onDismissRequest = { aboutMeGenderAction(AboutMeGenderAction.DIALOG_OK) },
        )
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
                }
                .testTag(TestTags.ABOUT_ME_GENDER_MALE),
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
                }
                .testTag(TestTags.ABOUT_ME_GENDER_FEMALE),
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

@Preview(showBackground = true)
@Composable
fun AboutMeGenderContentPreview() {
    TokitokiTheme {
        val uiState = AboutMeGenderState(selectedGender = Gender.FEMALE)
        AboutMeGenderContents(uiState = uiState)
    }
}
