package com.example.tokitoki.ui.screen


import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.RegisterWithEmailAction
import com.example.tokitoki.ui.constants.RegisterWithEmailConstants
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.buttons.TkRoundedIcon
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
import com.example.tokitoki.ui.state.RegisterWithEmailEvent
import com.example.tokitoki.ui.state.RegisterWithEmailState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.RegisterWithEmailViewModel

@Composable
fun RegisterWithEmailScreen(
    onEmailVerification: () -> Unit = {},
    viewModel: RegisterWithEmailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterWithEmailContents(
        uiState = uiState,
        registerWithEmailAction = viewModel::registerWithEmailAction,
        onEmailChanged = viewModel::onEmailChanged,
        updateShowDialogState = viewModel::updateShowDialogState,
    )

    LaunchedEffect(Unit) {
        viewModel.initState()

        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                RegisterWithEmailEvent.NOTHING -> {
                }

                is RegisterWithEmailEvent.ACTION -> {
                    when (uiEvent.action) {
                        RegisterWithEmailAction.Submit -> {
                            val result = viewModel.validateEmail(uiState.email)

                            if (result) {
                                onEmailVerification()
                            } else
                                viewModel.updateShowDialogState(true)
                        }

                        else -> {}
                    }
                    Log.d(RegisterWithEmailConstants.TAG, "uiEvent.action ${uiEvent.action}")
                }
            }
        }
    }
}

@Composable
fun RegisterWithEmailContents(
    modifier: Modifier = Modifier,
    uiState: RegisterWithEmailState = RegisterWithEmailState(),
    registerWithEmailAction: (RegisterWithEmailAction) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    updateShowDialogState: (Boolean) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
            .testTag(TestTags.REGISTER_WITH_EMAIL_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TkRoundedIcon(
            modifier = Modifier
                .padding(top = 100.dp),
            iconRes = R.drawable.ic_mail,
            size = 50.dp,
            colors = listOf(LocalColor.current.blue, LocalColor.current.white),
            shape = CircleShape,
            iconTint = LocalColor.current.white
        )
        Spacer(modifier = modifier.height(20.dp))
        Text(
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp,
            text = stringResource(id = R.string.register_title)
        )
        Spacer(modifier = modifier.height(20.dp))
        RegisterWithEmailTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            email = uiState.email,
            onEmailChanged = onEmailChanged
        )
        Spacer(modifier = Modifier.height(10.dp))
        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = stringResource(R.string.register_btn_title),
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            action = registerWithEmailAction,
            actionParam = RegisterWithEmailAction.Submit
        )
    }

    if (uiState.showDialog) {
        val errorMsg = stringResource(R.string.validate_error_msg)

        TkAlertDialog(
            message = errorMsg,
            onDismissRequest = { updateShowDialogState(false) }
        )
    }
}

@Composable
fun RegisterWithEmailTextField(
    modifier: Modifier = Modifier,
    email: String = "",
    onEmailChanged: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = email,
        onValueChange = onEmailChanged,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = LocalColor.current.blue,
            unfocusedIndicatorColor = LocalColor.current.blue,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.register_textfield_pressholder)
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .testTag(TestTags.REGISTER_WITH_EMAIL_TEXT_FIELD)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterWithEmailContentsPreView() {
    TokitokiTheme {
        RegisterWithEmailContents()
    }
}

@Preview
@Composable
fun RegisterWithEmailTextFieldPreview() {
    TokitokiTheme {
        RegisterWithEmailTextField()
    }
}
