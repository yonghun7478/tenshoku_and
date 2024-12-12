package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.CalculateAgeUseCase
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.GetTagByTagIdUseCase
import com.example.tokitoki.ui.constants.AboutMeMyProfileAction
import com.example.tokitoki.ui.converter.MyProfileUiConverter
import com.example.tokitoki.ui.state.AboutMeMyProfileEvent
import com.example.tokitoki.ui.state.AboutMeMyProfileState
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
class AboutMeMyProfileViewModel
@Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getMyTagUseCase: GetMyTagUseCase,
    private val getTagByTagIdUseCase: GetTagByTagIdUseCase,
    private val calculateAgeUseCase: CalculateAgeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeMyProfileState())
    val uiState: StateFlow<AboutMeMyProfileState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeMyProfileEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
        val myProfile = getMyProfileUseCase()
        val age = calculateAgeUseCase(myProfile.birthDay).getOrNull() ?: ""
        val myTag = getMyTagUseCase()
        val tag = getTagByTagIdUseCase(myTag.map { it.tagId })

        val myProfileItem = MyProfileUiConverter.domainToUi(myProfile, age, tag)

        _uiState.update { currentState ->
            currentState.copy(
                myProfileItem = myProfileItem,
            )
        }
    }

    fun aboutMeMyProfileAction(action: AboutMeMyProfileAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeMyProfileEvent.ACTION(action))
        }
    }

    suspend fun getBirthDay(): String {
        val myProfile = getMyProfileUseCase()
        return myProfile.birthDay
    }
}