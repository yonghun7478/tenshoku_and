package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import com.example.tenshoku_and.domain.usecase.SaveUsersUseCase
import com.example.tenshoku_and.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tenshoku_and.domain.util.Result

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserFromApiUseCase: GetUserFromApiUseCase,
    private val saveUsersUseCase: SaveUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserFromApiUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {

                    }

                    is Result.Error -> {
                    }
                }
//                saveUsersUseCase.invoke(users)
//                _uiState.value = UserUiState.Success(users)
            }
        }
    }
}