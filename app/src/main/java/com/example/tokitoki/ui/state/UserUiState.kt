package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.UserUiModel

sealed class UserUiState {
    object Init : UserUiState()
    object Loading : UserUiState()
    data class Success(val users: List<UserUiModel>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}