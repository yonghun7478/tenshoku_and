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
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.tokitoki.ui.viewmodel.PickupDirection
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.example.tokitoki.domain.model.MainHomeTag
import androidx.compose.ui.res.painterResource
import com.example.tokitoki.R

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
    val userTags by viewModel.userTags.collectAsState() // userTags 상태 관찰 추가

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
                title = { /* Text("프로필") */ }, // "프로필" 텍스트 제거
                navigationIcon = {
                    IconButton(onClick = onBackClick,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape) // 반투명 배경 추가
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color.White // 흰색으로 변경
                        )
                    }
                },
                actions = { // 즐겨찾기 버튼을 actions로 이동
                    IconButton(onClick = { viewModel.toggleFavorite() },
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape) // 반투명 배경 항상 유지
                    ) {
                        if (isFavorite) {
                            Icon(
                                imageVector = Icons.Default.Star, // 사용자의 마지막 수동 변경 사항 반영
                                contentDescription = "찜하기",
                                tint = Color.White // 사용자의 마지막 수동 변경 사항 반영
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_star_outline_24),
                                contentDescription = "찜하기",
                                tint = Color.White // 활성 상태와 동일하게 흰색으로 유지
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // 배경 투명화
                    scrolledContainerColor = Color.Transparent, // 스크롤 시에도 투명 유지
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues -> // paddingValues 다시 사용
        Box(
            modifier = Modifier
                .fillMaxSize()
                // .padding(paddingValues) // 이 줄 제거
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
                                userTags = userTags, // userTags 파라미터는 이미 List<MainHomeTag> 타입을 따름
                                isLiked = isLiked, // isLiked 상태 전달
                                onToggleLike = { viewModel.toggleLike() }, // 좋아요 토글 함수 전달
                                modifier = Modifier.fillMaxSize(),
                                scaffoldTopPadding = paddingValues.calculateTopPadding()
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
                // 기존 FloatingActionButton 제거
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // FlowRow 사용을 위해 추가
@Composable
private fun UserDetailContent(
    userDetail: UserDetail,
    userTags: List<MainHomeTag>, // List<String> -> List<MainHomeTag> 변경
    isLiked: Boolean, // isLiked 파라미터 추가
    onToggleLike: () -> Unit, // onToggleLike 파라미터 추가
    modifier: Modifier = Modifier,
    scaffoldTopPadding: Dp
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ThumbnailSection(
                thumbnailUrl = userDetail.thumbnailUrl,
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

        if (userTags.isNotEmpty()) { // userDetail.myTags 대신 userTags 사용
            item {
                SectionTitle(title = "마이 태그")
                MyTagsSection(tags = userTags) // userDetail.myTags 대신 userTags 사용
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
        
        // 기존 Spacer(modifier = Modifier.height(80.dp)) 제거

        // 좋아요 버튼 추가
        item {
            Button(
                onClick = onToggleLike,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLiked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isLiked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "좋아요",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("좋아요")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), // 스타일 변경 및 fontWeight 명시
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp) // 타이틀 아래 간격
    )
}

@Composable
private fun ThumbnailSection(
    thumbnailUrl: String,
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
    }
}

@Composable
private fun BasicInfoSection(name: String, age: Int, location: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // 왼쪽 정렬로 변경
    ) {
        Text(name, style = MaterialTheme.typography.titleLarge) // 닉네임 스타일 변경
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${age}세 $location", // 나이와 거주지 한 줄로 표시
            style = MaterialTheme.typography.bodySmall, // 스타일 변경
            color = MaterialTheme.colorScheme.onSurfaceVariant // 색상 연하게 변경
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MyTagsSection(tags: List<MainHomeTag>) { // List<String> -> List<MainHomeTag> 변경
    if (tags.isEmpty()) return

    FlowRow( // Column 대신 FlowRow 사용
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 태그 간 가로 간격
        verticalArrangement = Arrangement.spacedBy(8.dp) // 태그 간 세로 간격
    ) {
        tags.forEach { tag -> // itemsToShow 대신 tags 사용
            MyTagChip(
                tagText = tag.name, // MainHomeTag 객체에서 name 필드를 태그 텍스트로 사용
                tagImageUrl = tag.imageUrl // MainHomeTag 객체에서 imageUrl 필드를 태그 이미지 URL로 사용
                                            // 만약 MainHomeTag에 imageUrl 필드가 없다면:
                                            // tagImageUrl = "https://picsum.photos/seed/${tag.name.hashCode()}/48/48"
            )
        }
    }
}

@Composable
private fun MyTagChip(tagText: String, tagImageUrl: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 왼쪽: AsyncImage로 변경
            AsyncImage(
                model = tagImageUrl, // URL 사용
                contentDescription = "태그 이미지: $tagText",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop // 이미지 채우기 방식
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tagText, // tagText 사용
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
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
        ProfilePropertyGroupTitle(title = "기본 정보")
        ProfileDetailItem("닉네임", userDetail.name)
        ProfileDetailItem("나이", userDetail.age.toString())
        ProfileDetailItem("성별", if (userDetail.isMale) "남성" else "여성")
        ProfileDetailItem("거주지", userDetail.location)

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "신체 정보")
        ProfileDetailItem("혈액형", userDetail.bloodType)
        // TODO: UserDetail에 키(height) 필드가 있다면 추가
        // ProfileDetailItem("키", userDetail.height.toString() + "cm") 
        ProfileDetailItem("외견", userDetail.appearance) // 체형에 해당될 수 있음

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "학력 및 직업")
        ProfileDetailItem("학력", userDetail.education)
        ProfileDetailItem("직종", userDetail.occupation)
        // TODO: UserDetail에 연수입(annualIncome) 필드가 있다면 추가
        // ProfileDetailItem("연수입", userDetail.annualIncome)

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "가치관")
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
private fun ProfilePropertyGroupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun ProfileDetailItem(label: String, value: String?) {
    if (!value.isNullOrBlank()) {
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyMedium, // 레이블 스타일 변경
                color = MaterialTheme.colorScheme.onSurfaceVariant, // 레이블 색상 변경
                modifier = Modifier.width(100.dp) // 라벨 너비 고정으로 정렬 효과
            )
            Text(value, style = MaterialTheme.typography.bodyMedium) // 값 스타일 유지
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