package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
import com.example.tokitoki.ui.constants.AboutMeGenderAction
import com.example.tokitoki.ui.state.AboutMeGenderEvent
import com.example.tokitoki.ui.state.AboutMeGenderState
import com.example.tokitoki.ui.state.Gender
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
class AboutMeGenderViewModel @Inject constructor(
    private val setMyProfileUseCase: SetMyProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeGenderState())
    val uiState: StateFlow<AboutMeGenderState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeGenderEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init() {
        _uiState.value = AboutMeGenderState()
    }

    fun onGenderSelected(gender: Gender) {
        _uiState.update { it.copy(selectedGender = gender) }
    }

    fun aboutMeGenderAction(action: AboutMeGenderAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeGenderEvent.ACTION(action))
        }
    }

    suspend fun checkGender(): Boolean {
        if (_uiState.value.selectedGender != Gender.NONE) {
            val profile = getMyProfileUseCase()
            setMyProfileUseCase(profile.copy(isMale = _uiState.value.selectedGender == Gender.MALE))
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