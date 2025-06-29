package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.domain.usecase.UpdateDatabaseUseCase
import com.example.tokitoki.utils.RetryHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokitokiViewModel @Inject constructor(
    private val updateDatabaseUseCase: UpdateDatabaseUseCase
) : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private val _shouldExit = MutableStateFlow(false)
    val shouldExit = _shouldExit.asStateFlow()

    init {
        val retryHelper = RetryHelper()
        viewModelScope.launch {
            val success = retryHelper.retry {
                val databaseUpdated = updateDatabaseUseCase()

                if (databaseUpdated) true else null
            }

            if (success != null) {
                _isReady.value = true
            } else {
                _shouldExit.value = true
                _errorState.value = "필요한 데이터를 로드하지 못했습니다."
            }
        }
    }
}