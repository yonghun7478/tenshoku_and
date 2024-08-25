package com.example.tokitoki.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterContents()
}

@Composable
fun RegisterContents(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .background(color = LocalColor.current.black, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = DrawableSemantics.withDrawableId(resId = R.drawable.ic_mail),
            painter = painterResource(id = R.drawable.ic_mail),
            tint = LocalColor.current.white,
            contentDescription = "SignMenuBtnIcon"
        )
    }
}

@Composable
fun EmailIconWithGradientBackground() {
}

@Preview
@Composable
fun EmailIconWithGradientBackgroundPreview() {
    TokitokiTheme {
        EmailIconWithGradientBackground()
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterContentsPreView() {
    TokitokiTheme {
        RegisterContents()
    }
}
