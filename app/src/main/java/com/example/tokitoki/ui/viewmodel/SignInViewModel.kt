package com.example.tokitoki.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.GetTokensUseCase
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
    private val googleSignInManager: GoogleSignInManager
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignInEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun signInAction(action: SignInAction) {
        viewModelScope.launch {
            _uiEvent.emit(SignInEvent.ACTION(action))
        }
    }

    suspend fun signInGoogle(activityContext: Context) {
        val result = googleSignInManager.requestLogin(activityContext)

        if(result is ResultWrapper.Success) {

        } else {

        }
    }

    suspend fun checkToken(): Boolean {
        val tokens = getTokensUseCase()
        return tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()
    }
}