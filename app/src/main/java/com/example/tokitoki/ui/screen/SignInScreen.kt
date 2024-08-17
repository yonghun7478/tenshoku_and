package com.example.tokitoki.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.SignInEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.SignInAction
import com.example.tokitoki.ui.util.SignInConstants
import com.example.tokitoki.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiEvent by viewModel.uiEvent.collectAsState(initial = SignInEvent.NOTHING)

    SignInContent(
        onClick = {
            viewModel.clickListener(it)
        }
    )

    LaunchedEffect(uiEvent) {
        when (val currentUiEvent = uiEvent) {
            SignInEvent.NOTHING -> {
            }

            is SignInEvent.ACTION -> {
                Log.d(SignInConstants.TAG, "uiEvent.action ${currentUiEvent.action}")
            }
        }
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    onClick: (SignInAction) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(300.dp))
        TopLogo()
        Spacer(modifier = Modifier.weight(1f))
        SignInMenu(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick(SignInAction.Help)
                },
            text = stringResource(id = R.string.sign_in_new_member_help_title),
            color = LocalColor.current.blue,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )

        Row(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClick(SignInAction.Service)
                    },
                text = stringResource(id = R.string.terms_of_service),
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClick(SignInAction.Privacy)
                    },
                text = stringResource(id = R.string.privacy_policy),
                fontSize = 10.sp
            )

            Text(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onClick(SignInAction.Cookie)
                    },
                text = stringResource(id = R.string.cookie_policy),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun TopLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(70.dp)
            .offset(x = -(10).dp)
            .testTag("TopLogo"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.offset(x = 10.dp),
            painter = painterResource(id = R.drawable.pink_heart_logo),
            contentDescription = "TopLogoImage",
        )

        Text(
            text = stringResource(id = R.string.logo_name),
            color = LocalColor.current.grayColor,
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SignInMenu(
    modifier: Modifier = Modifier,
    onClick: (SignInAction) -> Unit = {},
) {
    Column(modifier = modifier.testTag("SignInMenu")) {
        SignMenuBtn(
            text = stringResource(id = R.string.google_btn_text),
            textColor = LocalColor.current.black,
            backgroundColor = LocalColor.current.white,
            iconRes = R.drawable.ic_google,
            isOutLine = true,
            signInAction = SignInAction.LoginWithGoogle,
            onClick = onClick
        )

        SignMenuBtn(
            text = stringResource(id = R.string.mail_btn_text),
            textColor = LocalColor.current.white,
            iconRes = R.drawable.ic_mail,
            backgroundColor = LocalColor.current.blue,
            signInAction = SignInAction.LoginWithEmail,
            onClick = onClick
        )
    }
}

@Composable
fun SignMenuBtn(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = LocalColor.current.black,
    backgroundColor: Color = LocalColor.current.white,
    iconRes: Int = -1,
    isOutLine: Boolean = false,
    signInAction: SignInAction = SignInAction.Nothing,
    onClick: (SignInAction) -> Unit = {},
) {
    if (isOutLine) {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth()
                .testTag("SignMenuBtn"),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            onClick = {
                onClick(signInAction)
            },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    tint = textColor,
                    contentDescription = "SignMenuBtnIcon"
                )
                Text(text = text, color = textColor)
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    } else {
        Button(
            modifier = modifier
                .fillMaxWidth()
                .testTag("SignMenuBtn"),
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            onClick = {
                onClick(signInAction)
            },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    tint = textColor,
                    contentDescription = "SignMenuBtnIcon"
                )
                Text(text = text, color = textColor)
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreView() {
    TokitokiTheme {
        SignInContent()
    }
}

@Preview(showBackground = true)
@Composable
fun TopLogoPreView() {
    TokitokiTheme {
        TopLogo()
    }
}

@Preview(showBackground = true)
@Composable
fun SignInMenuPreView() {
    TokitokiTheme {
        SignInMenu()
    }
}

@Preview(showBackground = true)
@Composable
fun SignMenuBtnPreView() {
    TokitokiTheme {
        SignMenuBtn(
            text = "メールアドレスでサインイン",
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            iconRes = R.drawable.ic_mail
        )
    }
}
