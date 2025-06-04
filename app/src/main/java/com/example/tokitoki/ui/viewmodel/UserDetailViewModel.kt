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
import com.example.tokitoki.domain.usecase.CheckIsUserLikedUseCase
import com.example.tokitoki.domain.usecase.CheckIsUserFavoriteUseCase
import com.example.tokitoki.domain.usecase.tag.GetUserTagsUseCase
import com.example.tokitoki.domain.model.MainHomeTag
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
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val checkIsUserLikedUseCase: CheckIsUserLikedUseCase,
    private val checkIsUserFavoriteUseCase: CheckIsUserFavoriteUseCase,
    private val getUserTagsUseCase: GetUserTagsUseCase
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

    private val _userTags = MutableStateFlow<List<MainHomeTag>>(emptyList())
    val userTags: StateFlow<List<MainHomeTag>> = _userTags.asStateFlow()

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

            // 5. 현재 유저의 좋아요/즐겨찾기 상태 로드
            if (selectedIndex != -1 && selectedIndex < cachedUserIds.size) {
                updateLikeAndFavoriteStatus(cachedUserIds[selectedIndex])
                updateUserTags(cachedUserIds[selectedIndex])
            }
        }
    }

    fun onPageChanged(newPage: Int) {
        if (newPage == _currentPage.value) return
        
        _currentPage.value = newPage
        loadUserDetails(newPage)

        // 현재 페이지 유저의 좋아요/즐겨찾기 상태 업데이트
        if (newPage >= 0 && newPage < cachedUserIds.size) {
            updateLikeAndFavoriteStatus(cachedUserIds[newPage])
            updateUserTags(cachedUserIds[newPage])
        }
    }

    private fun loadUserDetails(centerIndex: Int) {
        viewModelScope.launch {
            // 현재 페이지 기준 앞뒤로 preloadBuffer만큼의 유저 정보 로드
            val startIndex = (centerIndex - preloadBuffer).coerceAtLeast(0)
            val endIndex = (centerIndex + preloadBuffer).coerceAtMost(cachedUserIds.size - 1)

            // 로드가 필요한 인덱스 목록 생성
            val indicesToLoad = (startIndex..endIndex).filter { index ->
                _userDetails.value.getOrNull(index) is ResultWrapper.Loading
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
                if (index < updatedList.size) { // IndexOutOfBounds 방지
                    updatedList[index] = result
                }
            }
            _userDetails.value = updatedList
        }
    }

    fun toggleLike() {
        // 이미 좋아요를 누른 상태라면 아무 동작도 하지 않음
        // if (_isLiked.value) return // 좋아요 상태는 서버에서 관리하므로, 클라이언트에서 중복 방지 로직 제거 가능

        val currentUserDetail = userDetails.value.getOrNull(currentPage.value)?.let {
            if (it is ResultWrapper.Success) it.data else null
        } ?: return

        viewModelScope.launch {
            when (val result = likeUserUseCase(currentUserDetail.id)) {
                is ResultWrapper.Success -> {
                    // 성공 시, 좋아요 상태를 다시 확인하여 UI 업데이트
                    updateLikeAndFavoriteStatus(currentUserDetail.id)
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
        val currentUserDetail = userDetails.value.getOrNull(currentPage.value)?.let {
            if (it is ResultWrapper.Success) it.data else null
        } ?: return

        viewModelScope.launch {
            val result = if (_isFavorite.value) {
                removeFromFavoritesUseCase(currentUserDetail.id)
            } else {
                addToFavoritesUseCase(currentUserDetail.id)
            }

            when (result) {
                is ResultWrapper.Success -> {
                    // 성공 시, 즐겨찾기 상태를 다시 확인하여 UI 업데이트
                    updateLikeAndFavoriteStatus(currentUserDetail.id) 
                    // 토스트 메시지는 isFavorite 상태에 따라 분기 처리 유지 가능
                    _toastMessage.value = if (!_isFavorite.value) "즐겨찾기에 추가했습니다" else "즐겨찾기에서 제거했습니다" 
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

    // 좋아요 및 즐겨찾기 상태를 업데이트하는 새로운 private 함수
    private fun updateLikeAndFavoriteStatus(userId: String) {
        viewModelScope.launch {
            // 좋아요 상태 확인
            when (val likedResult = checkIsUserLikedUseCase(userId)) {
                is ResultWrapper.Success -> _isLiked.value = likedResult.data
                is ResultWrapper.Error -> {
                    _isLiked.value = false // 오류 시 기본값 false
                     _toastMessage.value = "좋아요 상태 로드 실패: ${likedResult.errorType}" // 필요시 주석 해제
                }
                is ResultWrapper.Loading -> { /* 로딩 중 UI 처리는 현재 없음 */ }
            }

            // 즐겨찾기 상태 확인
            when (val favoriteResult = checkIsUserFavoriteUseCase(userId)) {
                is ResultWrapper.Success -> _isFavorite.value = favoriteResult.data
                is ResultWrapper.Error -> {
                    _isFavorite.value = false // 오류 시 기본값 false
                    _toastMessage.value = "즐겨찾기 상태 로드 실패: ${favoriteResult.errorType}" // 필요시 주석 해제
                }
                is ResultWrapper.Loading -> { /* 로딩 중 UI 처리는 현재 없음 */ }
            }
        }
    }

    // 사용자 태그를 업데이트하는 새로운 private 함수
    private fun updateUserTags(userId: String) {
        viewModelScope.launch {
            getUserTagsUseCase(userId)
                .onSuccess { mainHomeTags ->
                    _userTags.value = mainHomeTags // mainHomeTags를 직접 할당
                }
                .onFailure { throwable ->
                    _userTags.value = emptyList() // 오류 시 빈 리스트
                    _toastMessage.value = "태그 로드 실패: ${throwable.localizedMessage}"
                }
        }
    }
} 