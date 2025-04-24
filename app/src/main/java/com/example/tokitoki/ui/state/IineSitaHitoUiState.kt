package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.LikedUser

data class IineSitaHitoUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val users: List<LikedUser> = emptyList(),
    val error: String? = null,
    val hasMoreItems: Boolean = true
) 