package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetTokensUseCase
import com.example.tokitoki.ui.state.SignInEvent
import com.example.tokitoki.ui.constants.SignInAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<SignInEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun signInAction(action: SignInAction) {
        viewModelScope.launch {
            _uiEvent.emit(SignInEvent.ACTION(action))
        }
    }

    suspend fun checkToken(): Boolean {
        val tokens = getTokensUseCase()
        return tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()
    }
}