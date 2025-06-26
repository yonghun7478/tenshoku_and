package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetAllMySelfSentenceUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.SetMyProfileUseCase
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
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val setMyProfileUseCase: SetMyProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeProfInputState())
    val uiState: StateFlow<AboutMeProfInputState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeProfInputEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init(
        selfSentenceId: Int
    ) {
        val sentenceList =
            getAllMySelfSentenceUseCase().map { MySelfSentenceUiConverter.domainToUi(it) }

        val offset = sentenceList.indexOfFirst { it.id == selfSentenceId }

        _uiState.update {
            it.copy(
                myselfSentenceList = sentenceList,
                offset = offset,
                isEditMode = offset != -1
            )
        }
    }

    suspend fun saveMySelfSentence(sentenceId: Int) {
        val curProfile = getMyProfileUseCase()
        if(!_uiState.value.isEditMode)
            setMyProfileUseCase(curProfile.copy(mySelfSentenceId = sentenceId))
    }

    fun aboutMeProfInputAction(action: AboutMeProfInputAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeProfInputEvent.ACTION(action))
        }
    }
}