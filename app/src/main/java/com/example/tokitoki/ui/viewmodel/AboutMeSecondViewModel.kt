package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMeSecondAction
import com.example.tokitoki.ui.state.AboutMeSecondEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutMeSecondViewModel @Inject constructor(
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AboutMeSecondEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun aboutMeSecondAction(action: AboutMeSecondAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeSecondEvent.ACTION(action))
        }
    }
}