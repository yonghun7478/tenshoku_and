package com.example.tokitoki.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.screen.CardDirection
import com.example.tokitoki.ui.screen.CardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainHomePickupViewModel @Inject constructor() : ViewModel() {

    // UI 상태 (StateFlow 사용)
    private val _uiState = MutableStateFlow(CardUiState())
    val uiState: StateFlow<CardUiState> = _uiState.asStateFlow()

    init {
        // 초기 카드 데이터 설정
        _uiState.update { currentState ->
            currentState.copy(
                cardStates = mutableStateListOf(
                    CardState(Color.Cyan),
                    CardState(Color.Magenta),
                    CardState(Color.Yellow),
                    CardState(Color.Gray)
                )
            )
        }
    }

    // 카드 제거 처리
    fun removeCard(cardState: CardState) {
        Log.d("CYHH", "cardState: $cardState")
        _uiState.update { currentState ->
            currentState.copy(cardStates = currentState.cardStates.toMutableList().apply {
                remove(cardState)
            })
        }
    }

    // 자동 제거 트리거 (좋아요 / 싫어요 버튼 클릭 시)
    fun triggerAutoRemove(direction: CardDirection) {
        _uiState.update { currentState ->
            // 리스트가 비어있지 않다면 첫 번째 카드의 autoRemoveDirection 업데이트
            if (currentState.cardStates.isNotEmpty()) {
                currentState.cardStates.first().cardDirection.value = direction
            }
            currentState
        }
    }
}

data class CardUiState(
    val cardStates: MutableList<CardState> = mutableStateListOf()
)