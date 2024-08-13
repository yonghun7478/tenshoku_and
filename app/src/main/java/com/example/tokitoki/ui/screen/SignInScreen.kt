package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.R
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.viewmodel.SignInViewModel

@Composable

fun MainScreen(
    viewModel: SignInViewModel = hiltViewModel()
) {
    MainContent()
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(300.dp))
        TopLogo()
        Spacer(modifier = Modifier.weight(1f))
        SignInMenu(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.sign_in_new_member_help_title),
            color = LocalColor.current.blue,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = stringResource(id = R.string.terms_of_service),
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = stringResource(id = R.string.privacy_policy),
                fontSize = 10.sp
            )
            Text(text = stringResource(id = R.string.cookie_policy), fontSize = 10.sp)
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
            .offset(x = -(10).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pink_heart_logo),
            contentDescription = null,
            modifier = Modifier.offset(x = 10.dp)
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SignMenuBtn(
            text = stringResource(id = R.string.google_btn_text),
            textColor = LocalColor.current.black,
            backgroundColor = LocalColor.current.white,
            iconRes = R.drawable.ic_google,
            isOutLine = true
        )

        SignMenuBtn(
            text = stringResource(id = R.string.mail_btn_text),
            textColor = LocalColor.current.white,
            iconRes = R.drawable.ic_mail,
            backgroundColor = LocalColor.current.blue,
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
    onClick: () -> Unit = {},
) {
    if (isOutLine) {
        OutlinedButton(
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    tint = textColor,
                    contentDescription = null
                )
                Text(text = text, color = textColor)
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    } else {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    tint = textColor,
                    contentDescription = null
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
        MainContent()
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
