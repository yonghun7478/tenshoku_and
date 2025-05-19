package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.usecase.GetUserDetailUseCase
import com.example.tokitoki.domain.usecase.GetCachedUserIdsUseCase
import com.example.tokitoki.domain.usecase.AddUserDetailToCacheUseCase
import com.example.tokitoki.domain.usecase.GetUserDetailFromCacheUseCase
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
    private val getUserDetailFromCacheUseCase: GetUserDetailFromCacheUseCase
) : ViewModel() {

    private val _userDetails = MutableStateFlow<List<ResultWrapper<UserDetail>>>(emptyList())
    val userDetails: StateFlow<List<ResultWrapper<UserDetail>>> = _userDetails.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

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
} 