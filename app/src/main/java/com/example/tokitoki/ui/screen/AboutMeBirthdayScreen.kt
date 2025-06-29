package com.example.tokitoki.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeBirthdayAction
import com.example.tokitoki.ui.constants.AboutMeNameAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.etc.TkBottomArrowNavigation
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
import com.example.tokitoki.ui.state.AboutMeBirthdayEvent
import com.example.tokitoki.ui.state.AboutMeBirthdayState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeBirthdayViewModel

@Composable
fun AboutMeBirthdayScreen(
    birthDay: String = "",
    isFromMyPage: Boolean = false,
    onAboutMeGenderScreen: () -> Unit = {},
    onAboutMeNameScreen: () -> Unit = {},
    onPrevScreen: (String) -> Unit = {},
    viewModel: AboutMeBirthdayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeBirthdayContents(
        uiState = uiState,
        aboutMeBirthdayAction = viewModel::aboutMeBirthdayAction,
        onBirthdayChanged = viewModel::onBirthdayChanged
    )

    LaunchedEffect(Unit) {
        viewModel.init(birthDay = birthDay, isFromMyPage = isFromMyPage)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeBirthdayEvent.ACTION -> {
                    when (event.action) {
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

                        AboutMeBirthdayAction.EDIT_OK -> {
                            if (viewModel.checkBirthday()) {
                                onPrevScreen(uiState.birthday)
                            } else {
                                viewModel.updateShowDialogState(true)
                            }
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
        TkIndicator(
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


        if (uiState.isEditMode) {
            TkBtn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                text = "修正する",
                textColor = LocalColor.current.white,
                backgroundColor = LocalColor.current.blue,
                action = aboutMeBirthdayAction,
                actionParam = AboutMeBirthdayAction.EDIT_OK
            )
        } else {
            TkBottomArrowNavigation(
                modifier = Modifier.padding(all = 10.dp),
                action = aboutMeBirthdayAction,
                nextActionParam = AboutMeBirthdayAction.NEXT,
                previousActionParam = AboutMeBirthdayAction.PREVIOUS,
            )
        }
    }

    if (uiState.showDialog) {
        TkAlertDialog(
            message = stringResource(R.string.about_me_validate_birthday),
            onDismissRequest = { aboutMeBirthdayAction(AboutMeBirthdayAction.DIALOG_OK) },
        )
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

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = birthday,
                selection = TextRange(birthday.length) // 커서를 기본적으로 끝으로 설정
            )
        )
    }

    LaunchedEffect(birthday) {
        if (birthday != textFieldValue.text) { // 불필요한 업데이트 방지
            textFieldValue = TextFieldValue(
                text = birthday,
                selection = TextRange(birthday.length) // 커서를 끝으로 설정
            )
        }
    }

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .testTag(TestTags.ABOUT_ME_BIRTHDAY_TEXT_FIELD),
        value = textFieldValue,
        singleLine = true,
        onValueChange = { newText ->
            textFieldValue = newText

            if (newText.text.length <= 8)
                onBirthdayChanged(newText.text)

            if (newText.text.length == 8) {
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

@Preview(showBackground = true)
@Composable
fun AboutMeBirthdayContentsPreview() {
    TokitokiTheme {
        AboutMeBirthdayContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeBirthdayContentsEditModePreview() {
    TokitokiTheme {
        AboutMeBirthdayContents(uiState = AboutMeBirthdayState(isEditMode = true))
    }
}

