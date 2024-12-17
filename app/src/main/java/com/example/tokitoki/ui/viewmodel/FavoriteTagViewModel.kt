package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetMyTagUseCase
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCase
import com.example.tokitoki.ui.constants.FavoriteTagAction
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

@HiltViewModel
class FavoriteTagViewModel @Inject constructor(
    private val getMyTagUseCase: GetMyTagUseCase,
    private val getTagByCategoryIdUseCase: GetTagByCategoryIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteTagUiState())
    val uiState: StateFlow<FavoriteTagUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<FavoriteTagEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun loadTagsByCategory() {

        val categories = getCategoriesUseCase()
        val myTags = getMyTagUseCase()
        val tagsByCategory = mutableMapOf<String, List<TagItem>>()

        for (category in categories) {
            val categoryTags = getTagByCategoryIdUseCase(category.id)
            val filteredTags = categoryTags.filter { tag ->
                myTags.any { myTag -> myTag.tagId == tag.id }
            }

            tagsByCategory[category.title] =
                filteredTags.map { TagUiConverter.domainToUi(it) }
        }

        _uiState.value = _uiState.value.copy(tagsByCategory = tagsByCategory)

    }

    fun onCategoryTabClicked(categoryId: Int) {
        viewModelScope.launch {
            _uiEvent.emit(FavoriteTagEvent.ACTION(FavoriteTagAction.CategoryTabClicked(categoryId)))
        }
    }

    fun onBackBtnClicked() {
        viewModelScope.launch {
            _uiEvent.emit(FavoriteTagEvent.ACTION(FavoriteTagAction.BackBtnClicked))
        }
    }
}