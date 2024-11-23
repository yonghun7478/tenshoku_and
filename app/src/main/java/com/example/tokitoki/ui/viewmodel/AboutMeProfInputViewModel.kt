package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMySelfSentenceUseCase
import com.example.tokitoki.ui.constants.AboutMeProfInputAction
import com.example.tokitoki.ui.converter.MySelfSentenceUiConverter
import com.example.tokitoki.ui.state.AboutMeProfInputEvent
import com.example.tokitoki.ui.state.AboutMeProfInputState
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
class AboutMeProfInputViewModel
@Inject constructor(
    private val getMySelfSentenceUseCase: GetMySelfSentenceUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeProfInputState())
    val uiState: StateFlow<AboutMeProfInputState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeProfInputEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
        val sentenceList = getMySelfSentenceUseCase().map { MySelfSentenceUiConverter.domainToUi(it) }

        _uiState.update {
            it.copy(
                isInitialized = true,
                myselfSentenceList = sentenceList,
            )
        }
    }

    fun aboutMeProfInputAction(action: AboutMeProfInputAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeProfInputEvent.ACTION(action))
        }
    }
}