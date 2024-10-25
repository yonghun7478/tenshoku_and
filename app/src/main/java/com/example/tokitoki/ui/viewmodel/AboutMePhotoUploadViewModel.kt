package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMePhotoUploadAction
import com.example.tokitoki.ui.state.AboutMePhotoUploadEvent
import com.example.tokitoki.ui.state.AboutMePhotoUploadState
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
class AboutMePhotoUploadViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMePhotoUploadState())
    val uiState: StateFlow<AboutMePhotoUploadState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMePhotoUploadEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init() {
        _uiState.value = AboutMePhotoUploadState()
    }

    fun aboutMePhotoUploadAction(action: AboutMePhotoUploadAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMePhotoUploadEvent.ACTION(action))
        }
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if(showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }

    fun updateShowBottomDialogState(showBottomDialog: Boolean) {
        _uiState.update {
            if(showBottomDialog)
                it.copy(showBottomDialog = true)
            else
                it.copy(showBottomDialog = false)
        }
    }
}