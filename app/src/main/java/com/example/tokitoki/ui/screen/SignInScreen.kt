package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .weight(1.5f)
        ) {
            TopLogo(
                modifier = Modifier.offset(y = (50).dp)
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "メールアドレスでログイン")
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "メールアドレスでログイン")
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "メールアドレスでログイン")
            }
        }
    }
}

@Composable
fun TopLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.pink_heart_logo),
            contentDescription = "Image description",
            modifier = Modifier.offset(x = 10.dp)
        )

        Text(
            text = "Tokitoki",
            color = LocalColor.current.grayColor,
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold
        )
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
