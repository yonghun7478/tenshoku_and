package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeMyProfileViewModel

@Composable
fun AboutMeMyProfileScreen(
    onAboutMeProfInputScreen: () -> Unit = {},
    onIntroduceLikePageScreen: () -> Unit = {},
    viewModel: AboutMeMyProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeMyProfileContents()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->

        }
    }
}

@Composable
fun AboutMeMyProfileContents(
    modifier: Modifier = Modifier,
) {

}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileContentsPreview() {
    TokitokiTheme {
        AboutMeMyProfileContents()
    }
}