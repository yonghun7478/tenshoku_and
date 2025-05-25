package com.example.tokitoki.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.MainHomeTagItemUiState
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.MainHomeMyTagViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.TextButton
import androidx.compose.ui.geometry.Offset
import kotlin.math.ceil
import androidx.compose.foundation.interaction.MutableInteractionSource


@Composable
fun MainHomeMyTagScreen(viewModel: MainHomeMyTagViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val suggestedTagsUiState by viewModel.suggestedTagsUiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val snackbarHostState = remember { SnackbarHostState() }

    // 스낵바 메시지 처리
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbarMessage()
        }
    }

    // BackHandler 추가: 물리적 뒤로 가기 버튼 처리
    BackHandler(enabled = isExpanded) {
        viewModel.restoreSelectedTags()
        viewModel.clearSearchQuery()
        viewModel.clearSearchResult()
        isExpanded = false // isExpanded를 false로 설정하여 검색창 닫기
    }

    // LaunchedEffect 추가: isExpanded가 true로 바뀔 때마다 loadRecentSearches 호출
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            viewModel.loadRecentSearches()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 원래의 컨텐츠 (LazyColumn)
            AnimatedVisibility(
                visible = !isExpanded, // isExpanded가 false일 때만 보임
                enter = fadeIn(),
                exit = fadeOut()
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        // 상단 태그 검색 바 (isExpanded == false 일때만)
                        MainHomeMyTagScreen_NormalSearchBar(
                            selectedTags = uiState.selectedTags,
                            onSearchBarClicked = {
                                viewModel.saveSelectedTags()
                                viewModel.clearSearchQuery()
                                viewModel.clearSearchResult()
                                isExpanded = true
                            },
                        )
                        Divider()
                    }

                    item {
                        // 오늘의 태그 & 트렌딩 태그
                        MainHomeMyTagScreen_TodayAndTrendingTags(
                            listOfNotNull(uiState.todayTag) + uiState.trendingTags,
                            uiState.isLoadingTodayAndTrending
                        )
                        Divider()
                    }
                    item {
                        // 내가 선택한 태그
                        MainHomeMyTagScreen_MySelectedTags(uiState.myTags, uiState.isLoadingMyTags)
                        Divider()
                    }

                    item {
                        // 프로모션 배너 (임시)
                        MainHomeMyTagScreen_PromotionBanner(
                            imageUrl = "https://picsum.photos/400/100",
                            onClick = { /* TODO: Handle banner click */ }
                        )
                        Divider()
                    }
                    item {
                        // 새로운 태그 추천
                        MainHomeMyTagScreen_SuggestedTags(
                            suggestedTags = suggestedTagsUiState.tags,
                            canLoadMore = suggestedTagsUiState.canLoadMore,
                            isLoading = suggestedTagsUiState.isLoading,
                            onLoadMore = {
                                viewModel.loadMoreSuggestedTags()
                            }
                        )
                    }
                }
            }

            // 확장된 검색 바 화면 (전체 화면, isExpanded == true 일때만)
            AnimatedVisibility(
                visible = isExpanded, // isExpanded가 true일 때만 보임
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) { // 배경색 추가

                    MainHomeMyTagScreen_ExpandedSearchBar(
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChanged = { query -> viewModel.onTagSearchQueryChanged(query) },
                        focusRequester = focusRequester,
                        onSearchPerformed = {  // 추가
                            viewModel.onSearchPerformed()
                            viewModel.clearSearchQuery()
                            viewModel.clearSearchResult()
                            isExpanded = false
                        },
                        onBackButtonClicked = {
                            viewModel.restoreSelectedTags()
                            viewModel.clearSearchQuery()
                            viewModel.clearSearchResult()
                            isExpanded = false
                        } // 추가
                    )

                    MainHomeMyTagScreen_ExpandedSearchContent(
                        searchQuery = uiState.searchQuery,
                        recentSearches = uiState.recentSearches,
                        trendingTags = uiState.trendingTags,
                        searchResults = uiState.searchResults,
                        selectedTags = uiState.selectedTags,
                        onTagSelected = { tag -> viewModel.onTagSelected(tag) },
                        onTagRemoved = { tag -> viewModel.onTagRemoved(tag) },
                        isVisible = true // AnimatedVisibility 안에 있으므로 항상 true
                    )
                }
            }
        }
    }
}

// 검색 바 (일반 상태)
@Composable
fun MainHomeMyTagScreen_NormalSearchBar(
    selectedTags: List<MainHomeTagItemUiState>, // 변경
    onSearchBarClicked: () -> Unit,
    modifier: Modifier = Modifier, // 추가: 외부에서 Modifier를 받을 수 있도록
) {
    Row(
        modifier = modifier // 외부에서 Modifier 적용
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onSearchBarClicked() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        if (selectedTags.isEmpty()) {
            Text(
                text = "興味があるマイタグを検索",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            MainHomeMyTagScreen_SelectedTagsRow(
                // 변경
                selectedTags = selectedTags,
            )
        }
    }
}

// 검색 바 (확장 상태)
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    onSearchPerformed: () -> Unit,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                cursorBrush = SolidColor(Color.Black),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "興味があるマイタグを検索",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onSearchPerformed) {
                Text("検索")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onBackButtonClicked) {
                Text("戻る")
            }
        }
    }
}

// 선택된 태그들을 보여주는 가로 스크롤 리스트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainHomeMyTagScreen_SelectedTagsRow(
    selectedTags: List<MainHomeTagItemUiState>, // 변경
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),

        ) {
        selectedTags.forEach { tag ->
            MainHomeMyTagScreen_TagChip( // 변경
                tag = tag,
                isRemovable = false
            )
        }
    }
}

// 확장된 검색 바의 내용 (최근 검색, 급상승 태그, 검색 결과)
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContent(
    searchQuery: String,
    recentSearches: List<MainHomeTagItemUiState>,
    trendingTags: List<MainHomeTagItemUiState>,
    searchResults: List<MainHomeTagItemUiState>,
    selectedTags: List<MainHomeTagItemUiState>,
    onTagSelected: (MainHomeTagItemUiState) -> Unit,
    onTagRemoved: (MainHomeTagItemUiState) -> Unit,
    isVisible: Boolean
) {
    AnimatedVisibility(isVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            MainHomeMyTagScreen_TagSection(
                title = "選択したタグ",
                tags = selectedTags,
                onTagClick = onTagRemoved,
                isRemovable = true
            )

            MainHomeMyTagScreen_TagSection(
                title = "検索結果",
                tags = searchResults,
                onTagClick = onTagSelected
            )

            if (searchQuery.isBlank()) {
                MainHomeMyTagScreen_TagSection(
                    title = "最近の検索",
                    tags = recentSearches,
                    onTagClick = onTagSelected
                )

                MainHomeMyTagScreen_TagSection(
                    title = "人気のタグ",
                    tags = trendingTags,
                    onTagClick = onTagSelected
                )
            }
        }
    }
}

// 태그 섹션 (제목 + 칩 목록)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainHomeMyTagScreen_TagSection(
    title: String,
    tags: List<MainHomeTagItemUiState>,
    onTagClick: (MainHomeTagItemUiState) -> Unit,
    isRemovable: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (tags.isEmpty()) {
            Text(
                text = "なし",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3
            ) {
                tags.forEach { tag ->
                    MainHomeMyTagScreen_TagChip(
                        tag = tag,
                        onTagClick = {
                            onTagClick(tag)
                        },
                        isRemovable = isRemovable
                    )
                }
            }
        }
    }
}

@Composable
fun MainHomeMyTagScreen_TagChip(
    tag: MainHomeTagItemUiState,
    onTagClick: (() -> Unit)? = null, // Optional<() -> Unit>으로 변경,  null 허용, 기본값 null
    isRemovable: Boolean = false
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isRemovable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 4.dp)
                .then(if (onTagClick != null) Modifier.clickable(onClick = onTagClick) else Modifier) // 조건부 clickable
        ) {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = if (isRemovable) 0.dp else 8.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
            )
            if (isRemovable) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .then(if (onTagClick != null) Modifier.clickable(onClick = onTagClick) else Modifier), // 조건부 clickable
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "X",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Shimmer effect composable
@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}

// Shimmer card for trending tags
@Composable
fun TrendingTagShimmerCard(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shimmer for image
            ShimmerEffect(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Shimmer for text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(16.dp)
                )
            }
        }
    }
}

// 오늘의 태그 & 트렌딩 태그 (Carousel)
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTags(
    tags: List<MainHomeTagItemUiState>,
    isLoading: Boolean = false
) {
    // pageCount를 tags.size로 직접 지정
    val pagerState = rememberPagerState(initialPage = 0) {
        if (isLoading) 3 else tags.size
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "今日のタグ & 人気のタグ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // 고정된 높이 설정
        ) {
            if (isLoading) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 40.dp),
                    pageSpacing = 15.dp
                ) { page ->
                    TrendingTagShimmerCard()
                }
            } else if (tags.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 40.dp),
                    pageSpacing = 15.dp
                ) { page ->
                    val tag = tags[page]
                    MainHomeMyTagScreen_TrendingTagCard(
                        tag = tag
                    )
                }
            } else {
                Text(
                    text = "利用可能なタグがありません。",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 페이지 표시기 - 높이 유지하면서 조건부 표시
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (!isLoading && tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(tags.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Page Indicator",
                                tint = color,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 오늘의 태그 및 인기 태그용 카드
@Composable
fun MainHomeMyTagScreen_TrendingTagCard(
    tag: MainHomeTagItemUiState,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isChecked by remember { mutableStateOf(false) }
    val viewModel: MainHomeMyTagViewModel = hiltViewModel()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 썸네일 이미지 (왼쪽)
                AsyncImage(
                    model = tag.imageUrl,
                    contentDescription = "Tag Image",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.no_image_icon)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 태그 정보 (오른쪽)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = tag.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Subscriber Icon",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${tag.subscriberCount}人",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 우측 중앙 동그란 버튼
            Surface(
                shape = CircleShape,
                color = if (isChecked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
                    .absoluteOffset(x = (-16).dp)
                    .clickable { 
                        isChecked = !isChecked
                        viewModel.onTagToggle(tag, isChecked)
                    }
            ) {
                Icon(
                    imageVector = if (isChecked) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (isChecked) "Tag Added" else "Add Tag",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp)
                )
            }
        }
    }
}

// 마이태그용 카드
@Composable
fun MainHomeMyTagScreen_MyTagCard(
    tag: MainHomeTagItemUiState,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 섬네일 (왼쪽)
            AsyncImage(
                model = tag.imageUrl,
                contentDescription = "Tag Image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.no_image_icon)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 태그 정보 (오른쪽)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Subscriber Icon",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${tag.subscriberCount}人",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// 추천 태그용 카드
@Composable
fun MainHomeMyTagScreen_SuggestedTagCard(
    tag: MainHomeTagItemUiState,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = Modifier
            .width(100.dp)
            .wrapContentHeight()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 섬네일 (1:1 비율)
            AsyncImage(
                model = tag.imageUrl,
                contentDescription = "Tag Image",
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.no_image_icon)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 태그 이름
            Text(
                text = tag.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            // 구독자 수 (아이콘 + 텍스트)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Subscriber Icon",
                    modifier = Modifier.size(12.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${tag.subscriberCount}人",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// 내가 선택한 태그
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MainHomeMyTagScreen_MySelectedTags(
    myTags: List<MainHomeTagItemUiState>,
    isLoading: Boolean = false
) {
    val itemsPerPage = 4
    val maxPages = 3
    val totalItems = minOf(myTags.size, itemsPerPage * maxPages)
    val pageCount = ceil(totalItems.toFloat() / itemsPerPage).toInt().coerceAtLeast(1)
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "マイタグ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "もっと見る",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(328.dp) // 1열 4행 기준 고정 높이
        ) {
            if (isLoading) {
                // 로딩 중일 때 시머 이펙트 표시
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 16.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { page ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(itemsPerPage) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp)
                            ) {
                                MyTagShimmerCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                )
                            }
                        }
                    }
                }
            } else if (myTags.isEmpty()) {
                // 로딩이 끝났고 아이템이 없을 때
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "プロフィールでタグを選択してください。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                // 로딩이 끝났고 아이템이 있을 때
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 16.dp,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) { page ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val startIndex = page * itemsPerPage
                        val endIndex = minOf(startIndex + itemsPerPage, totalItems)
                        for (index in startIndex until endIndex) {
                            val tag = myTags[index]
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(72.dp)
                            ) {
                                MainHomeMyTagScreen_MyTagCard(
                                    tag = tag,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                )
                            }
                        }
                        // 아이템이 4개 미만일 때 빈 공간 채우기
                        repeat(itemsPerPage - (endIndex - startIndex)) {
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Page Indicator",
                        tint = color,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
        }
    }
}

// 프로모션 배너
@Composable
fun MainHomeMyTagScreen_PromotionBanner(
    imageUrl: String = "https://picsum.photos/400/100",
    onClick: () -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Promotion Banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.no_image_icon)
    )
}

// 새로운 태그 추천
@Composable
fun MainHomeMyTagScreen_SuggestedTags(
    suggestedTags: List<MainHomeTagItemUiState>,
    canLoadMore: Boolean = false,
    isLoading: Boolean = false,
    onLoadMore: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "おすすめタグ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = suggestedTags) { tag ->
                    MainHomeMyTagScreen_SuggestedTagCard(tag)
                }

                if (!isLoading && canLoadMore) {
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onLoadMore() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "もっと見る",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

// 1. MainHomeMyTagScreen_NormalSearchBar Preview
@Preview(showBackground = true, name = "Normal Search Bar (Empty)")
@Composable
fun MainHomeMyTagScreen_NormalSearchBarEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_NormalSearchBar(
            selectedTags = listOf(), // 빈 리스트
            onSearchBarClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Normal Search Bar (With Tags)")
@Composable
fun MainHomeMyTagScreen_NormalSearchBarWithTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_NormalSearchBar(
            selectedTags = listOf(
                MainHomeTagItemUiState(
                    id = "tag_1",
                    name = "태그1",
                    description = "태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 10
                ),
                MainHomeTagItemUiState(
                    id = "tag_2",
                    name = "태그2",
                    description = "태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 25
                )
            ),
            onSearchBarClicked = {}
        )
    }
}

// 2. MainHomeMyTagScreen_ExpandedSearchBar Preview
@Preview(showBackground = true, name = "Expanded Search Bar (Empty)")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBarEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchBar(
            searchQuery = "", // 빈 검색어
            onSearchQueryChanged = {},
            focusRequester = FocusRequester(),
            onSearchPerformed = {},
            onBackButtonClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Bar (With Text)")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBarWithTextPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchBar(
            searchQuery = "검색 중...", // 검색어 입력
            onSearchQueryChanged = {},
            focusRequester = FocusRequester(),
            onSearchPerformed = {},
            onBackButtonClicked = {}
        )
    }
}

// 4. MainHomeMyTagScreen_SelectedTagsRow Preview
@Preview(showBackground = true, name = "Selected Tags Row")
@Composable
fun MainHomeMyTagScreen_SelectedTagsRowPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SelectedTagsRow(
            selectedTags = listOf(
                MainHomeTagItemUiState(
                    id = "tag_1",
                    name = "태그1",
                    description = "태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 10
                ),
                MainHomeTagItemUiState(
                    id = "tag_2",
                    name = "태그2",
                    description = "태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 25
                ),
                MainHomeTagItemUiState(
                    id = "tag_3",
                    name = "태그3",
                    description = "태그3 설명",
                    imageUrl = "image3",
                    subscriberCount = 5
                )
            )
        )
    }
}

// 5. MainHomeMyTagScreen_ExpandedSearchContent Preview:  검색어 없는 경우 (최근, 급상승) + 있는 경우.
@Preview(showBackground = true, name = "Expanded Search Content Preview")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContentPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchContent(
            searchQuery = "",
            recentSearches = listOf(
                MainHomeTagItemUiState(
                    id = "recent_1",
                    name = "최근검색1",
                    description = "최근검색1 설명",
                    imageUrl = "recent1",
                    subscriberCount = 1
                ),
                MainHomeTagItemUiState(
                    id = "recent_2",
                    name = "최근검색2",
                    description = "최근검색2 설명",
                    imageUrl = "recent2",
                    subscriberCount = 2
                )
            ),
            trendingTags = listOf(
                MainHomeTagItemUiState(
                    id = "trend_1",
                    name = "트렌딩 태그1",
                    description = "트렌딩 태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 50
                ),
                MainHomeTagItemUiState(
                    id = "trend_2",
                    name = "트렌딩 태그2",
                    description = "트렌딩 태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 120
                ),
                MainHomeTagItemUiState(
                    id = "trend_3",
                    name = "트렌딩 태그3",
                    description = "트렌딩 태그3 설명",
                    imageUrl = "image3",
                    subscriberCount = 80
                )
            ),
            searchResults = listOf(
                MainHomeTagItemUiState(
                    id = "search_1",
                    name = "검색결과1",
                    description = "검색결과1 설명",
                    imageUrl = "search_result_image1",
                    subscriberCount = 10
                ),
                MainHomeTagItemUiState(
                    id = "search_2",
                    name = "검색결과2",
                    description = "검색결과2 설명",
                    imageUrl = "search_result_image2",
                    subscriberCount = 20
                )
            ),
            selectedTags = listOf(
                MainHomeTagItemUiState(
                    id = "selected_1",
                    name = "선택된태그1",
                    description = "선택된태그1 설명",
                    imageUrl = "selected_image1",
                    subscriberCount = 100
                ),
                MainHomeTagItemUiState(
                    id = "selected_2",
                    name = "선택된태그2",
                    description = "선택된태그2 설명",
                    imageUrl = "selected_image2",
                    subscriberCount = 200
                )
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Content with Query")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContentSearchQueryPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchContent(
            searchQuery = "검색어있음",
            recentSearches = listOf(
                MainHomeTagItemUiState(
                    id = "recent_1",
                    name = "최근검색1",
                    description = "최근검색1 설명",
                    imageUrl = "recent1",
                    subscriberCount = 1
                ),
                MainHomeTagItemUiState(
                    id = "recent_2",
                    name = "최근검색2",
                    description = "최근검색2 설명",
                    imageUrl = "recent2",
                    subscriberCount = 2
                )
            ),
            trendingTags = listOf(
                MainHomeTagItemUiState(
                    id = "trend_1",
                    name = "트렌딩 태그1",
                    description = "트렌딩 태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 50
                ),
                MainHomeTagItemUiState(
                    id = "trend_2",
                    name = "트렌딩 태그2",
                    description = "트렌딩 태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 120
                ),
                MainHomeTagItemUiState(
                    id = "trend_3",
                    name = "트렌딩 태그3",
                    description = "트렌딩 태그3 설명",
                    imageUrl = "image3",
                    subscriberCount = 80
                )
            ),
            searchResults = listOf(
                MainHomeTagItemUiState(
                    id = "search_1",
                    name = "검색결과1",
                    description = "검색결과1 설명",
                    imageUrl = "search_result_image1",
                    subscriberCount = 10
                ),
                MainHomeTagItemUiState(
                    id = "search_2",
                    name = "검색결과2",
                    description = "검색결과2 설명",
                    imageUrl = "search_result_image2",
                    subscriberCount = 20
                )
            ),
            selectedTags = listOf(
                MainHomeTagItemUiState(
                    id = "selected_1",
                    name = "선택된태그1",
                    description = "선택된태그1 설명",
                    imageUrl = "selected_image1",
                    subscriberCount = 100
                ),
                MainHomeTagItemUiState(
                    id = "selected_2",
                    name = "선택된태그2",
                    description = "선택된태그2 설명",
                    imageUrl = "selected_image2",
                    subscriberCount = 200
                )
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}

// 6. MainHomeMyTagScreen_TagSection:  isRemovable = false 만. (true는 Chip에서 확인)
@Preview(showBackground = true, name = "Tag Section Preview")
@Composable
fun MainHomeMyTagScreen_TagSectionPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagSection(
            title = "섹션 제목",
            tags = listOf(
                MainHomeTagItemUiState(
                    id = "section_1",
                    name = "태그1",
                    description = "태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 10
                ),
                MainHomeTagItemUiState(
                    id = "section_2",
                    name = "태그2",
                    description = "태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 20
                ),
                MainHomeTagItemUiState(
                    id = "section_3",
                    name = "태그3",
                    description = "태그3 설명",
                    imageUrl = "image3",
                    subscriberCount = 30
                )
            ),
            onTagClick = {},
            isRemovable = false
        )
    }
}

// 8. MainHomeMyTagScreen_TagChip: isRemovable = true, false  2가지
@Preview(showBackground = true, name = "Tag Chip Preview")
@Composable
fun MainHomeMyTagScreen_TagChipPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagChip(
            tag = MainHomeTagItemUiState(
                id = "preview_1",
                name = "태그",
                description = "태그 설명",
                imageUrl = "image1",
                subscriberCount = 10
            ),
            onTagClick = {},
            isRemovable = true
        )
    }
}

@Preview(showBackground = true, name = "Tag Chip Not Removable")
@Composable
fun MainHomeMyTagScreen_TagChipNotRemovablePreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagChip(
            tag = MainHomeTagItemUiState(
                id = "preview_2",
                name = "태그",
                description = "태그 설명",
                imageUrl = "image1",
                subscriberCount = 10
            ),
            onTagClick = {},
            isRemovable = false
        )
    }
}

@Preview(showBackground = true, name = "Carousel Preview")
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TodayAndTrendingTags(
            tags = listOf(
                MainHomeTagItemUiState(
                    id = "today_1",
                    name = "오늘의 태그",
                    description = "오늘의 태그 설명",
                    imageUrl = "",
                    subscriberCount = 100
                ),
                MainHomeTagItemUiState(
                    id = "trend_1",
                    name = "트렌딩 태그1",
                    description = "트렌딩 태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 50
                ),
                MainHomeTagItemUiState(
                    id = "trend_2",
                    name = "트렌딩 태그2",
                    description = "트렌딩 태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 120
                ),
                MainHomeTagItemUiState(
                    id = "trend_3",
                    name = "트렌딩 태그3",
                    description = "트렌딩 태그3 설명",
                    imageUrl = "image3",
                    subscriberCount = 80
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "My Selected Tags Preview")
@Composable
fun MainHomeMyTagScreen_MySelectedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MySelectedTags(
            myTags = listOf(
                MainHomeTagItemUiState(
                    id = "my_1",
                    name = "선택한 태그1",
                    description = "선택한 태그1 설명",
                    imageUrl = "",
                    subscriberCount = 30
                ),
                MainHomeTagItemUiState(
                    id = "my_2",
                    name = "선택한 태그2",
                    description = "선택한 태그2 설명",
                    imageUrl = "",
                    subscriberCount = 45
                ),
                MainHomeTagItemUiState(
                    id = "my_3",
                    name = "선택한 태그3",
                    description = "선택한 태그3 설명",
                    imageUrl = "",
                    subscriberCount = 60
                ),
                MainHomeTagItemUiState(
                    id = "my_4",
                    name = "선택한 태그4",
                    description = "선택한 태그4 설명",
                    imageUrl = "",
                    subscriberCount = 22
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "Promotion Banner Preview")
@Composable
fun MainHomeMyTagScreen_PromotionBannerPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_PromotionBanner(
            imageUrl = "https://picsum.photos/400/100",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Suggested Tags Preview")
@Composable
fun MainHomeMyTagScreen_SuggestedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTags(
            suggestedTags = listOf(
                MainHomeTagItemUiState(
                    id = "suggest_1",
                    name = "추천 태그1",
                    description = "추천 태그1 설명",
                    imageUrl = "image1",
                    subscriberCount = 15
                ),
                MainHomeTagItemUiState(
                    id = "suggest_2",
                    name = "추천 태그2",
                    description = "추천 태그2 설명",
                    imageUrl = "image2",
                    subscriberCount = 33
                )
            )
        )
    }
}

@Composable
fun MyTagShimmerCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shimmer for image
        ShimmerEffect(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Shimmer for text content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
            )
        }
    }
}
