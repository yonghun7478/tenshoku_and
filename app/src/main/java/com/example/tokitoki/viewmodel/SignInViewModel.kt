package com.example.tokitoki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.usecase.DeleteUserUseCase
import com.example.tokitoki.domain.usecase.GetUserFromApiUseCase
import com.example.tokitoki.domain.usecase.GetUserFromDbUseCase
import com.example.tokitoki.domain.usecase.GetUserNameUseCase
import com.example.tokitoki.domain.usecase.SaveUserUseCase
import com.example.tokitoki.domain.usecase.SaveUsersUseCase
import com.example.tokitoki.domain.usecase.SetUserNameUseCase
import com.example.tokitoki.domain.usecase.UpdateUserUseCase
import com.example.tokitoki.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.util.Resource
import com.example.tokitoki.ui.data.ButtonData
import com.example.tokitoki.ui.data.ButtonType
import com.example.tokitoki.ui.converter.UserUiConverter
import com.example.tokitoki.ui.state.UserUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {
}