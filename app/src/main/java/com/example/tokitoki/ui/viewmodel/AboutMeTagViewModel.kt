package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetTagByCategoryIdUseCase
import com.example.tokitoki.ui.constants.AboutMeTagAction
import com.example.tokitoki.ui.model.TagItem
import com.example.tokitoki.ui.converter.CategoryUiConverter
import com.example.tokitoki.ui.converter.TagUiConverter
import com.example.tokitoki.ui.state.AboutMeTagEvent
import com.example.tokitoki.ui.state.AboutMeTagState
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
class AboutMeTagViewModel
@Inject constructor(
    private val isTest: Boolean,
    private val getTagByCategoryIdUseCase: GetTagByCategoryIdUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeTagState())
    val uiState: StateFlow<AboutMeTagState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeTagEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun getIsTest(): Boolean {
        return isTest
    }

    suspend fun init() {
        val domainCategories = getCategoriesUseCase()
        val uiCategories = domainCategories.map { CategoryUiConverter.domainToUi(it) }

        val tagsByCategory = mutableMapOf<String, List<TagItem>>()

        domainCategories.forEach { category ->
            val tags = getTagByCategoryIdUseCase(category.id)
                .map { TagUiConverter.domainToUi(it) }

            tagsByCategory[category.title] = tags
        }

        _uiState.update { currentState ->
            currentState.copy(
                categoryList = uiCategories,
                tagsByCategory = tagsByCategory
            )
        }
    }

    fun aboutMeTagAction(action: AboutMeTagAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeTagEvent.ACTION(action))
        }
    }

    fun updateShowDialogState(showDialog: Boolean) {
        _uiState.update {
            if (showDialog)
                it.copy(showDialog = true)
            else
                it.copy(showDialog = false)
        }
    }

    fun updateGridItem(categoryTitle: String, index: Int) {
        val updatedCategoryList =
            _uiState.value.tagsByCategory[categoryTitle]?.toMutableList() ?: return

        if (index in updatedCategoryList.indices) {
            val selectedItem = updatedCategoryList[index].copy(
                showBadge = !updatedCategoryList[index].showBadge,
            )

            updatedCategoryList[index] = selectedItem
            val updatedMap = _uiState.value.tagsByCategory.toMutableMap().apply {
                put(categoryTitle, updatedCategoryList)
            }

            _uiState.update {
                it.copy(tagsByCategory = updatedMap)
            }
        }
    }

    fun checkTags(): Boolean {
        val selectedTags = _uiState.value.tagsByCategory.values.flatten()
            .filter { it.showBadge }

        return selectedTags.size > 2
    }
}