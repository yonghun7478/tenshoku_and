package com.example.tokitoki.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeNameAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
import com.example.tokitoki.ui.screen.components.etc.TkBottomArrowNavigation
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.state.AboutMeNameEvent
import com.example.tokitoki.ui.state.AboutMeNameState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeNameViewModel

@Composable
fun AboutMeNameScreen(
    name: String = "",
    onAboutMeBirthdayScreen: () -> Unit = {},
    onAboutMeSecondScreen: () -> Unit = {},
    viewModel: AboutMeNameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeNameContents(
        uiState = uiState,
        aboutMeNameAction = viewModel::aboutMeNameAction,
        onNameChanged = viewModel::onNameChanged
    )

    LaunchedEffect(Unit) {
        viewModel.init(name)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeNameEvent.ACTION -> {
                    when (event.action) {
                        AboutMeNameAction.DIALOG_OK -> {
                            viewModel.updateShowDialogState(false)
                        }

                        AboutMeNameAction.NEXT -> {
                            if (viewModel.checkName()) {
                                onAboutMeSecondScreen()
                            } else {
                                viewModel.updateShowDialogState(true)
                            }
                        }

                        AboutMeNameAction.NOTHING -> {}
                        AboutMeNameAction.PREVIOUS -> {
                            onAboutMeBirthdayScreen()
                        }
                    }
                }

                AboutMeNameEvent.NOTHING -> {

                }
            }
        }
    }
}

@Composable
fun AboutMeNameContents(
    uiState: AboutMeNameState = AboutMeNameState(),
    aboutMeNameAction: (AboutMeNameAction) -> Unit = {},
    onNameChanged: (String) -> Unit = {}
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
            .testTag(TestTags.ABOUT_ME_NAME_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TkIndicator(
            modifier = Modifier.padding(top = 30.dp),
            total = 20,
            current = 3
        )

        AboutMeNameTitle(
            modifier = Modifier.padding(top = 10.dp)
        )

        AboutMeNameTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(horizontal = 10.dp),
            name = uiState.name,
            onNameChanged = onNameChanged
        )

        Spacer(modifier = Modifier.weight(1f))

        TkBottomArrowNavigation(
            modifier = Modifier.padding(all = 10.dp),
            action = aboutMeNameAction,
            nextActionParam = AboutMeNameAction.NEXT,
            previousActionParam = AboutMeNameAction.PREVIOUS,
        )
    }

    if (uiState.showDialog) {
        TkAlertDialog(
            message = stringResource(R.string.about_me_validate_name),
            onDismissRequest = { aboutMeNameAction(AboutMeNameAction.DIALOG_OK) },
        )
    }
}

@Composable
fun AboutMeNameTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.about_me_name_title),
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
fun AboutMeNameTextField(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChanged: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = name,
                selection = TextRange(name.length) // 커서를 기본적으로 끝으로 설정
            )
        )
    }

    LaunchedEffect(name) {
        if (name != textFieldValue.text) { // 불필요한 업데이트 방지
            textFieldValue = TextFieldValue(
                text = name,
                selection = TextRange(name.length) // 커서를 끝으로 설정
            )
        }
    }

    TextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            onNameChanged(it.text)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = LocalColor.current.blue,
            unfocusedIndicatorColor = LocalColor.current.blue,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.about_me_name_pressholder)
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .testTag(TestTags.ABOUT_ME_NAME_TEXT_FIELD)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeNameContentsPreview() {
    TokitokiTheme {
        AboutMeNameContents()
    }
}