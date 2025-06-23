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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.tokitoki.ui.viewmodel.PickupDirection
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.example.tokitoki.domain.model.MainHomeTag
import androidx.compose.ui.res.painterResource
import com.example.tokitoki.R
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.ui.theme.TokitokiTheme
import androidx.compose.ui.unit.LayoutDirection
import com.example.tokitoki.ui.theme.LocalColor

@OptIn(ExperimentalFoundationApi::class)
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

    val listState = rememberLazyListState() // LazyListState 추가
    val showFab by remember { // FAB 표시 여부 상태
        derivedStateOf {
            // 아이템이 하나 이상 있고, 더 이상 아래로 스크롤할 수 없을 때 FAB를 표시
            listState.layoutInfo.totalItemsCount > 0 && !listState.canScrollForward
        }
    }

    UserDetailContent(
        userDetails = userDetails,
        userTags = userTags,
        currentPage = currentPage,
        isLiked = isLiked,
        isFavorite = isFavorite,
        pagerState = pagerState,
        listState = listState,
        showFab = showFab,
        screenName = screenName,
        onBackClick = onBackClick,
        onToggleFavorite = { viewModel.toggleFavorite() },
        onToggleLike = { viewModel.toggleLike() },
        onPickupLeftClick = {
            sharedViewModel.setPickupDirection(PickupDirection.LEFT)
            onBackClick()
        },
        onPickupRightClick = {
            sharedViewModel.setPickupDirection(PickupDirection.RIGHT)
            onBackClick()
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun UserDetailContent(
    // states
    userDetails: List<ResultWrapper<UserDetail>>,
    userTags: List<MainHomeTag>,
    currentPage: Int,
    isLiked: Boolean,
    isFavorite: Boolean,
    pagerState: androidx.compose.foundation.pager.PagerState,
    listState: LazyListState,
    showFab: Boolean,
    screenName: String,

    // event handlers
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleLike: () -> Unit,
    onPickupLeftClick: () -> Unit,
    onPickupRightClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (screenName != "MessageListScreen") { // screenName이 "MessageListScreen"이 아닐 때만 TopAppBar 표시
                TopAppBar(
                    title = { /* Text("프로필") */ }, // "프로필" 텍스트 제거
                    navigationIcon = {
                        IconButton(onClick = onBackClick,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape) // 반투명 배경 추가
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "戻る",
                                tint = Color.White // 흰색으로 변경
                            )
                        }
                    },
                    actions = { // 즐겨찾기 버튼을 actions로 이동
                        IconButton(onClick = onToggleFavorite,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape) // 반투명 배경 항상 유지
                        ) {
                            if (isFavorite) {
                                Icon(
                                    imageVector = Icons.Default.Star, // 사용자의 마지막 수동 변경 사항 반영
                                    contentDescription = "お気に入り",
                                    tint = Color.White // 사용자의 마지막 수동 변경 사항 반영
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_star_outline_24),
                                    contentDescription = "お気に入り",
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
        },
        floatingActionButton = { // FloatingActionButton을 Button으로 변경
            if (screenName != "MessageListScreen" && screenName != "MainHomePickupScreen") { // "MessageListScreen" 또는 "MainHomePickupScreen"이 아닐 때만 좋아요 버튼 표시
                AnimatedVisibility(
                    visible = showFab,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // 좌우 패딩 추가
                ) {
                    Button(
                        onClick = onToggleLike,
                        modifier = Modifier.fillMaxWidth(), // 버튼 너비 꽉 채우기
                        enabled = !isLiked, // isLiked가 true일 때 버튼 비활성화
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalColor.current.blue,
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFFFA500), // 오렌지색
                            disabledContentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "いいね",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("いいね")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center // FAB 위치를 중앙 하단으로 변경
    ) { paddingValues -> // paddingValues 다시 사용
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateRightPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (userDetails.isNotEmpty() && page < userDetails.size) {
                    when (val userDetailResult = userDetails[page]) {
                        is ResultWrapper.Success -> {
                            UserDetailPage(
                                userDetail = userDetailResult.data,
                                userTags = userTags,
                                modifier = Modifier.fillMaxSize(),
                                listState = listState
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
                        error = ResultWrapper.ErrorType.ExceptionError("プロフィール情報を読み込めませんでした。(page:$page, currentPage:$currentPage, details size:${userDetails.size}, pageCount:${pagerState.pageCount})"),
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
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onPickupLeftClick,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "いまいち",
                            tint = LocalColor.current.blue
                        )
                    }

                    IconButton(
                        onClick = onPickupRightClick,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "いいね",
                            tint = LocalColor.current.blue
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
private fun UserDetailPage(
    userDetail: UserDetail,
    userTags: List<MainHomeTag>,
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LocalColor.current.lightGray), // 배경색 추가
        state = listState,
        contentPadding = PaddingValues(bottom = 88.dp), // 하단 패딩 조정 (버튼 높이 + 추가 여유 공간)
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ThumbnailSection(
                thumbnailUrl = userDetail.thumbnailUrl,
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColor.current.white)
                    .padding(20.dp)
            ) {
                BasicInfoSection(
                    name = userDetail.name,
                    age = userDetail.age,
                    location = userDetail.location
                )
            }
        }

        if (userTags.isNotEmpty()) { // userDetail.myTags 대신 userTags 사용
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(LocalColor.current.white)
                        .padding(20.dp)
                ) {
                    SectionTitle(title = "マイタグ")
                    MyTagsSection(tags = userTags) // userDetail.myTags 대신 userTags 사용
                }
            }
        }

        if (userDetail.introduction.isNotBlank()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(LocalColor.current.white)
                        .padding(20.dp)
                ) {
                    SectionTitle(title = "自己紹介")
                    IntroductionSection(introduction = userDetail.introduction)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(LocalColor.current.white)
                    .padding(20.dp)
            ) {
                SectionTitle(title = "プロフィール情報")
                ProfileDetailsSection(userDetail = userDetail)
            }
        }
        
        // 기존 Spacer(modifier = Modifier.height(80.dp)) 제거
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
            .aspectRatio(0.7f) // 정사각형 비율
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)) // 모서리 둥글게
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "プロフィールサムネイル",
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
        Text(
            text = name,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        ) // 닉네임 스타일 변경
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${age}歳 $location", // 나이와 거주지 한 줄로 표시
            fontSize = 20.sp
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
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 왼쪽: AsyncImage로 변경
        AsyncImage(
            model = tagImageUrl, // URL 사용
            contentDescription = "タグイメージ: $tagText",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop // 이미지 채우기 방식
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tagText, // tagText 사용
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
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
        ProfilePropertyGroupTitle(title = "基本情報")
        ProfileDetailItem("ニックネーム", userDetail.name)
        ProfileDetailItem("年齢", userDetail.age.toString())
        ProfileDetailItem("性別", if (userDetail.isMale) "男性" else "女性")
        ProfileDetailItem("居住地", userDetail.location)

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "身体情報")
        ProfileDetailItem("血液型", userDetail.bloodType)
        // TODO: UserDetailに키(height)フィールドがあれば追加
        // ProfileDetailItem("키", userDetail.height.toString() + "cm")
        ProfileDetailItem("外見", userDetail.appearance) // 체형에 해당될 수 있음

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "学歴・職歴")
        ProfileDetailItem("学歴", userDetail.education)
        ProfileDetailItem("職種", userDetail.occupation)
        // TODO: UserDetailに年収(annualIncome)フィールドがあれば追加
        // ProfileDetailItem("年収", userDetail.annualIncome)

        Spacer(modifier = Modifier.height(16.dp)) // 그룹 간 간격

        ProfilePropertyGroupTitle(title = "価値観")
        ProfileDetailItem("恋愛観", userDetail.datingPhilosophy)
        ProfileDetailItem("結婚観", userDetail.marriageView)
        if (userDetail.personalityTraits.isNotEmpty()) {
            ProfileDetailItem("性格", userDetail.personalityTraits.joinToString(", "))
        }
        if (userDetail.hobbies.isNotEmpty()){
            ProfileDetailItem("趣味", userDetail.hobbies.joinToString(", "))
        }
        ProfileDetailItem("ライフスタイル", userDetail.lifestyle)
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
                else -> "不明なエラーが発生しました。"
            },
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true, name = "UserDetailContentプレビュー")
@Composable
fun UserDetailContentPreview() {
    val dummyUserDetail = UserDetail(
        id = "dummy_user_123",
        name = "キム・トキ",
        age = 28,
        location = "ソウル",
        thumbnailUrl = "https://images.unsplash.com/photo-1532074205216-d0e1f4b87368?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80",
        introduction = "こんにちは！コーディングと旅行が好きなキム・トキです。一緒に美味しいものを食べたり、面白い話をしながら良い縁を作りたいです。よろしくお願いします！:)",
        isMale = true,
        bloodType = "A型",
        education = "大学卒業",
        occupation = "ソフトウェアエンジニア",
        appearance = "スリムな体型",
        datingPhilosophy = "お互いに良い影響を与える関係",
        marriageView = "時期が来たらしたいです",
        personalityTraits = listOf("ポジティブ", "社交的", "率直", "ユーモラス"),
        hobbies = listOf("コーディング", "Netflix視聴", "グルメ探訪", "海外旅行", "写真撮影"),
        lifestyle = "週末は主に家で休んだり、友達に会ったりします。"
    )

    val dummyUserTags = listOf(
        MainHomeTag(id = "tag1", name = "☕️ カフェ巡り", description = "", imageUrl = "https://images.unsplash.com/photo-1559925393-8be0ec4767c8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 10, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag2", name = "✈️ 自由な海外旅行", description = "", imageUrl = "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 20, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag3", name = "🎬 人生映画探し", description = "", imageUrl = "https://images.unsplash.com/photo-1574267432553-4b4628081c31?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 30, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag4", name = "🏃‍♂️ 週末は登山", description = "", imageUrl = "https://images.unsplash.com/photo-1458442310124-352161d4224d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 40, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag5", name = "📚 月に2冊読書", description = "", imageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 50, categoryId = "c1", tagType = TagType.HOBBY)
    )

    TokitokiTheme {
        UserDetailContent(
            userDetails = listOf(ResultWrapper.Success(dummyUserDetail)),
            userTags = dummyUserTags,
            currentPage = 0,
            isLiked = false,
            isFavorite = false,
            pagerState = rememberPagerState { 1 },
            listState = rememberLazyListState(),
            showFab = true,
            screenName = "FavoriteUsersScreen",
            onBackClick = {},
            onToggleFavorite = {},
            onToggleLike = {},
            onPickupLeftClick = {},
            onPickupRightClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "UserDetailContentプレビュー - タグ/紹介なし")
@Composable
fun UserDetailContentNoTagsPreview() {
    val dummyUserDetail = UserDetail(
        id = "dummy_user_456",
        name = "パク・トキ",
        age = 31,
        location = "釜山",
        thumbnailUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80",
        introduction = "",
        isMale = true,
        bloodType = "B型",
        education = "高校卒業",
        occupation = "フリーランサー",
        appearance = "普通",
        datingPhilosophy = "自由な関係",
        marriageView = "考えていません",
        personalityTraits = listOf("内向的"),
        hobbies = listOf("音楽鑑賞"),
        lifestyle = "静かに過ごす方"
    )

    TokitokiTheme {
        UserDetailContent(
            userDetails = listOf(ResultWrapper.Success(dummyUserDetail)),
            userTags = emptyList(),
            currentPage = 0,
            isLiked = false,
            isFavorite = false,
            pagerState = rememberPagerState { 1 },
            listState = rememberLazyListState(),
            showFab = true,
            screenName = "FavoriteUsersScreen",
            onBackClick = {},
            onToggleFavorite = {},
            onToggleLike = {},
            onPickupLeftClick = {},
            onPickupRightClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "SectionTitleプレビュー")
@Composable
fun SectionTitlePreview() {
    TokitokiTheme {
        SectionTitle(title = "マイタグ")
    }
}

@Preview(showBackground = true, name = "ThumbnailSectionプレビュー")
@Composable
fun ThumbnailSectionPreview() {
    TokitokiTheme {
        ThumbnailSection(thumbnailUrl = "https://images.unsplash.com/photo-1532074205216-d0e1f4b87368?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80")
    }
}

@Preview(showBackground = true, name = "BasicInfoSectionプレビュー")
@Composable
fun BasicInfoSectionPreview() {
    TokitokiTheme {
        BasicInfoSection(name = "キム・トキ", age = 28, location = "ソウル")
    }
}

@Preview(showBackground = true, name = "MyTagsSectionプレビュー")
@Composable
fun MyTagsSectionPreview() {
    val dummyUserTags = listOf(
        MainHomeTag(id = "tag1", name = "☕️ カフェ巡り", description = "", imageUrl = "https://images.unsplash.com/photo-1559925393-8be0ec4767c8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 10, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag2", name = "✈️ 自由な海外旅行", description = "", imageUrl = "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 20, categoryId = "c1", tagType = TagType.HOBBY),
        MainHomeTag(id = "tag3", name = "🎬 人生映画探し", description = "", imageUrl = "https://images.unsplash.com/photo-1574267432553-4b4628081c31?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80", subscriberCount = 30, categoryId = "c1", tagType = TagType.HOBBY)
    )
    TokitokiTheme {
        MyTagsSection(tags = dummyUserTags)
    }
}

@Preview(showBackground = true, name = "MyTagChipプレビュー")
@Composable
fun MyTagChipPreview() {
    TokitokiTheme {
        MyTagChip(
            tagText = "☕️ カフェ巡り",
            tagImageUrl = "https://images.unsplash.com/photo-1559925393-8be0ec4767c8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1941&q=80"
        )
    }
}

@Preview(showBackground = true, name = "IntroductionSectionプレビュー")
@Composable
fun IntroductionSectionPreview() {
    TokitokiTheme {
        IntroductionSection(introduction = "こんにちは！コーディングと旅行が好きなキム・トキです。一緒に美味しいものを食べたり、面白い話をしながら良い縁を作りたいです。よろしくお願いします！:)")
    }
}

@Preview(showBackground = true, name = "ProfileDetailsSectionプレビュー")
@Composable
fun ProfileDetailsSectionPreview() {
    val dummyUserDetail = UserDetail(
        name = "キム・トキ",
        age = 28,
        isMale = true,
        location = "ソウル",
        bloodType = "A型",
        education = "大学卒業",
        occupation = "ソフトウェアエンジニア",
        appearance = "スリムな体型",
        datingPhilosophy = "お互いに良い影響を与える関係",
        marriageView = "時期が来たらしたいです",
        personalityTraits = listOf("ポジティブ", "社交的", "率直", "ユーモラス"),
        hobbies = listOf("コーディング", "Netflix視聴", "グルメ探訪", "海外旅行", "写真撮影"),
        lifestyle = "週末は主に家で休んだり、友達に会ったりします。"
    )
    TokitokiTheme {
        ProfileDetailsSection(userDetail = dummyUserDetail)
    }
}

@Preview(showBackground = true, name = "ProfilePropertyGroupTitleプレビュー")
@Composable
fun ProfilePropertyGroupTitlePreview() {
    TokitokiTheme {
        ProfilePropertyGroupTitle(title = "基本情報")
    }
}

@Preview(showBackground = true, name = "ProfileDetailItemプレビュー")
@Composable
fun ProfileDetailItemPreview() {
    TokitokiTheme {
        ProfileDetailItem(label = "ニックネーム", value = "キム・トキ")
    }
}

// androidx.compose.foundation.layout.ExperimentalLayoutApi 임포트 추가
// import androidx.compose.foundation.layout.ExperimentalLayoutApi
// 주: 이미 파일 상단에 OptIn으로 추가되어 있다면 별도 import 문은 없어도 될 수 있음.
// 하지만 명시적으로 추가하는 것이 좋을 수 있음.
// 실제로는 @OptIn(ExperimentalLayoutApi::class) 어노테이션이 있는 컴포저블 내부에서만 사용 가능.

// 주: 이 파일에서 사용되는 모든 컴포저블에서 필요한 경우 @OptIn(ExperimentalLayoutApi::class) 어노테이션을 추가해야 합니다.
// 이는 해당 컴포저블에서 사용되는 모든 컴포저블에 적용되어야 합니다. 