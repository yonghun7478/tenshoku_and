package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.state.AshiatoData
import com.example.tokitoki.ui.state.AshiatoScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AshiatoViewModel @Inject constructor(
    // If you have any dependencies, list them here
) : ViewModel() {
    private val _state = MutableStateFlow(AshiatoScreenState(AshiatoData.dummyUsers))
    val state: StateFlow<AshiatoScreenState> = _state

    fun onUserClicked(userId: String) {
        // Handle user click event
    }
}