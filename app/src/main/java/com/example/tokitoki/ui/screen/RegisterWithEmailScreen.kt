package com.example.tokitoki.ui.screen


import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.RegisterWithEmailAction
import com.example.tokitoki.ui.constants.RegisterWithEmailConstants
import com.example.tokitoki.ui.state.RegisterWithEmailEvent
import com.example.tokitoki.ui.state.RegisterWithEmailState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.RegisterWithEmailViewModel

@Composable
fun RegisterWithEmailScreen(
    viewModel: RegisterWithEmailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RegisterWithEmailContents(
        uiState = uiState,
        onClick = viewModel::clickListener,
        onEmailChanged = viewModel::onEmailChanged,
        onDismiss = viewModel::onDismiss,
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                RegisterWithEmailEvent.NOTHING -> {
                }

                is RegisterWithEmailEvent.ACTION -> {
                    when (uiEvent.action) {
                        RegisterWithEmailAction.Submit -> {
                            viewModel.submit(uiState.email)
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
    onClick: (RegisterWithEmailAction) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
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
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RegisterWithEmailIcon(
            modifier = Modifier
                .padding(top = 100.dp)
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
        RegisterWithEmailSubmitBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = onClick
        )
    }

    if (uiState.showDialog) {
        val errorMsg = stringResource(R.string.validate_error_msg)

        RegisterWithEmailErrorDialog(
            message = errorMsg,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun RegisterWithEmailIcon(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        LocalColor.current.blue,
                        LocalColor.current.white
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = DrawableSemantics.withDrawableId(resId = R.drawable.ic_mail),
            painter = painterResource(id = R.drawable.ic_mail),
            tint = LocalColor.current.white,
            contentDescription = "RegisterWithEmailIcon"
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
        modifier = modifier.focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@Composable
fun RegisterWithEmailSubmitBtn(
    modifier: Modifier = Modifier,
    onClick: (RegisterWithEmailAction) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Button(
        modifier = modifier
            .testTag("RegisterWithEmailSubmitBtn"),
        colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
        onClick = {
            focusManager.clearFocus()
            onClick(RegisterWithEmailAction.Submit)
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = R.string.register_btn_title))
        }
    }
}

@Composable
fun RegisterWithEmailErrorDialog(
    message: String = "",
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = onDismiss
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
fun RegisterWithEmailContentsPreView() {
    TokitokiTheme {
        RegisterWithEmailContents()
    }
}

@Preview
@Composable
fun RegisterWithEmailIconPreview() {
    TokitokiTheme {
        RegisterWithEmailIcon()
    }
}

@Preview
@Composable
fun RegisterWithEmailTextFieldPreview() {
    TokitokiTheme {
        RegisterWithEmailTextField()
    }
}

@Preview
@Composable
fun RegisterWithEmailSubmitBtnPreview() {
    TokitokiTheme {
        RegisterWithEmailSubmitBtn()
    }
}

@Preview
@Composable
fun RegisterWithEmailErrorDialogPreview() {
    TokitokiTheme {
        RegisterWithEmailErrorDialog()
    }
}
