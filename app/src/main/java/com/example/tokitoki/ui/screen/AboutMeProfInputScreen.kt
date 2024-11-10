package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeProfInputViewModel

@Composable
fun AboutMeProfInputScreen(
    onAboutMePhotoUploadScreen: () -> Unit = {},
    onAboutMeProfScreen: () -> Unit = {},
    viewModel: AboutMeProfInputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeProfInputContents()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->

        }
    }
}

@Composable
fun AboutMeProfInputContents() {

}

@Preview(showBackground = true)
@Composable
fun AboutMeProfileInputContentsPreview() {
    TokitokiTheme {
        AboutMeProfInputContents()
    }
}