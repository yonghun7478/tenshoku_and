package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import com.example.tenshoku_and.domain.usecase.SaveUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserFromApiUseCase: GetUserFromApiUseCase,
    private val saveUsersUseCase: SaveUsersUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            getUserFromApiUseCase().collect { users ->
                saveUsersUseCase.invoke(users)
            }
        }
    }
}