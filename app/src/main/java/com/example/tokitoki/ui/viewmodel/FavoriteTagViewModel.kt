package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.ui.constants.FavoriteTagAction
import com.example.tokitoki.ui.converter.CategoryUiConverter
import com.example.tokitoki.ui.converter.TagUiConverter
import com.example.tokitoki.ui.model.TagItem
import com.example.tokitoki.ui.state.FavoriteTagEvent
import com.example.tokitoki.ui.state.FavoriteTagUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tokitoki.domain.usecase.tag.GetTagTypeListUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByTypeUseCase

@HiltViewModel
class FavoriteTagViewModel @Inject constructor(
    private val getMyTagUseCase: GetMyTagUseCase,
    private val getTagTypeListUseCase: GetTagTypeListUseCase,
    private val getTagsByTypeUseCase: GetTagsByTypeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteTagUiState())
    val uiState: StateFlow<FavoriteTagUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<FavoriteTagEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun loadTagsByCategory() {

        val tagTypes = getTagTypeListUseCase()
        val myTags = getMyTagUseCase()
        val tagsByCategory = mutableMapOf<String, List<TagItem>>()

        for (tagType in tagTypes) {
            val categoryTagsResult = getTagsByTypeUseCase(tagType)
            val categoryTags = categoryTagsResult.getOrNull() ?: emptyList()

            val filteredTags = categoryTags.filter {
                myTags.any { myTag -> myTag.tagId.toString() == it.id }
            }

            tagsByCategory[tagType.value] =
                filteredTags.map { TagUiConverter.domainToUi(it) }
        }

        _uiState.value = _uiState.value.copy(
            tagsByCategory = tagsByCategory,
            categoryList = tagTypes.map { CategoryUiConverter.tagTypeToUi(it) }
        )

    }

    fun onCategoryTabClicked(index: Int) {
        viewModelScope.launch {
            _uiEvent.emit(FavoriteTagEvent.ACTION(FavoriteTagAction.CategoryTabClicked(index)))
        }
    }

    fun onBackBtnClicked() {
        viewModelScope.launch {
            _uiEvent.emit(FavoriteTagEvent.ACTION(FavoriteTagAction.BackBtnClicked))
        }
    }
}