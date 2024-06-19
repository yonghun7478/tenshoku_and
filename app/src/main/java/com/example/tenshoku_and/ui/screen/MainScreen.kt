package com.example.tenshoku_and.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tenshoku_and.viewmodel.MainViewModel
import java.lang.reflect.Modifier

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
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
