package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.usecase.DislikePickupUserUseCase
import com.example.tokitoki.domain.usecase.FetchPickupUsersUseCase
import com.example.tokitoki.domain.usecase.LikePickupUserUseCase
import com.example.tokitoki.ui.converter.PickupUserMapper
import com.example.tokitoki.ui.state.PickupUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickupUserViewModel @Inject constructor(
    private val fetchPickupUsersUseCase: FetchPickupUsersUseCase,
    private val likePickupUserUseCase: LikePickupUserUseCase,
    private val dislikePickupUserUseCase: DislikePickupUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PickupUserUiState())
    val uiState: StateFlow<PickupUserUiState> = _uiState

    init {
        loadPickupUsers()
    }

    fun loadPickupUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = fetchPickupUsersUseCase()) {
                is ResultWrapper.Success -> {
                    _uiState.value = _uiState.value.copy(users = result.data.map { PickupUserMapper.toPresentation(it) })
                }
                is ResultWrapper.Error -> {
                    _uiState.value = _uiState.value.copy(errorMessage = "Failed to load users")
                }
            }
        }
    }

    fun likePickupUser(pickupUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            likePickupUserUseCase(pickupUserId)
            removePickupUser(pickupUserId)
        }
    }

    fun dislikePickupUser(pickupUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dislikePickupUserUseCase(pickupUserId)
            removePickupUser(pickupUserId)
        }
    }

    private fun removePickupUser(pickupUserId: String) {
        _uiState.value = _uiState.value.copy(
            users = _uiState.value.users.filterNot { it.id == pickupUserId }
        )
    }
}