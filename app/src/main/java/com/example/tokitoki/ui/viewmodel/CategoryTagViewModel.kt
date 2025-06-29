package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.GetTagsByCategoryUseCase
import com.example.tokitoki.domain.usecase.tag.GetTagsByQueryUseCase
import com.example.tokitoki.ui.state.CategoryTagResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryTagViewModel @Inject constructor(
    private val getTagsByCategoryUseCase: GetTagsByCategoryUseCase,
    private val getTagsByQueryUseCase: GetTagsByQueryUseCase
) : ViewModel() {

    private val _tags = MutableStateFlow<List<CategoryTagResultUiState>>(emptyList())
    val tags: StateFlow<List<CategoryTagResultUiState>> = _tags.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentCategoryId: String? = null

    fun initialize(categoryId: String) {
        currentCategoryId = categoryId
        loadTagsByCategory(categoryId)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            currentCategoryId?.let { loadTagsByCategory(it) }
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = getTagsByQueryUseCase(query)
                result.onSuccess { list ->
                    _tags.value = list.map { domain ->
                        CategoryTagResultUiState(
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

    private fun loadTagsByCategory(categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = getTagsByCategoryUseCase(categoryId)
                result.onSuccess { list ->
                    _tags.value = list.map { domain ->
                        CategoryTagResultUiState(
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
} 