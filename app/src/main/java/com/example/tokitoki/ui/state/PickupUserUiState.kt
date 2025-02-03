package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.PickupUserItem

data class PickupUserUiState(
    val users: List<PickupUserItem> = emptyList(),
    val errorMessage: String? = null
)
