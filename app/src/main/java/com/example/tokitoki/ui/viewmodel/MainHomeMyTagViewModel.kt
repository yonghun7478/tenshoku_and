package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainHomeMyTagViewModel @Inject constructor(
): ViewModel() {
    private val _uiState = MutableStateFlow(MainHomeMyTagUiState())
    val uiState: StateFlow<MainHomeMyTagUiState> = _uiState

    fun initialize() {
        // 초기 데이터 설정
    }

    fun onSearchTextChanged(text: String) {
        // 검색 텍스트 변경 로직
    }
}