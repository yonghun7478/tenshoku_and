package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.PickupUserItem

data class PickupUserUiState(
    val state: PickupUserState = PickupUserState.NONE,
    val users: List<PickupUserItem> = emptyList(),
    val errorMessage: String? = null
)

enum class PickupUserState { NONE, LOADING, COMPLETE }
