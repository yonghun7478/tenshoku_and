package com.example.tokitoki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.usecase.GetUserDetailUseCase
import com.example.tokitoki.domain.usecase.GetCachedUserIdsUseCase
import com.example.tokitoki.domain.usecase.AddUserDetailToCacheUseCase
import com.example.tokitoki.domain.usecase.LikeUserUseCase
import com.example.tokitoki.domain.usecase.AddToFavoritesUseCase
import com.example.tokitoki.domain.usecase.RemoveFromFavoritesUseCase
import com.example.tokitoki.domain.usecase.CheckIsUserLikedUseCase
import com.example.tokitoki.domain.usecase.CheckIsUserFavoriteUseCase
import com.example.tokitoki.domain.usecase.tag.GetUserTagsUseCase
import com.example.tokitoki.ui.state.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getCachedUserIdsUseCase: GetCachedUserIdsUseCase,
    private val addUserDetailToCacheUseCase: AddUserDetailToCacheUseCase,
    private val likeUserUseCase: LikeUserUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val checkIsUserLikedUseCase: CheckIsUserLikedUseCase,
    private val checkIsUserFavoriteUseCase: CheckIsUserFavoriteUseCase,
    private val getUserTagsUseCase: GetUserTagsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<ResultWrapper<UserDetailUiState>>>(emptyList())
    val uiState: StateFlow<List<ResultWrapper<UserDetailUiState>>> = _uiState.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private var cachedUserIds: List<String> = emptyList()
    private val preloadBuffer = 2

    fun initialize(selectedUserId: String, screenName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cachedUserIds = getCachedUserIdsUseCase(screenName)
            val selectedIndex = cachedUserIds.indexOf(selectedUserId)

            if (selectedIndex == -1) {
                _uiState.value = listOf(ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError("유저를 찾을 수 없습니다.")))
                return@launch
            }

            val initialList = List(cachedUserIds.size) { ResultWrapper.Loading }
            _uiState.value = initialList
            _currentPage.value = selectedIndex

            loadUserUiStates(selectedIndex)
        }
    }

    fun onPageChanged(newPage: Int) {
        if (newPage == _currentPage.value || newPage < 0 || newPage >= cachedUserIds.size) return
        
        _currentPage.value = newPage
        loadUserUiStates(newPage)
    }

    private fun loadUserUiStates(centerIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val startIndex = (centerIndex - preloadBuffer).coerceAtLeast(0)
            val endIndex = (centerIndex + preloadBuffer).coerceAtMost(cachedUserIds.size - 1)

            val indicesToLoad = (startIndex..endIndex).filter { index ->
                _uiState.value.getOrNull(index) is ResultWrapper.Loading
            }

            if (indicesToLoad.isEmpty()) return@launch

            val results = indicesToLoad.map { index ->
                async {
                    val userId = cachedUserIds[index]
                    index to loadFullUserDetail(userId)
                }
            }.awaitAll()

            val updatedList = _uiState.value.toMutableList()
            results.forEach { (index, result) ->
                if (index < updatedList.size) {
                    updatedList[index] = result
                }
            }
            _uiState.value = updatedList
        }
    }

    private suspend fun loadFullUserDetail(userId: String): ResultWrapper<UserDetailUiState> {
        return try {
            coroutineScope {
                val detailDeferred = async { getUserDetailUseCase(userId) }
                val likedDeferred = async { checkIsUserLikedUseCase(userId) }
                val favoriteDeferred = async { checkIsUserFavoriteUseCase(userId) }
                val tagsDeferred = async { getUserTagsUseCase(userId) }

                val userDetailResult = detailDeferred.await()
                val isLikedResult = likedDeferred.await()
                val isFavoriteResult = favoriteDeferred.await()
                val userTagsResult = tagsDeferred.await()

                val userTags = userTagsResult.fold(
                    onSuccess = { it },
                    onFailure = { emptyList() }
                )

                val userDetail = when (userDetailResult) {
                    is ResultWrapper.Success -> userDetailResult.data
                    is ResultWrapper.Error -> return@coroutineScope ResultWrapper.Error(userDetailResult.errorType)
                    is ResultWrapper.Loading -> return@coroutineScope ResultWrapper.Loading
                }

                val isLiked = (isLikedResult as? ResultWrapper.Success)?.data ?: false
                val isFavorite = (isFavoriteResult as? ResultWrapper.Success)?.data ?: false

                addUserDetailToCacheUseCase(userId, userDetail)
                
                ResultWrapper.Success(
                    UserDetailUiState(
                        userDetail = userDetail,
                        userTags = userTags,
                        isLiked = isLiked,
                        isFavorite = isFavorite
                    )
                )
            }
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.localizedMessage ?: "알 수 없는 에러 발생"))
        }
    }


    fun toggleLike() {
        val currentState = _uiState.value.getOrNull(currentPage.value)
        val uiState = (currentState as? ResultWrapper.Success)?.data ?: return

        if (uiState.isLiked) {
            _toastMessage.value = "이미 좋아요를 눌렀습니다."
            return
        }
        
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = likeUserUseCase(uiState.userDetail.id)) {
                is ResultWrapper.Success -> {
                    updateState(currentPage.value) { it.copy(isLiked = true) }
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
        val currentState = _uiState.value.getOrNull(currentPage.value)
        val uiState = (currentState as? ResultWrapper.Success)?.data ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val result = if (uiState.isFavorite) {
                removeFromFavoritesUseCase(uiState.userDetail.id)
            } else {
                addToFavoritesUseCase(uiState.userDetail.id)
            }

            when (result) {
                is ResultWrapper.Success -> {
                    val newIsFavorite = !uiState.isFavorite
                    updateState(currentPage.value) { it.copy(isFavorite = newIsFavorite) }
                    _toastMessage.value = if (newIsFavorite) "즐겨찾기에 추가했습니다" else "즐겨찾기에서 제거했습니다"
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

    private fun updateState(index: Int, block: (UserDetailUiState) -> UserDetailUiState) {
        val updatedList = _uiState.value.toMutableList()
        val currentResult = updatedList.getOrNull(index)
        if (currentResult is ResultWrapper.Success) {
            updatedList[index] = ResultWrapper.Success(block(currentResult.data))
            _uiState.value = updatedList
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
} 