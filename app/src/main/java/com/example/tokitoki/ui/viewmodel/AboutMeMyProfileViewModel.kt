package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokitoki.domain.usecase.GetMyProfileUseCase
import com.example.tokitoki.domain.usecase.GetTagByTagIdUseCase
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.state.AboutMeMyProfileEvent
import com.example.tokitoki.ui.state.AboutMeMyProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AboutMeMyProfileViewModel
@Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getMyTagByTagIdUseCase: GetTagByTagIdUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeMyProfileState())
    val uiState: StateFlow<AboutMeMyProfileState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeMyProfileEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
//        val myProfileItem = MyProfileConverter.domainToItem(getMyProfileUseCase())

        _uiState.update { currentState ->
            currentState.copy(
                isInitialized = true,
                myProfileItem = MyProfileItem(),
            )
        }
    }
}