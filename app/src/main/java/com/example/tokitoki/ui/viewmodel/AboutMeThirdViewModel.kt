package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.constants.AboutMeThirdAction
import com.example.tokitoki.ui.state.AboutMeThirdEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutMeThirdViewModel @Inject constructor(
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<AboutMeThirdEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun aboutMeThirdAction(action: AboutMeThirdAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeThirdEvent.ACTION(action))
        }
    }
}