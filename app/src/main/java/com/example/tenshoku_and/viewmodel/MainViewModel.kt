package com.example.tenshoku_and.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenshoku_and.domain.model.User
import com.example.tenshoku_and.domain.usecase.GetUserFromApiUseCase
import com.example.tenshoku_and.domain.usecase.GetUserFromDbUseCase
import com.example.tenshoku_and.domain.usecase.SaveUserUseCase
import com.example.tenshoku_and.domain.usecase.SaveUsersUseCase
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

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserFromApiUseCase: GetUserFromApiUseCase,
    private val getUserFromDbUseCase: GetUserFromDbUseCase,
    private val saveUsersUseCase: SaveUsersUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    ) : ViewModel() {
    var buttonsData = listOf(
        ButtonData("select", ButtonType.SELECT),
        ButtonData("delete", ButtonType.DELETE),
        ButtonData("update", ButtonType.UPDATE),
        ButtonData("insert", ButtonType.INSERT),
        )

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Init)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

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
//                "delete" -> {
//                    saveUsersUseCase.invoke(emptyList())
//                    _uiState.value = UserUiState.Success(emptyList())
//                }
//                "update" -> {
//                    val users = getUserFromApiUseCase.invoke()
//                    saveUsersUseCase.invoke(users)
//                    _uiState.value = UserUiState.Success(users)
//                }
//                "insert" -> {
//                    val users = getUserFromApiUseCase.invoke()
//                    saveUsersUseCase.invoke(users)
//                    _uiState.value = UserUiState.Success(users)
//                }
                else -> {}
            }
        }
    }

    fun inputListener(id: Int, name:String) {
        viewModelScope.launch {
            val user = User(id = id, name = name)
            saveUserUseCase.invoke(user)
        }
    }
}