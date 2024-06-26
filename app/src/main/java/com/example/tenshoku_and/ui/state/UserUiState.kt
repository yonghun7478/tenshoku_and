package com.example.tenshoku_and.ui.state

import com.example.tenshoku_and.domain.model.User

sealed class UserUiState {
    object Init : UserUiState()
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}