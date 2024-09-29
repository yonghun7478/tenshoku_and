package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.state.AboutMeInterestEvent
import com.example.tokitoki.ui.state.AboutMeInterestState
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeInterestViewModel

@Composable
fun AboutMeInterestScreen(
    onAboutMeSecondScreen: () -> Unit = {},
    onAboutMeThirdScreen: () -> Unit = {},
    viewModel: AboutMeInterestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeInterestContents(
        uiState = uiState,
        aboutMeInterestAction = viewModel::aboutMeInterestAction,
    )

    LaunchedEffect(Unit) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeInterestEvent.ACTION -> {
                    when (event.action) {
                        AboutMeInterestAction.CLICK_INTEREST -> {}
                        AboutMeInterestAction.DIALOG_OK -> {}
                        AboutMeInterestAction.NEXT -> {}
                        AboutMeInterestAction.NOTHING -> {}
                        AboutMeInterestAction.PREVIOUS -> {}
                    }
                }

                AboutMeInterestEvent.NOTHING -> {}
            }
        }
    }
}

@Composable
fun AboutMeInterestContents(
    uiState: AboutMeInterestState = AboutMeInterestState(),
    aboutMeInterestAction: (AboutMeInterestAction) -> Unit = {},
) {

}

@Preview(showBackground = true)
@Composable
fun AboutMeInterestContentsPreview() {
    TokitokiTheme {
        AboutMeInterestContents()
    }
}