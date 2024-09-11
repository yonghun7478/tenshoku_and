package com.example.tokitoki.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.constants.AgreementConfirmationAction
import com.example.tokitoki.ui.state.AgreementConfirmationEvent
import com.example.tokitoki.ui.viewmodel.AgreementConfirmationViewModel

@Composable
fun AgreementConfirmationScreen(
    onAboutMeScreen: () -> Unit = {},
    viewModel: AgreementConfirmationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AgreementConfirmationContents()

    LaunchedEffect(Unit) {
        viewModel.initState()
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                AgreementConfirmationEvent.NOTHING -> {

                }

                is AgreementConfirmationEvent.ACTION -> {
                    when (uiEvent.action) {
                        AgreementConfirmationAction.NOTHING -> {

                        }

                        AgreementConfirmationAction.CHECKBOX_AGE_CLICK -> {
                            viewModel.onAgreementChange(true)
                        }

                        AgreementConfirmationAction.CHECKBOX_POLICY_CLICK -> {
                            viewModel.onAgreementChange(false)
                        }

                        AgreementConfirmationAction.SUBMIT -> {
                            onAboutMeScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AgreementConfirmationContents() {
}