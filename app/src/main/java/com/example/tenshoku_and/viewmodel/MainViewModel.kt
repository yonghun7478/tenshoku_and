package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.usecase.DeleteUserUseCase
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import com.example.tenshoku_and.domain.usecase.GetUserFromDbUseCase
import com.example.tenshoku_and.domain.usecase.GetUserNameUseCase
import com.example.tenshoku_and.domain.usecase.SaveUserUseCase
import com.example.tenshoku_and.domain.usecase.SaveUsersUseCase
import com.example.tenshoku_and.domain.usecase.SetUserNameUseCase
import com.example.tenshoku_and.domain.usecase.UpdateUserUseCase
import com.example.tenshoku_and.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tenshoku_and.domain.util.Resource
import com.example.tenshoku_and.ui.data.ButtonData
import com.example.tenshoku_and.ui.data.ButtonType
import com.example.tenshoku_and.ui.converter.UserUiConverter
import com.example.tenshoku_and.ui.state.UserUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserFromApiUseCase: GetUserFromApiUseCase,
    private val getUserFromDbUseCase: GetUserFromDbUseCase,
    private val saveUsersUseCase: SaveUsersUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val setUserNameUseCase: SetUserNameUseCase

) : ViewModel() {
    val buttonsData = listOf(
        ButtonData("select", ButtonType.SELECT),
        ButtonData("delete", ButtonType.DELETE),
        ButtonData("update", ButtonType.UPDATE),
        ButtonData("insert", ButtonType.INSERT),
        ButtonData("next", ButtonType.NEXT),
    )

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Init)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UserUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun menuListener(button: ButtonData) {
        viewModelScope.launch {
            when (button.type) {
                ButtonType.SELECT -> {
                    _uiState.value = UserUiState.Loading

                    val users = getUserFromApiUseCase.invoke()

                    users.collect { apiResult ->
                        if (apiResult is Resource.Success) {
                            saveUsersUseCase.invoke(apiResult.data)
                            getUserFromDbUseCase.invoke().collect { dbResult ->
                                if (dbResult is Resource.Success) {
                                    _uiState.value = UserUiState.Success(dbResult.data.map {
                                        UserUiConverter.domainUserToUser(it)
                                    })
                                }
                            }
                        }
                    }
                }
                ButtonType.NEXT -> {
                    viewModelScope.launch {
                        _uiEvent.emit(UserUiEvent.NextScreen)
                    }
                }

                else -> {}
            }
        }
    }

    fun inputListener(id: Int, name: String) {
        viewModelScope.launch {
            val user = User(id = id, name = name)
            saveUserUseCase.invoke(user)
        }
    }

    fun onDeleteClick(id: Int) {
        viewModelScope.launch {
            deleteUserUseCase.invoke(id)
        }
    }

    fun onEditClick(id: Int, name: String) {
        viewModelScope.launch {
            updateUserUseCase.invoke(id, name)
            setUserNameUseCase.invoke(name)
            println("name : ${getUserNameUseCase.invoke()}")
        }
    }
}