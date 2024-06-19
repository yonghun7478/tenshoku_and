package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserFromApiUseCase: GetUserFromApiUseCase
) : ViewModel() {
    init {
        viewModelScope.launch {
            getUserFromApiUseCase().collect { users ->
                System.out.println("called!! $users")
            }
        }
    }
}