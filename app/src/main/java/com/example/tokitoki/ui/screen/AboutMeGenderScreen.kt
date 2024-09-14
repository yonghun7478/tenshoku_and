package com.example.tokitoki.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.state.AboutMeGenderState
import com.example.tokitoki.ui.state.Gender
import com.example.tokitoki.ui.viewmodel.AboutMeGenderViewModel

@Composable
fun AboutMeGenderScreen(
    onAboutMeBirthDayScreen: () -> Unit = {},
    viewModel: AboutMeGenderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeGenderContent(
        uiState = uiState,
        onGenderSelected = viewModel::onGenderSelected
    )
}

@Composable
fun AboutMeGenderContent(
    uiState: AboutMeGenderState,
    onGenderSelected: (Gender) -> Unit
) {

}