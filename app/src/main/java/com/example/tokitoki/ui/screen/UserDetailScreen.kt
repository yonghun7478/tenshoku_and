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
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.tokitoki.ui.viewmodel.PickupDirection
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel
// FlowRow를 위해 추가 (만약 accompanist 라이브러리가 없다면, 이 import는 오류를 발생시킬 수 있습니다.)
// import com.google.accompanist.flowlayout.FlowRow

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
    
    LaunchedEffect(selectedUserId, screenName) {
        viewModel.initialize(selectedUserId, screenName)
    }

    val userDetails by viewModel.userDetails.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val isLiked by viewModel.isLiked.collectAsState() // 기존 isLiked 상태
    val isFavorite by viewModel.isFavorite.collectAsState() // isFavorite 상태
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    val pagerState = rememberPagerState { userDetails.size }

    LaunchedEffect(currentPage) {
        if (currentPage < pagerState.pageCount) { // 안정성 추가
            pagerState.scrollToPage(currentPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentPage) { // 불필요한 호출 방지
             viewModel.onPageChanged(pagerState.currentPage)
        }
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
                // actions에 있던 즐겨찾기 버튼 제거
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
                if (userDetails.isNotEmpty() && page < userDetails.size) {
                    when (val userDetailResult = userDetails[page]) {
                        is ResultWrapper.Success -> {
                            UserDetailContent(
                                userDetail = userDetailResult.data,
                                isFavorite = isFavorite, // isFavorite 상태 전달
                                onFavoriteClick = { viewModel.toggleFavorite() }, // toggleFavorite 콜백 전달
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is ResultWrapper.Error -> {
                            ErrorContent(
                                error = userDetailResult.errorType,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is ResultWrapper.Loading -> {
                            LoadingContent(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else if (userDetails.isEmpty() && pagerState.pageCount == 0 && currentPage == 0) { // 초기 로딩 조건 명확화
                    LoadingContent(modifier = Modifier.fillMaxSize())
                } else if (page >= userDetails.size && userDetails.isNotEmpty()){ // Pager가 아직 업데이트 안된 경우
                    LoadingContent(modifier = Modifier.fillMaxSize()) // 또는 이전 사용자 정보 유지
                }
                 else {
                     ErrorContent( // 좀 더 명확한 오류 메시지
                         error = ResultWrapper.ErrorType.ExceptionError("프로필 정보를 불러올 수 없습니다. (page:$page, currentPage:$currentPage, details size:${userDetails.size}, pageCount:${pagerState.pageCount})"),
                         modifier = Modifier.fillMaxSize()
                     )
                 }
            }

            // 하단 버튼 영역 (screenName에 따라 다른 버튼 표시)
            if (screenName == "MainHomePickupScreen") {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                FloatingActionButton(
                    onClick = { viewModel.toggleLike() }, // 좋아요 버튼은 isLiked 상태에 따라 아이콘 변경
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    containerColor = if (isLiked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant // 색상 변경
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "좋아요",
                        tint = if (isLiked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary // 색상 변경
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // FlowRow 사용을 위해 추가
@Composable
private fun UserDetailContent(
    userDetail: UserDetail,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ThumbnailSection(
                thumbnailUrl = userDetail.thumbnailUrl,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            BasicInfoSection(
                name = userDetail.name,
                age = userDetail.age,
                location = userDetail.location
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        if (userDetail.myTags.isNotEmpty()) {
            item {
                SectionTitle(title = "마이 태그")
                MyTagsSection(tags = userDetail.myTags)
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        if (userDetail.introduction.isNotBlank()) {
            item {
                SectionTitle(title = "자기소개")
                IntroductionSection(introduction = userDetail.introduction)
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        item {
            SectionTitle(title = "프로필 정보")
            ProfileDetailsSection(userDetail = userDetail)
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) } 
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge, // 스타일 변경
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp) // 타이틀 아래 간격
    )
}

@Composable
private fun ThumbnailSection(
    thumbnailUrl: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // 정사각형 비율
            .clip(RoundedCornerShape(12.dp)) // 모서리 둥글게
            .background(MaterialTheme.colorScheme.surfaceVariant) // 배경색
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "프로필 썸네일",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop // 이미지 채우기 방식
        )
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.3f), CircleShape) // 반투명 배경 추가
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "찜하기",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White
            )
        }
    }
}

@Composable
private fun BasicInfoSection(name: String, age: Int, location: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // 왼쪽 정렬로 변경
    ) {
        Text(name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text("나이: $age", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            Text("거주지: $location", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MyTagsSection(tags: List<String>) {
    if (tags.isEmpty()) return
//
//    // FlowRow 사용 (com.google.accompanist:accompanist-flowlayout 의존성 필요)
//    // 만약 의존성이 없다면 HorizontalLazyRow 등으로 대체 구현 필요
//    com.google.accompanist.flowlayout.FlowRow(
//        mainAxisSpacing = 8.dp,
//        crossAxisSpacing = 8.dp,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        tags.forEach { tag ->
//            Surface(
//                shape = RoundedCornerShape(16.dp),
//                color = MaterialTheme.colorScheme.secondaryContainer,
//                onClick = { /* 태그 클릭 시 액션 (필요시) */ },
//                tonalElevation = 2.dp // 약간의 입체감
//            ) {
//                Text(
//                    text = tag,
//                    style = MaterialTheme.typography.labelMedium,
//                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
//                )
//            }
//        }
//    }
}


@Composable
private fun IntroductionSection(introduction: String) {
    Text(
        text = introduction,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProfileDetailsSection(userDetail: UserDetail) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        ProfileDetailItem("닉네임", userDetail.name)
        ProfileDetailItem("나이", userDetail.age.toString())
        ProfileDetailItem("성별", if (userDetail.isMale) "남성" else "여성")
        ProfileDetailItem("거주지", userDetail.location)
        ProfileDetailItem("혈액형", userDetail.bloodType)
        ProfileDetailItem("학력", userDetail.education)
        ProfileDetailItem("직종", userDetail.occupation)
        ProfileDetailItem("외견", userDetail.appearance)
        ProfileDetailItem("연애관", userDetail.datingPhilosophy)
        ProfileDetailItem("결혼관", userDetail.marriageView)
        if (userDetail.personalityTraits.isNotEmpty()) {
            ProfileDetailItem("성격", userDetail.personalityTraits.joinToString(", "))
        }
        if (userDetail.hobbies.isNotEmpty()){
            ProfileDetailItem("취미", userDetail.hobbies.joinToString(", "))
        }
        ProfileDetailItem("생활", userDetail.lifestyle)
    }
}

@Composable
private fun ProfileDetailItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.titleSmall, // 라벨 스타일
                modifier = Modifier.width(100.dp) // 라벨 너비 고정으로 정렬 효과
            )
            Text(value, style = MaterialTheme.typography.bodyMedium) // 값 스타일
        }
    }
}

// 기존 LoadingContent, ErrorContent 함수 (변경 없음)
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

// androidx.compose.foundation.layout.ExperimentalLayoutApi 임포트 추가
// import androidx.compose.foundation.layout.ExperimentalLayoutApi 
// 주: 이미 파일 상단에 OptIn으로 추가되어 있다면 별도 import 문은 없어도 될 수 있음.
// 하지만 명시적으로 추가하는 것이 좋을 수 있음.
// 실제로는 @OptIn(ExperimentalLayoutApi::class) 어노테이션이 있는 컴포저블 내부에서만 사용 가능.
// MyTagsSection과 UserDetailContent에 이미 OptIn이 되어있음.

// 주: 이 파일에서 사용되는 모든 컴포저블에서 필요한 경우 @OptIn(ExperimentalLayoutApi::class) 어노테이션을 추가해야 합니다.
// 이는 해당 컴포저블에서 사용되는 모든 컴포저블에 적용되어야 합니다. 