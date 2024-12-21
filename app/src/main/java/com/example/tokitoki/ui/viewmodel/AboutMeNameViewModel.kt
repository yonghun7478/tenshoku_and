package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.ui.constants.AboutMeNameAction
import com.example.tokitoki.ui.state.AboutMeNameEvent
import com.example.tokitoki.ui.state.AboutMeNameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutMeNameViewModel @Inject constructor(
    private val setMyProfileUseCase: SetMyProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeNameState())
    val uiState: StateFlow<AboutMeNameState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeNameEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init(name: String) {
        _uiState.value = AboutMeNameState(name = name, isEditMode = name.isNotEmpty())
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun aboutMeNameAction(action: AboutMeNameAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeNameEvent.ACTION(action))
        }
    }

    suspend fun checkName(): Boolean {
        if (_uiState.value.name.isNotEmpty()) {
            val profile = getMyProfileUseCase()
            setMyProfileUseCase(profile.copy(name = _uiState.value.name))

            return true
        }

        return false
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if (showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }
}