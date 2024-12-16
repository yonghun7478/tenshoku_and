package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.FavoriteTagAction

sealed class FavoriteTagEvent {
    data object NOTHING : FavoriteTagEvent()
    data class ACTION(val action: FavoriteTagAction) : FavoriteTagEvent()
}