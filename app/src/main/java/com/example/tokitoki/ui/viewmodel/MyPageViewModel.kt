package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.ui.state.MyPageDummyData
import com.example.tokitoki.ui.state.MyPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel
@HiltViewModel // HiltViewModel 어노테이션 추가
class MyPageViewModel @Inject constructor( // @Inject 생성자 추가
    // 필요한 의존성들을 여기에 선언합니다. 예시:
    // private val userRepository: UserRepository
) : ViewModel() {
    private val _myPageState = MutableStateFlow(MyPageDummyData.myPageState)
    val myPageState: StateFlow<MyPageState> = _myPageState.asStateFlow()

    fun onEditProfileClick() {
        // Use Case 호출 (아직 구현 안됨)
        println("ViewModel: 프로필 수정 클릭")
        // UI 상태 업데이트 (예시)
        viewModelScope.launch { //launch 추가
            _myPageState.emit(_myPageState.value.copy(nickname = "New Nickname"))
        }

    }

    fun onSeenMeClick() {
        println("ViewModel: 나를 본 사람 클릭")
    }

    fun onFavoritesClick() {
        println("ViewModel: 즐겨찾기 클릭")
    }

    fun onLikedMeClick() {
        println("ViewModel: 내가 좋아요 한 사람 클릭")
    }

    fun onLogoutClick() {
        println("ViewModel: 로그아웃 클릭")
    }
}