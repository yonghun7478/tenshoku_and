package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedPickupViewModel @Inject constructor() : ViewModel() {
    private val _pickupDirection = MutableStateFlow<PickupDirection>(PickupDirection.NONE)
    val pickupDirection = _pickupDirection.asStateFlow()

    fun setPickupDirection(direction: PickupDirection) {
        _pickupDirection.value = direction
    }

    fun consumePickupDirection() {
        _pickupDirection.value = PickupDirection.NONE
    }
}

enum class PickupDirection {
    LEFT, RIGHT, NONE
} 