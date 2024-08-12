package com.example.tokitoki.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.viewmodel.backup.MainViewModel
import androidx.compose.ui.Modifier
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
    Column {
        Text(text = "test")
        Text(text = "test")
        Text(text = "test")
        Text(text = "test")
        Text(text = "test")
        Text(text = "test")
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreView(
    modifier: Modifier = Modifier
) {
    TokitokiTheme {
        MainContent()
    }
}
