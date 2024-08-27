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
import com.example.tokitoki.ui.util.DrawableSemantics
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

            else -> {}
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
        TopLogo(
            modifier = Modifier
                .padding(top = 300.dp)
                .offset(x = -(10).dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        SignInMenu(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onClick = onClick
        )
        SignInSupportLink(modifier = Modifier.padding(top = 24.dp), onClick = onClick)
        SignInSubLinks(modifier = Modifier.padding(vertical = 12.dp), onClick = onClick)
    }
}

@Composable
fun TopLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.testTag("TopLogo"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .offset(x = 10.dp)
                .height(70.dp)
                .then(DrawableSemantics.withDrawableId(resId = R.drawable.pink_heart_logo)),
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
    Column(
        modifier = modifier
            .testTag("SignInMenu")
    ) {
        SignMenuOutlinedBtn(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.google_btn_text),
            textColor = LocalColor.current.black,
            backgroundColor = LocalColor.current.white,
            iconRes = R.drawable.ic_google,
            signInAction = SignInAction.LoginWithGoogle,
            onClick = onClick
        )

        SignMenuBtn(
            modifier = Modifier
                .fillMaxWidth(),
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
fun SignMenuOutlinedBtn(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = LocalColor.current.black,
    backgroundColor: Color = LocalColor.current.white,
    iconRes: Int = -1,
    signInAction: SignInAction = SignInAction.Nothing,
    onClick: (SignInAction) -> Unit = {},
) {
    OutlinedButton(
        modifier = modifier.testTag("SignMenuOutlinedBtn"),
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
                modifier = DrawableSemantics.withDrawableId(resId = iconRes),
                painter = painterResource(id = iconRes),
                tint = textColor,
                contentDescription = "SignMenuOutlinedBtnIcon"
            )
            Text(text = text, color = textColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(1.dp))
        }
    }

}

@Composable
fun SignMenuBtn(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = LocalColor.current.black,
    backgroundColor: Color = LocalColor.current.white,
    iconRes: Int = -1,
    signInAction: SignInAction = SignInAction.Nothing,
    onClick: (SignInAction) -> Unit = {},
) {
    Button(
        modifier = modifier.testTag("SignMenuBtn"),
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
                modifier = DrawableSemantics.withDrawableId(resId = iconRes),
                painter = painterResource(id = iconRes),
                tint = textColor,
                contentDescription = "SignMenuBtnIcon"
            )
            Text(text = text, color = textColor, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(1.dp))
        }
    }

}

@Composable
fun SignInSupportLink(
    modifier: Modifier = Modifier,
    onClick: (SignInAction) -> Unit = {},
) {
    Text(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(SignInAction.Help)
            }
            .testTag("SignInSupportLink"),
        text = stringResource(id = R.string.sign_in_new_member_help_title),
        color = LocalColor.current.blue,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    )
}

@Composable
fun SignInSubLinks(
    modifier: Modifier = Modifier,
    onClick: (SignInAction) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("SignInSubLinks"),
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
            fontSize = 10.sp,
            color = LocalColor.current.grayColor
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
            fontSize = 10.sp,
            color = LocalColor.current.grayColor
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
            fontSize = 10.sp,
            color = LocalColor.current.grayColor
        )
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
fun SignMenuOutlinedBtnPreView() {
    TokitokiTheme {
        SignMenuOutlinedBtn(
            text = stringResource(id = R.string.google_btn_text),
            textColor = LocalColor.current.black,
            backgroundColor = LocalColor.current.white,
            iconRes = R.drawable.ic_google,
            signInAction = SignInAction.LoginWithGoogle,
        )
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

@Preview(showBackground = true)
@Composable
fun SignInSupportLinkPreView() {
    TokitokiTheme {
        SignInSupportLink()
    }
}

@Preview(showBackground = true)
@Composable
fun SignInSubLinksPreView() {
    TokitokiTheme {
        SignInSubLinks()
    }
}
