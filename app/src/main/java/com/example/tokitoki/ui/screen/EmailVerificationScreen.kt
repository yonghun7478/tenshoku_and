package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import com.example.tokitoki.ui.constants.EmailVerificationAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.buttons.TkRoundedIcon
import com.example.tokitoki.ui.state.EmailVerificationEvent
import com.example.tokitoki.ui.state.EmailVerificationState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.EmailVerificationViewModel

@Composable
fun EmailVerificationScreen(
    onAgreementConfirmationScreen: () -> Unit = {},
    viewModel: EmailVerificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    EmailVerificationContents(
        uiState = uiState,
        onPasswordValueChange = viewModel::onPasswordValueChange,
        emailVerificationAction = viewModel::emailVerificationAction,
        updateShowDialogState = viewModel::updateShowDialogState,
    )

    LaunchedEffect(Unit) {
        viewModel.initState()

        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                EmailVerificationEvent.NOTHING -> {

                }

                is EmailVerificationEvent.ACTION -> {
                    when (uiEvent.action) {
                        EmailVerificationAction.NOTHING -> {

                        }

                        EmailVerificationAction.SUBMIT -> {
                            var result = viewModel.validateCode(uiState.code)

                            if (result)
                                onAgreementConfirmationScreen()
                            else {
                                viewModel.updateShowDialogState(true)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmailVerificationContents(
    modifier: Modifier = Modifier,
    uiState: EmailVerificationState = EmailVerificationState(),
    onPasswordValueChange: (String) -> Unit = {},
    emailVerificationAction: (EmailVerificationAction) -> Unit = {},
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
            }.testTag(TestTags.EMAIL_VERIFICATION_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TkRoundedIcon(
            modifier = Modifier.padding(top = 70.dp)
        )
        EmailVerificationText(
            modifier = Modifier.padding(top = 5.dp)
        )
        EmailVerificationTextField(
            modifier = Modifier.padding(top = 60.dp, start = 10.dp, end = 10.dp),
            password = uiState.code,
            onPasswordValueChange = onPasswordValueChange,
            emailVerificationAction = emailVerificationAction,
        )
    }

    if (uiState.showDialog) {
        val errorMsg = stringResource(R.string.validate_email_code_error_msg)

        EmailVerificationErrorDialog(
            message = errorMsg,
            updateShowDialogState = updateShowDialogState
        )
    }
}

@Composable
fun EmailVerificationText(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier,
            text = stringResource(R.string.verification_text),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun EmailVerificationTextField(
    modifier: Modifier = Modifier,
    password: String = "",
    onPasswordValueChange: (String) -> Unit = {},
    emailVerificationAction: (EmailVerificationAction) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        BasicTextField(
            value = password,
            singleLine = true,
            onValueChange = { newText ->
                onPasswordValueChange(newText)
                if (newText.length == 6) {
                    focusManager.clearFocus()
                    emailVerificationAction(EmailVerificationAction.SUBMIT)
                }
            },
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    password.forEachIndexed { _, char ->
                        EmailVerificationTextFieldContainer(
                            modifier = Modifier.weight(1f),
                            text = char,
                        )
                    }
                    repeat(6 - password.length) {
                        EmailVerificationTextFieldContainer(
                            modifier = Modifier.weight(1f),
                            text = ' ',
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .focusRequester(focusRequester)
                .testTag(TestTags.EMAIL_VERIFICATION_TEXT_FIELD)
        )

        EmailVerificationAdviceText(
            modifier = Modifier
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun EmailVerificationAdviceText(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.verification_advice1),
            fontSize = 10.sp
        )
        Text(
            text = stringResource(R.string.verification_advice2),
            color = LocalColor.current.blue,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun EmailVerificationTextFieldContainer(
    modifier: Modifier = Modifier,
    text: Char,
) {
    val shape = remember { RoundedCornerShape(4.dp) }

    Card(
        modifier = modifier
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = LocalColor.current.lightGray,
                    shape = shape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                fontSize = 30.sp,
                text = text.toString(),
                color = LocalColor.current.black
            )
        }
    }
}

@Composable
fun EmailVerificationErrorDialog(
    message: String = "",
    updateShowDialogState: (Boolean) -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier
            .testTag(TestTags.REGISTER_WITH_EMAIL_ERROR_DIALOG),
        onDismissRequest = { updateShowDialogState(false) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = { updateShowDialogState(false) }
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
fun DigitTextFieldPreview() {
    TokitokiTheme {
        EmailVerificationContents()
    }
}
