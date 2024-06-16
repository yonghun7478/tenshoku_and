package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tenshoku_and.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: UserUseCase
) : ViewModel() {

}