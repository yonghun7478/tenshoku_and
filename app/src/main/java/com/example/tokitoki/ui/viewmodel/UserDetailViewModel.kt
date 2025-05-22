package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.usecase.GetUserDetailUseCase
import com.example.tokitoki.domain.usecase.GetCachedUserIdsUseCase
import com.example.tokitoki.domain.usecase.AddUserDetailToCacheUseCase
import com.example.tokitoki.domain.usecase.GetUserDetailFromCacheUseCase
import com.example.tokitoki.domain.usecase.LikeUserUseCase
import com.example.tokitoki.domain.usecase.AddToFavoritesUseCase
import com.example.tokitoki.domain.usecase.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getCachedUserIdsUseCase: GetCachedUserIdsUseCase,
    private val addUserDetailToCacheUseCase: AddUserDetailToCacheUseCase,
    private val getUserDetailFromCacheUseCase: GetUserDetailFromCacheUseCase,
    private val likeUserUseCase: LikeUserUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {

    private val _userDetails = MutableStateFlow<List<ResultWrapper<UserDetail>>>(emptyList())
    val userDetails: StateFlow<List<ResultWrapper<UserDetail>>> = _userDetails.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private var cachedUserIds: List<String> = emptyList()
    private val preloadBuffer = 2 // 현재 페이지 기준 앞뒤로 2명씩 프리로드

    fun initialize(selectedUserId: String, screenName: String) {
        viewModelScope.launch {
            // 1. 캐시된 유저 ID 목록 가져오기
            cachedUserIds = getCachedUserIdsUseCase(screenName)
            
            // 2. 선택된 유저의 인덱스 찾기
            val selectedIndex = cachedUserIds.indexOf(selectedUserId)
            if (selectedIndex == -1) {
                _userDetails.value = listOf(ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("유저를 찾을 수 없습니다.")))
                return@launch
            }

            // 3. 초기 유저 상세 정보 리스트 생성 (모두 Loading 상태로)
            val initialList = List(cachedUserIds.size) { ResultWrapper.Loading }
            _userDetails.value = initialList
            _currentPage.value = selectedIndex

            // 4. 선택된 유저와 주변 유저들의 정보 로드
            loadUserDetails(selectedIndex)
        }
    }

    fun onPageChanged(newPage: Int) {
        if (newPage == _currentPage.value) return
        
        _currentPage.value = newPage
        loadUserDetails(newPage)
    }

    private fun loadUserDetails(centerIndex: Int) {
        viewModelScope.launch {
            // 현재 페이지 기준 앞뒤로 preloadBuffer만큼의 유저 정보 로드
            val startIndex = (centerIndex - preloadBuffer).coerceAtLeast(0)
            val endIndex = (centerIndex + preloadBuffer).coerceAtMost(cachedUserIds.size - 1)

            // 로드가 필요한 인덱스 목록 생성
            val indicesToLoad = (startIndex..endIndex).filter { index ->
                _userDetails.value[index] is ResultWrapper.Loading
            }

            if (indicesToLoad.isEmpty()) return@launch

            // 모든 유저 정보를 병렬로 로드
            val results = indicesToLoad.map { index ->
                async {
                    val userId = cachedUserIds[index]
                    // 먼저 캐시에서 확인
                    val cachedDetail = getUserDetailFromCacheUseCase(userId)
                    if (cachedDetail != null) {
                        index to ResultWrapper.Success(cachedDetail)
                    } else {
                        // 캐시에 없으면 API 호출
                        val result = getUserDetailUseCase(userId)
                        // 성공한 경우 캐시에 저장
                        if (result is ResultWrapper.Success) {
                            addUserDetailToCacheUseCase(userId, result.data)
                        }
                        index to result
                    }
                }
            }.awaitAll()

            // 결과를 현재 리스트에 반영
            val updatedList = _userDetails.value.toMutableList()
            results.forEach { (index, result) ->
                updatedList[index] = result
            }
            _userDetails.value = updatedList
        }
    }

    fun toggleLike() {
        // 이미 좋아요를 누른 상태라면 아무 동작도 하지 않음
        if (_isLiked.value) return

        val currentUser = userDetails.value.getOrNull(currentPage.value)?.let {
            if (it is ResultWrapper.Success) it.data else null
        } ?: return

        viewModelScope.launch {
            when (val result = likeUserUseCase(currentUser.id)) {
                is ResultWrapper.Success -> {
                    _isLiked.value = true
                    _toastMessage.value = "좋아요를 보냈습니다"
                }
                is ResultWrapper.Error -> {
                    _toastMessage.value = when (result.errorType) {
                        is ResultWrapper.ErrorType.ServerError -> "서버 오류가 발생했습니다"
                        is ResultWrapper.ErrorType.ExceptionError -> result.errorType.message
                    }
                }
                else -> {}
            }
        }
    }

    fun toggleFavorite() {
        val currentUser = userDetails.value.getOrNull(currentPage.value)?.let {
            if (it is ResultWrapper.Success) it.data else null
        } ?: return

        viewModelScope.launch {
            val result = if (_isFavorite.value) {
                removeFromFavoritesUseCase(currentUser.id)
            } else {
                addToFavoritesUseCase(currentUser.id)
            }

            when (result) {
                is ResultWrapper.Success -> {
                    _isFavorite.value = !_isFavorite.value
                    _toastMessage.value = if (_isFavorite.value) "즐겨찾기에 추가했습니다" else "즐겨찾기에서 제거했습니다"
                }
                is ResultWrapper.Error -> {
                    _toastMessage.value = when (result.errorType) {
                        is ResultWrapper.ErrorType.ServerError -> "서버 오류가 발생했습니다"
                        is ResultWrapper.ErrorType.ExceptionError -> result.errorType.message
                    }
                }
                else -> {}
            }
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
} 