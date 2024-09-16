package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeBirthdayState())
    val uiState: StateFlow<AboutMeBirthdayState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeBirthdayEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init() {
        _uiState.value = AboutMeBirthdayState()
    }

    fun onBirthdayChanged(birthday: String) {
        _uiState.update { it.copy(birthday = birthday) }
    }

    fun aboutMeGenderAction(action: AboutMeBirthdayAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeBirthdayEvent.ACTION(action))
        }
    }

    fun checkGender() = _uiState.value.birthday.isNotEmpty()

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if(showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }
}