package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.usecase.GetMyTagsByTypeUseCase
import com.example.tokitoki.ui.state.MyTagListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTagListViewModel @Inject constructor(
    private val getMyTagsByTypeUseCase: GetMyTagsByTypeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyTagListUiState())
    val uiState: StateFlow<MyTagListUiState> = _uiState.asStateFlow()

    init {
        loadTags(TagType.HOBBY)
    }

    fun onTabSelected(tagType: TagType) {
        if (_uiState.value.selectedTab == tagType) return
        _uiState.update { it.copy(selectedTab = tagType, isLoading = true, error = null) }
        if (_uiState.value.tagLists.containsKey(tagType)) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }
        loadTags(tagType)
    }

    private fun loadTags(tagType: TagType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // TODO: userId를 어떻게 가져올지 결정 필요
            val userId = "current_user_id" // 임시로 하드코딩
            val result = getMyTagsByTypeUseCase(userId, tagType)
            result.onSuccess { tags ->
                val newMap = _uiState.value.tagLists.toMutableMap()
                newMap[tagType] = tags
                _uiState.update { it.copy(tagLists = newMap, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e?.message) }
            }
        }
    }
} 