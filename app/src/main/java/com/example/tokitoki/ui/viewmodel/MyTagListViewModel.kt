package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.usecase.GetMyTagsByTagTypeUseCase
import com.example.tokitoki.ui.state.MyTagListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.usecase.tag.GetTagsUseCase

@HiltViewModel
class MyTagListViewModel @Inject constructor(
    private val getMyTagsByTagTypeUseCase: GetMyTagsByTagTypeUseCase,
    private val getTagsUseCase: GetTagsUseCase
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
            val result = getMyTagsByTagTypeUseCase(tagType)
            result.onSuccess { myTags ->
                val myTagIds = myTags.map { it.tagId }
                val mainHomeTags = getTagsUseCase(myTagIds)
                val newMap = _uiState.value.tagLists.toMutableMap()

                newMap[tagType] = mainHomeTags
                _uiState.update { it.copy(tagLists = newMap, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e?.message) }
            }
        }
    }
} 