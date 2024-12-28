package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.ui.constants.AboutMeBirthdayAction
import com.example.tokitoki.ui.state.AboutMeBirthdayEvent
import com.example.tokitoki.ui.state.AboutMeBirthdayState
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
class AboutMeBirthdayViewModel @Inject constructor(
    private val setMyProfileUseCase: SetMyProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeBirthdayState())
    val uiState: StateFlow<AboutMeBirthdayState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeBirthdayEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init(birthDay: String) {
        _uiState.value =
            AboutMeBirthdayState(birthday = birthDay, isEditMode = birthDay.isNotEmpty())
    }

    fun onBirthdayChanged(birthday: String) {
        _uiState.update { it.copy(birthday = birthday) }
    }

    fun aboutMeBirthdayAction(action: AboutMeBirthdayAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeBirthdayEvent.ACTION(action))
        }
    }

    suspend fun checkBirthday(): Boolean {
        if (_uiState.value.birthday.length == 8) {
            val profile = getMyProfileUseCase()
            setMyProfileUseCase(profile.copy(birthDay = _uiState.value.birthday))
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