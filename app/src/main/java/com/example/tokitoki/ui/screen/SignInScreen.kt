package com.example.tokitoki.ui.screen

import android.app.Activity
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.tokitoki.ui.constants.SignInAction
import com.example.tokitoki.ui.constants.SignInConstants
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.buttons.TkOutlineBtn
import com.example.tokitoki.ui.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    onRegisterWithEmail: () -> Unit = {},
    viewModel: SignInViewModel = hiltViewModel()
) {
    val activityContext = LocalContext.current as Activity

    SignInContent(
        signInAction = {
            viewModel.signInAction(it)
        }
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                SignInEvent.NOTHING -> {
                }

                is SignInEvent.ACTION -> {
                    when (uiEvent.action) {
                        SignInAction.LoginWithEmail -> {
                            if(viewModel.checkToken()) {

                            } else {
                                onRegisterWithEmail()
                            }
                        }

                        SignInAction.LoginWithGoogle -> {
                            viewModel.signInGoogle(activityContext)
                        }

                        else -> {}
                    }
                    Log.d(SignInConstants.TAG, "uiEvent.action ${uiEvent.action}")
                }
            }
        }
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    signInAction: (SignInAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(TestTags.SIGN_IN_CONTENTS),
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
            signInAction = signInAction
        )
        SignInSupportLink(modifier = Modifier.padding(top = 24.dp), signInAction = signInAction)
        SignInSubLinks(modifier = Modifier.padding(vertical = 12.dp), signInAction = signInAction)
    }
}

@Composable
fun TopLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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
    signInAction: (SignInAction) -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        TkOutlineBtn(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.google_btn_text),
            textColor = LocalColor.current.black,
            backgroundColor = LocalColor.current.white,
            iconRes = R.drawable.ic_google,
            action = signInAction,
            actionParam = SignInAction.LoginWithGoogle
        )

        TkBtn(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.mail_btn_text),
            textColor = LocalColor.current.white,
            iconRes = R.drawable.ic_mail,
            backgroundColor = LocalColor.current.blue,
            action = signInAction,
            actionParam = SignInAction.LoginWithEmail
        )
    }
}

@Composable
fun SignInSupportLink(
    modifier: Modifier = Modifier,
    signInAction: (SignInAction) -> Unit = {},
) {
    Text(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                signInAction(SignInAction.Help)
            },
        text = stringResource(id = R.string.sign_in_new_member_help_title),
        color = LocalColor.current.blue,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    )
}

@Composable
fun SignInSubLinks(
    modifier: Modifier = Modifier,
    signInAction: (SignInAction) -> Unit = {},
) {
    Row(
        modifier = modifier
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
                    signInAction(SignInAction.Service)
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
                    signInAction(SignInAction.Privacy)
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
                    signInAction(SignInAction.Cookie)
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
