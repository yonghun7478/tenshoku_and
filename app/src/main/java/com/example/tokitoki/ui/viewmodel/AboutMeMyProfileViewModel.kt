package com.example.tokitoki.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem
import com.example.tokitoki.ui.state.AboutMeMyProfileEvent
import com.example.tokitoki.ui.state.AboutMeMyProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AboutMeMyProfileViewModel
@Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(AboutMeMyProfileState())
    val uiState: StateFlow<AboutMeMyProfileState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AboutMeMyProfileEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    suspend fun init() {
        val myProfileItem = MyProfileItem(
            name = "yonghun",
            age = "33",
            thumbnailImageUri = Uri.EMPTY,
            myTagItems = listOf(
                MyTagItem(
                    title = "テストタグ1",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー1"
                ),
                MyTagItem(
                    title = "テストタグ2",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー2"
                ),
                MyTagItem(
                    title = "テストタグ3",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    categoryTitle = "テストカテゴリー3"
                ),
            ),
            mySelfSentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst."
        )

        _uiState.update { currentState ->
            currentState.copy(
                isInitialized = true,
                myProfileItem = myProfileItem,
            )
        }
    }
}