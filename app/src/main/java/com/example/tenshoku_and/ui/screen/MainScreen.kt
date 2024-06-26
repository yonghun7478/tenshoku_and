package com.example.tenshoku_and.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tenshoku_and.ui.state.UserUiState
import com.example.tenshoku_and.viewmodel.MainViewModel
import java.lang.reflect.Modifier

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UserUiState.Loading -> {
            println("CYHH loading")
        }

        is UserUiState.Success -> {
            println("CYHH success ${(uiState as UserUiState.Success).users}")
        }

        is UserUiState.Error -> {
            println("CYHH error")
        }
    }

    MainContent()
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier()
) {
}

@Preview(showBackground = true)
@Composable
fun MainContentPreView(
    modifier: Modifier = Modifier()
) {

}
