package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.SetMySelfSentenceUseCase
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
    private val getAllMySelfSentenceUseCase: GetAllMySelfSentenceUseCase,
    private val setMySelfSentenceUseCase: SetMySelfSentenceUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeProfInputState())
    val uiState: StateFlow<AboutMeProfInputState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeProfInputEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
        val sentenceList = getAllMySelfSentenceUseCase().map { MySelfSentenceUiConverter.domainToUi(it) }

        _uiState.update {
            it.copy(
                myselfSentenceList = sentenceList,
            )
        }
    }

    suspend fun saveMySelfSentence(sentence: String) {
        setMySelfSentenceUseCase(sentence)
    }

    fun aboutMeProfInputAction(action: AboutMeProfInputAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeProfInputEvent.ACTION(action))
        }
    }
}