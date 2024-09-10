package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMeAction
import com.example.tokitoki.ui.state.AboutMeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutMeViewModel @Inject constructor(
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AboutMeEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun aboutMeAction(action: AboutMeAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeEvent.ACTION(action))
        }
    }
}