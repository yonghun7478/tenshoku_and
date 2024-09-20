package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeBirthdayAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.state.AboutMeBirthdayEvent
import com.example.tokitoki.ui.state.AboutMeBirthdayState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeBirthdayViewModel

@Composable
fun AboutMeBirthdayScreen(
    onAboutMeGenderScreen: () -> Unit = {},
    onAboutMeNameScreen: () -> Unit = {},
    viewModel: AboutMeBirthdayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeBirthdayContents(
        uiState = uiState,
        aboutMeBirthdayAction = viewModel::aboutMeBirthdayAction,
        onBirthdayChanged = viewModel::onBirthdayChanged
    )

    LaunchedEffect(Unit) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeBirthdayEvent.ACTION -> {
                    when (event.action) {
                        AboutMeBirthdayAction.DIALOG_DISMISS -> {
                            viewModel.updateShowDialogState(false)
                        }

                        AboutMeBirthdayAction.DIALOG_OK -> {
                            viewModel.updateShowDialogState(false)
                        }

                        AboutMeBirthdayAction.NEXT -> {
                            if (viewModel.checkBirthday()) {
                                onAboutMeNameScreen()
                            } else {
                                viewModel.updateShowDialogState(true)
                            }
                        }

                        AboutMeBirthdayAction.NOTHING -> {

                        }

                        AboutMeBirthdayAction.PREVIOUS -> {
                            onAboutMeGenderScreen()
                        }
                    }
                }

                AboutMeBirthdayEvent.NOTHING -> {

                }
            }
        }
    }
}

@Composable
fun AboutMeBirthdayContents(
    uiState: AboutMeBirthdayState = AboutMeBirthdayState(),
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {},
    onBirthdayChanged: (String) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
            .testTag(TestTags.ABOUT_ME_BIRTHDAY_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AboutMeBirthdayIndicator(
            modifier = Modifier.padding(top = 30.dp),
            total = 20,
            current = 2
        )
        AboutMeBirthdayTitle(
            modifier = Modifier.padding(top = 10.dp)
        )
        AboutMeBirthdayInput(
            modifier = Modifier.padding(top = 30.dp),
            birthday = uiState.birthday,
            onBirthdayChanged = onBirthdayChanged,
        )
        Spacer(modifier = Modifier.weight(1f))

        AboutMeBirthdayBottomMenu(
            modifier = Modifier.padding(10.dp),
            aboutMeBirthdayAction = aboutMeBirthdayAction
        )
    }

    if (uiState.showDialog) {
        AboutMeBirthdayErrorDialog(
            message = stringResource(id = R.string.about_me_validate_birthday),
            aboutMeBirthdayAction = aboutMeBirthdayAction
        )
    }
}

@Composable
fun AboutMeBirthdayIndicator(
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
fun AboutMeBirthdayTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.about_me_birthday_title),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.about_me_birthday_sub_title),
            fontSize = 10.sp
        )
    }
}

@Composable
fun AboutMeBirthdayInput(
    modifier: Modifier = Modifier,
    birthday: String = "",
    onBirthdayChanged: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .testTag(TestTags.ABOUT_ME_BIRTHDAY_TEXT_FIELD),
        value = birthday,
        singleLine = true,
        onValueChange = { newText ->
            if (newText.length <= 8)
                onBirthdayChanged(newText)

            if (newText.length == 8) {
                focusManager.clearFocus()
            }
        },
        decorationBox = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val paddedStr = birthday.padEnd(8, ' ')
                for ((index, char) in paddedStr.withIndex()) {
                    AboutMeBirthdayTextFieldContainer(modifier = Modifier.width(30.dp), text = char)
                    if (index == 3 || index == 5) {
                        Text(
                            text = "/",
                            color = LocalColor.current.grayColor,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun AboutMeBirthdayTextFieldContainer(
    modifier: Modifier = Modifier,
    text: Char,
) {
    Box(
        modifier = modifier
            .height(40.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = 24.sp,
            text = text.toString(),
            color = LocalColor.current.grayColor,
        )

        HorizontalDivider(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = LocalColor.current.grayColor,
            thickness = 1.dp
        )
    }

}

@Composable
fun AboutMeBirthdayBottomMenu(
    modifier: Modifier = Modifier,
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {},
) {
    Row(modifier = modifier) {
        AboutMeBirthdayNavigationBtn(isNext = false, aboutMeBirthdayAction = aboutMeBirthdayAction)
        Spacer(modifier = Modifier.weight(1f))
        AboutMeBirthdayNavigationBtn(isNext = true, aboutMeBirthdayAction = aboutMeBirthdayAction)
    }
}

@Composable
fun AboutMeBirthdayNavigationBtn(
    isNext: Boolean = false,
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {},
) {
    Button(
        onClick = { aboutMeBirthdayAction(if (isNext) AboutMeBirthdayAction.NEXT else AboutMeBirthdayAction.PREVIOUS) },
        modifier = Modifier
            .clip(CircleShape)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape
            )
            .size(50.dp)
            .testTag(if (isNext) TestTags.ABOUT_ME_BIRTHDAY_NEXT_BTN else TestTags.ABOUT_ME_BIRTHDAY_PREVIOUS_BTN),
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
fun AboutMeBirthdayErrorDialog(
    message: String = "",
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { aboutMeBirthdayAction(AboutMeBirthdayAction.DIALOG_DISMISS) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = { aboutMeBirthdayAction(AboutMeBirthdayAction.DIALOG_OK) }
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
fun AboutMeBirthdayContentsPreview() {
    TokitokiTheme {
        AboutMeBirthdayContents()
    }
}
