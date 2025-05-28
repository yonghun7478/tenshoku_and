package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetTagCategoriesUseCase
import com.example.tokitoki.domain.usecase.GetTagsByCategoryUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByQueryUseCase
import com.example.tokitoki.ui.state.TagCategoryUiState
import com.example.tokitoki.ui.state.TagResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TagSearchViewModel @Inject constructor(
    private val getTagCategoriesUseCase: GetTagCategoriesUseCase,
    private val getTagsByCategoryUseCase: GetTagsByCategoryUseCase,
    private val getTagsByQueryUseCase: GetTagsByQueryUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<TagCategoryUiState>>(emptyList())
    val categories: StateFlow<List<TagCategoryUiState>> = _categories.asStateFlow()

    private val _tags = MutableStateFlow<List<TagResultUiState>>(emptyList())
    val tags: StateFlow<List<TagResultUiState>> = _tags.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    init {
        loadCategories()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _tags.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = getTagsByQueryUseCase(query)
                result.onSuccess { list ->
                    _tags.value = list.map { domain ->
                        TagResultUiState(
                            id = domain.id,
                            name = domain.name,
                            imageUrl = domain.imageUrl,
                            subscriberCount = domain.subscriberCount
                        )
                    }
                }.onFailure { e ->
                    _errorMessage.value = e.message
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onCategoryClick(category: TagCategoryUiState) {
        _selectedCategoryId.value = category.id
        loadTagsByCategory(category.id)
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = getTagCategoriesUseCase()
                // TODO: 도메인 모델 -> UiState 매핑 필요
                _categories.value = result.getOrElse { emptyList() }.map { domain ->
                    TagCategoryUiState(
                        id = domain.id,
                        name = domain.name,
                        imageUrl = domain.imageUrl
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTagsByCategory(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = getTagsByCategoryUseCase(categoryId)
                // TODO: 도메인 모델 -> UiState 매핑 필요
                _tags.value = result.getOrElse { emptyList() }.map { domain ->
                    TagResultUiState(
                        id = domain.id,
                        name = domain.name,
                        imageUrl = domain.imageUrl,
                        subscriberCount = domain.subscriberCount
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 