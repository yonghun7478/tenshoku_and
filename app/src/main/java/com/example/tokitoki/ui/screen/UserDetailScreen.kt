package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.ui.viewmodel.UserDetailViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import android.widget.Toast
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.platform.LocalContext
import com.example.tokitoki.ui.viewmodel.PickupDirection
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    selectedUserId: String,
    screenName: String,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel(),
    sharedViewModel: SharedPickupViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // ViewModel 초기화
    LaunchedEffect(selectedUserId, screenName) {
        viewModel.initialize(selectedUserId, screenName)
    }

    // ViewModel의 상태를 수집
    val userDetails by viewModel.userDetails.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val isLiked by viewModel.isLiked.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // 토스트 메시지 처리
    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    // Pager 상태 설정
    val pagerState = rememberPagerState { userDetails.size }

    // currentPage가 변경될 때 pagerState 업데이트
    LaunchedEffect(currentPage) {
        pagerState.scrollToPage(currentPage)
    }

    // 페이지 변경 감지
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    // 즐겨찾기 버튼
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "즐겨찾기",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (val userDetail = userDetails[page]) {
                    is ResultWrapper.Success -> {
                        UserDetailContent(
                            userDetail = userDetail.data,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is ResultWrapper.Error -> {
                        ErrorContent(
                            error = userDetail.errorType,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    is ResultWrapper.Loading -> {
                        LoadingContent(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // 하단 버튼 영역
            if (screenName == "MainHomePickupScreen") {
                // 픽업화면에서 왔을 때는 좌우 화살표 버튼 표시
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 왼쪽 화살표 버튼
                    IconButton(
                        onClick = { 
                            sharedViewModel.setPickupDirection(PickupDirection.LEFT)
                            onBackClick()
                        },
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "いまいち",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // 오른쪽 화살표 버튼
                    IconButton(
                        onClick = { 
                            sharedViewModel.setPickupDirection(PickupDirection.RIGHT)
                            onBackClick()
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "いいね",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                // 그 외의 경우 기존 좋아요 버튼 표시
                FloatingActionButton(
                    onClick = { viewModel.toggleLike() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    containerColor = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "좋아요",
                        tint = if (isLiked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun UserDetailContent(
    userDetail: UserDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 이미지
        AsyncImage(
            model = userDetail.thumbnailUrl,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 사용자 정보
        Text(
            text = userDetail.name,
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 이메일
        Text(
            text = userDetail.email,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 생년월일
        Text(
            text = "생년월일: ${userDetail.birthDay}",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 성별
        Text(
            text = "성별: ${if (userDetail.isMale) "남성" else "여성"}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    error: ResultWrapper.ErrorType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (error) {
                is ResultWrapper.ErrorType.ExceptionError -> error.message
                else -> "알 수 없는 오류가 발생했습니다."
            },
            color = MaterialTheme.colorScheme.error
        )
    }
} 