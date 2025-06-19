package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetTokensUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTokensUseCase: GetTokensUseCase
) : ViewModel() {

    private val _isTokenExist = MutableStateFlow(false)
    val isTokenExist: StateFlow<Boolean> = _isTokenExist.asStateFlow()

    private val _isCheckComplete = MutableStateFlow(false)
    val isCheckComplete: StateFlow<Boolean> = _isCheckComplete.asStateFlow()

    fun checkToken() {
        viewModelScope.launch {
            val tokens = getTokensUseCase()
            _isTokenExist.value = tokens.accessToken.isNotEmpty() && tokens.refreshToken.isNotEmpty()
            _isCheckComplete.value = true
        }
    }
} 