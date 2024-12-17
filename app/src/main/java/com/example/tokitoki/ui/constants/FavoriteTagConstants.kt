package com.example.tokitoki.ui.constants

object FavoriteTagConstants {
    const val TAG = "FavoriteTagConstants"
}

sealed class FavoriteTagAction {
    data object NOTHING : FavoriteTagAction()
    data class CategoryTabClicked(val index: Int) : FavoriteTagAction()
    data object BackBtnClicked : FavoriteTagAction()
}