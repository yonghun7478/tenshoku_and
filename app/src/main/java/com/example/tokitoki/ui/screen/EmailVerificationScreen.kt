package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.EmailVerificationViewModel

@Composable
fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel = hiltViewModel()
) {
    EmailVerificationContents()
}

@Composable
fun EmailVerificationContents(
    modifier: Modifier = Modifier
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailVerificationEmailIcon(
            modifier = Modifier.padding(top = 70.dp)
        )
        EmailVerificationText(
            modifier = Modifier.padding(top = 5.dp)
        )
        EmailVerificationTextField(
            modifier = Modifier.padding(top = 60.dp, start = 10.dp, end = 10.dp)
        )
    }
}

@Composable
fun EmailVerificationEmailIcon(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(60.dp)
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
    onVerificationComplete: () -> Unit = {},
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        BasicTextField(
            value = text,
            singleLine = true,
            onValueChange = { newText ->
                if (newText.length <= 6) {
                    text = newText

                    if (newText.length == 6)
                        focusManager.clearFocus()
                }
            },
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    text.forEachIndexed { _, char ->
                        EmailVerificationTextFieldContainer(
                            text = char,
                        )
                    }
                    repeat(6 - text.length) {
                        EmailVerificationTextFieldContainer(
                            text = ' ',
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.focusRequester(focusRequester)
        )

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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(text) {
        if (text.length == 6) {
            onVerificationComplete()
        }
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
            .size(
                width = 55.dp,
                height = 80.dp,
            )
    ) {
        Box(
            modifier = modifier
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

@Preview(showBackground = true)
@Composable
fun DigitTextFieldPreview() {
    TokitokiTheme {
        EmailVerificationContents()
    }
}
