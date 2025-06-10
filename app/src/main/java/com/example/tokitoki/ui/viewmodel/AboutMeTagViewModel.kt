package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.domain.usecase.SetMyTagUseCase
import com.example.tokitoki.ui.constants.AboutMeTagAction
import com.example.tokitoki.ui.converter.CategoryUiConverter
import com.example.tokitoki.ui.converter.TagUiConverter
import com.example.tokitoki.ui.model.MyTagItem
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
import com.example.tokitoki.domain.usecase.tag.GetTagTypeListUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByTypeUseCase

@HiltViewModel
class AboutMeTagViewModel
@Inject constructor(
    private val isTest: Boolean,
    private val getTagTypeListUseCase: GetTagTypeListUseCase,
    private val getTagsByTypeUseCase: GetTagsByTypeUseCase,
    private val setMyTagUseCase: SetMyTagUseCase,
    private val clearMyTagUseCase: ClearMyTagUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeTagState())
    val uiState: StateFlow<AboutMeTagState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeTagEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun getIsTest(): Boolean {
        return isTest
    }

    suspend fun init(
        tagIds: List<MyTagItem> = listOf(),
    ) {
        // Step 1: 도메인 데이터를 가져오기 (TagType)
        val tagTypes = getTagTypeListUseCase()
        val uiCategories = tagTypes.map { CategoryUiConverter.tagTypeToUi(it) }

        // Step 2: 기본 tagsByCategory 생성
        val tagsByCategory = mutableMapOf<String, List<com.example.tokitoki.ui.model.TagItem>>()
        tagTypes.forEach { tagType ->
            val tags = getTagsByTypeUseCase(tagType)
                .getOrElse { emptyList() }
                .map { TagUiConverter.domainToUi(it) }
            tagsByCategory[tagType.value] = tags
        }

        // Step 3: tagIds가 비어 있지 않은 경우에만 showBadge 업데이트
        val finalTagsByCategory = if (tagIds.isNotEmpty()) {
            tagsByCategory.mapValues { (_, tags) ->
                tags.map { tag ->
                    val isFavorite = tagIds.any { it.tagId == tag.id }
                    tag.copy(showBadge = isFavorite)
                }
            }
        } else {
            tagsByCategory
        }

        // Step 4: UI 상태 업데이트
        _uiState.update { currentState ->
            currentState.copy(
                categoryList = uiCategories,
                tagsByCategory = finalTagsByCategory,
                isEditMode = tagIds.isNotEmpty()
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

    suspend fun checkTags(): Boolean {
        val selectedTags = _uiState.value.tagsByCategory.values.flatten()
            .filter { it.showBadge }

        if (selectedTags.size > 2) {
            // 1. 필터링: showBadge가 true인 태그만 추출
            val filteredTags = _uiState.value.tagsByCategory
                .values
                .flatten()
                .filter { it.showBadge }

            val domainMyTags = filteredTags.map { TagUiConverter.uiToDomain(it) }

            clearMyTagUseCase()
            val result = setMyTagUseCase(domainMyTags)

            if (result.isSuccess) {
                return true
            } else {
                return false
            }
        }

        return false
    }
}