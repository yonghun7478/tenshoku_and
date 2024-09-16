package com.example.tokitoki.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeBirthdayAction
import com.example.tokitoki.ui.state.AboutMeBirthdayState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeBirthdayViewModel

@Composable
fun AboutMeBirthdayScreen(
    onAboutMeGenderScreen: () -> Unit = {},
    onAboutMeNameScreen: () -> Unit = {},
    viewModel: AboutMeBirthdayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init()
    }
}

@Composable
fun AboutMeBirthdayContents(
    uiState: AboutMeBirthdayState = AboutMeBirthdayState(),
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {}
) {

}

@Composable
fun AboutMeBirthdayErrorDialog(
    message: String = "",
    aboutMeBirthdayAction: (AboutMeBirthdayAction) -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { aboutMeBirthdayAction(AboutMeBirthdayAction.DIALOG_DISMISS) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = { aboutMeBirthdayAction(AboutMeBirthdayAction.DIALOG_OK) }
            ) {
                Text(
                    color = LocalColor.current.white,
                    text = stringResource(id = R.string.register_error_dialog_ok)
                )
            }
        },
        containerColor = LocalColor.current.white,
    )
}

@Preview
@Composable
fun AboutMeBirthdayContentsPreview() {
    TokitokiTheme {
        AboutMeBirthdayContents()
    }
}
