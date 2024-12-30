package com.example.tokitoki.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetTokensUseCase
import com.example.tokitoki.domain.usecase.SaveTokensUseCase
import com.example.tokitoki.domain.usecase.VerifyGoogleTokenUseCase
import com.example.tokitoki.ui.state.SignInEvent
import com.example.tokitoki.ui.constants.SignInAction
import com.example.tokitoki.utils.GoogleSignInManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase,
    private val googleSignInManager: GoogleSignInManager,
    private val verifyGoogleTokenUseCase: VerifyGoogleTokenUseCase,
    private val saveTokenUseCase: SaveTokensUseCase

) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignInEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun signInAction(action: SignInAction) {
        viewModelScope.launch {
            _uiEvent.emit(SignInEvent.ACTION(action))
        }
    }

    suspend fun signInGoogle(activityContext: Context): Boolean {
        val requestLoginResult = googleSignInManager.requestLogin(activityContext)

        if (requestLoginResult is ResultWrapper.Success) {
            val response = verifyGoogleTokenUseCase(
                id = requestLoginResult.data.id,
                idToken = requestLoginResult.data.token ?: ""
            )

            if(response is ResultWrapper.Success) {
                saveTokenUseCase(
                    token = response.data.accessToken,
                    refreshToken = response.data.refreshToken
                )

                return true
            }
        }

        return false
    }

    suspend fun checkToken(): Boolean {
        val tokens = getTokensUseCase()
        return tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()
    }
}