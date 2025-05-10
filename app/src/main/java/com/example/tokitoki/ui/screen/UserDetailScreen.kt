package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    selectedUserId: String,
    orderBy: String,
    onBackClick: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // ViewModel 초기화
    LaunchedEffect(selectedUserId, orderBy) {
        viewModel.initialize(selectedUserId, orderBy)
    }

    // ViewModel의 상태를 수집
    val userDetails by viewModel.userDetails.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    // Pager 상태 설정
    val pagerState = rememberPagerState(initialPage = currentPage) { userDetails.size }

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
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
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