package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.ClearMyTagUseCase
import com.example.tokitoki.domain.usecase.SetMyTagUseCase
import com.example.tokitoki.ui.constants.AboutMeTagAction
import com.example.tokitoki.ui.converter.TagTypeUiConverter
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
        isFromMyPage: Boolean = false,
    ) {
        // Step 1: 도메인 데이터를 가져오기 (TagType)
        val tagTypes = getTagTypeListUseCase()
        val uiTagTypes = tagTypes.map { TagTypeUiConverter.tagTypeToUi(it) }

        // Step 2: 기본 tagsByTagType 생성
        val tagsByTagType = mutableMapOf<String, List<com.example.tokitoki.ui.model.TagItem>>()
        tagTypes.forEach { tagType ->
            val tags = getTagsByTypeUseCase(tagType)
                .getOrElse { emptyList() }
                .map { TagUiConverter.domainToUi(it) }
            tagsByTagType[tagType.value] = tags
        }

        // Step 3: tagIds가 비어 있지 않은 경우에만 showBadge 업데이트
        val finalTagsByTagType = if (tagIds.isNotEmpty()) {
            tagsByTagType.mapValues { (_, tags) ->
                tags.map { tag ->
                    val isFavorite = tagIds.any { it.tagId == tag.id }
                    tag.copy(showBadge = isFavorite)
                }
            }
        } else {
            tagsByTagType
        }

        // Step 4: UI 상태 업데이트
        _uiState.update { currentState ->
            currentState.copy(
                tagTypeList = uiTagTypes,
                tagsByTagType = finalTagsByTagType,
                isEditMode = isFromMyPage || tagIds.isNotEmpty()
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

    fun updateGridItem(tagTypeTitle: String, index: Int) {
        val updatedTagTypeList =
            _uiState.value.tagsByTagType[tagTypeTitle]?.toMutableList() ?: return

        if (index in updatedTagTypeList.indices) {
            val selectedItem = updatedTagTypeList[index].copy(
                showBadge = !updatedTagTypeList[index].showBadge,
            )

            updatedTagTypeList[index] = selectedItem
            val updatedMap = _uiState.value.tagsByTagType.toMutableMap().apply {
                put(tagTypeTitle, updatedTagTypeList)
            }

            _uiState.update {
                it.copy(tagsByTagType = updatedMap)
            }
        }
    }

    suspend fun checkTags(): Boolean {
        val selectedTags = _uiState.value.tagsByTagType.values.flatten()
            .filter { it.showBadge }

        if (selectedTags.size > 2) {
            // 1. 필터링: showBadge가 true인 태그만 추출
            val filteredTags = _uiState.value.tagsByTagType
                .values
                .flatten()
                .filter { it.showBadge }

            val domainMyTags = filteredTags.map { TagUiConverter.uiToDomain(it) }

            if(!uiState.value.isEditMode) {
                clearMyTagUseCase()
                setMyTagUseCase(domainMyTags)
            }

            return true
        }

        return false
    }
}