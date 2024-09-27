package com.example.tokitoki.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeNameAction
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
import com.example.tokitoki.ui.state.AboutMeNameState
import com.example.tokitoki.ui.viewmodel.AboutMeNameViewModel

@Composable
fun AboutMeNameScreen(
    onAboutMeBirthdayScreen: () -> Unit = {},
    onAboutMeSecondScreen: () -> Unit = {},
    viewModel: AboutMeNameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeNameContents(
        uiState = uiState,
        aboutMeNameAction = viewModel::aboutMeNameAction,
        onNameChanged = viewModel::onNameChanged
    )

    LaunchedEffect(Unit) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->

        }
    }
}

@Composable
fun AboutMeNameContents(
    uiState: AboutMeNameState = AboutMeNameState(),
    aboutMeNameAction: (AboutMeNameAction) -> Unit = {},
    onNameChanged: (String) -> Unit = {}
) {
    Column {

    }

    if (uiState.showDialog) {
        TkAlertDialog(
            message = stringResource(R.string.about_me_validate_name),
            onDismissRequest = { aboutMeNameAction(AboutMeNameAction.DIALOG_OK) },
        )
    }
}
