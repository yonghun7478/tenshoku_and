package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.VerifyEmailUseCase
import com.example.tokitoki.ui.constants.EmailVerificationAction
import com.example.tokitoki.ui.state.EmailVerificationEvent
import com.example.tokitoki.ui.state.EmailVerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.CheckEmailRegisteredUseCase
import com.example.tokitoki.domain.usecase.SaveRegistrationTokenUseCase
import com.example.tokitoki.domain.usecase.SaveTokensUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.ui.state.VerificationType

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val saveRegistrationTokenUseCase: SaveRegistrationTokenUseCase,
    private val checkEmailRegisteredUseCase: CheckEmailRegisteredUseCase,
    private val saveTokensUseCase: SaveTokensUseCase,
    private val setMyProfileUseCase: SetMyProfileUseCase,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<EmailVerificationEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(EmailVerificationState())
    val uiState: StateFlow<EmailVerificationState> = _uiState.asStateFlow()

    fun initState() {
        _uiState.value = EmailVerificationState()
    }

    fun onPasswordValueChange(newText: String) {
        if (newText.length <= 6) {
            _uiState.value = _uiState.value.copy(code = newText)
        }
    }

    fun emailVerificationAction(action: EmailVerificationAction) {
        viewModelScope.launch {
            _uiEvent.emit(EmailVerificationEvent.ACTION(action))
        }
    }

    suspend fun processCodeValidation(code: String): VerificationType {
        val profile = getMyProfileUseCase()
        val result = verifyEmailUseCase(profile.email, code)

        if (result is ResultWrapper.Success) {
            saveRegistrationTokenUseCase(result.data.registrationToken)
            val isRegistered = checkEmailRegisteredUseCase(profile.email)

            if (isRegistered is ResultWrapper.Success) {
                return if (isRegistered.data.isRegistered) {
                    saveTokensUseCase(isRegistered.data.accessToken, isRegistered.data.refreshToken)
                    val dummyProfile = profile.copy(
                        name = "더미 사용자",
                        birthDay = "1990-01-01",
                        isMale = true,
                        mySelfSentenceId = 0,
                        thumbnailUrl = ""
                    )
                    setMyProfileUseCase(myProfile = dummyProfile)
                    VerificationType.GotoMainScreen
                } else {
                    VerificationType.GotoAboutMeScreen
                }
            }
        }

        return VerificationType.Error
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if (showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false, code = "")
        }
    }
}
