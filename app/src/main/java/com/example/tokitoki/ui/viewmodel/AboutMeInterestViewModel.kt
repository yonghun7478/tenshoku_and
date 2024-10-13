package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetUserInterestsUseCase
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.model.UserInterestItem
import com.example.tokitoki.ui.converter.CategoryUiConverter
import com.example.tokitoki.ui.converter.UserInterestUiConverter
import com.example.tokitoki.ui.state.AboutMeInterestEvent
import com.example.tokitoki.ui.state.AboutMeInterestState
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
class AboutMeInterestViewModel
@Inject constructor(
    private val getUserInterestsUseCase: GetUserInterestsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutMeInterestState())
    val uiState: StateFlow<AboutMeInterestState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeInterestEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
        val domainCategories = getCategoriesUseCase()
        val uiCategories = domainCategories.map { CategoryUiConverter.domainToUi(it) }

        val userInterestsByCategory = mutableMapOf<String, List<UserInterestItem>>()
        domainCategories.forEach { category ->
            val userInterests = getUserInterestsUseCase(category.id)
                .map { UserInterestUiConverter.domainToUi(it) }

            userInterestsByCategory[category.title] = userInterests
        }

        _uiState.update { currentState ->
            currentState.copy(
                isInitialized = true,
                categoryList = uiCategories,
                userInterestsByCategory = userInterestsByCategory
            )
        }
    }

    fun aboutMeInterestAction(action: AboutMeInterestAction) {
        viewModelScope.launch {
            _uiEvent.emit(AboutMeInterestEvent.ACTION(action))
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
            _uiState.value.userInterestsByCategory[categoryTitle]?.toMutableList() ?: return

        if (index in updatedCategoryList.indices) {
            val selectedItem = updatedCategoryList[index].copy(
                showBadge = !updatedCategoryList[index].showBadge,
            )

            updatedCategoryList[index] = selectedItem
            val updatedMap = _uiState.value.userInterestsByCategory.toMutableMap().apply {
                put(categoryTitle, updatedCategoryList)
            }

            _uiState.update {
                it.copy(userInterestsByCategory = updatedMap)
            }
        }
    }
}