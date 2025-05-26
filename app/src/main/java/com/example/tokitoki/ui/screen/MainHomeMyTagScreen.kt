package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.MainHomeTagItemUiState
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.MainHomeMyTagViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.geometry.Offset
import kotlin.math.ceil
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.state.SuggestedTagsUiState

/**
 * 메인 홈 화면의 마이태그 섹션을 표시하는 메인 컴포저블
 * @param viewModel 마이태그 관련 데이터와 로직을 관리하는 ViewModel
 * @param onNavigateToTagSearch 태그 검색 화면으로 이동하는 콜백
 * @param onNavigateToMyTagList 마이태그 목록 화면으로 이동하는 콜백
 */
@Composable
fun MainHomeMyTagScreen(
    viewModel: MainHomeMyTagViewModel = hiltViewModel(),
    onNavigateToTagSearch: () -> Unit = {},
    onNavigateToMyTagList: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val suggestedTagsUiState by viewModel.suggestedTagsUiState.collectAsState()
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

    MainHomeMyTagScreenContent(
        uiState = uiState,
        suggestedTagsUiState = suggestedTagsUiState,
        snackbarHostState = snackbarHostState,
        onLoadMoreSuggestedTags = { viewModel.loadMoreSuggestedTags() },
        onNavigateToTagSearch = onNavigateToTagSearch,
        onNavigateToMyTagList = onNavigateToMyTagList
    )
}

/**
 * 메인 홈 화면의 마이태그 섹션의 실제 UI를 표시하는 컴포저블
 * @param uiState UI 상태
 * @param suggestedTagsUiState 추천 태그 UI 상태
 * @param snackbarHostState 스낵바 호스트 상태
 * @param onLoadMoreSuggestedTags 더 불러오기 클릭 시 호출될 콜백
 * @param onNavigateToTagSearch 태그 검색 화면으로 이동하는 콜백
 * @param onNavigateToMyTagList 마이태그 목록 화면으로 이동하는 콜백
 */
@Composable
fun MainHomeMyTagScreenContent(
    uiState: MainHomeMyTagUiState,
    suggestedTagsUiState: SuggestedTagsUiState,
    snackbarHostState: SnackbarHostState,
    onLoadMoreSuggestedTags: () -> Unit,
    onNavigateToTagSearch: () -> Unit,
    onNavigateToMyTagList: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                item {
                    // 상단 태그 검색 바 (isExpanded == false 일때만)
                    MainHomeMyTagScreen_NormalSearchBar(
                        onSearchBarClicked = onNavigateToTagSearch,
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
                    MainHomeMyTagScreen_MySelectedTags(
                        uiState.myTags,
                        uiState.isLoadingMyTags,
                        onMoreClick = onNavigateToMyTagList
                    )
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
                        isLoading = uiState.isLoadingSuggestedTags,
                        onLoadMore = onLoadMoreSuggestedTags
                    )
                }
            }
        }
    }
}

/**
 * 일반 상태의 검색 바를 표시하는 컴포저블
 * @param onSearchBarClicked 검색 바 클릭 시 호출될 콜백
 * @param modifier 외부에서 전달받은 Modifier
 */
@Composable
fun MainHomeMyTagScreen_NormalSearchBar(
    onSearchBarClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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

        Text(
            text = "興味があるマイタグを検索",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

/**
 * 로딩 효과를 위한 시머 이펙트 컴포저블
 * @param modifier 외부에서 전달받은 Modifier
 */
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

/**
 * 트렌딩 태그용 시머 카드 컴포저블
 * @param modifier 외부에서 전달받은 Modifier
 */
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

/**
 * 오늘의 태그와 트렌딩 태그를 캐러셀 형태로 표시하는 컴포저블
 * @param tags 태그 목록
 * @param isLoading 로딩 상태
 */
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

/**
 * 트렌딩 태그 카드를 표시하는 컴포저블
 * @param tag 태그 정보
 * @param onClick 카드 클릭 시 호출될 콜백
 * @param modifier 외부에서 전달받은 Modifier
 */
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

/**
 * 마이태그 카드를 표시하는 컴포저블
 * @param tag 태그 정보
 * @param onClick 카드 클릭 시 호출될 콜백
 * @param modifier 외부에서 전달받은 Modifier
 */
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

/**
 * 추천 태그 카드를 표시하는 컴포저블
 * @param tag 태그 정보
 * @param onClick 카드 클릭 시 호출될 콜백
 */
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

/**
 * 내가 선택한 태그들을 표시하는 컴포저블
 * @param myTags 선택된 태그 목록
 * @param isLoading 로딩 상태
 * @param onMoreClick 'もっと見る' 클릭 시 호출될 콜백
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MainHomeMyTagScreen_MySelectedTags(
    myTags: List<MainHomeTagItemUiState>,
    isLoading: Boolean = false,
    onMoreClick: () -> Unit = {}
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
                color = Color.Gray,
                modifier = Modifier.clickable { onMoreClick() }
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

/**
 * 프로모션 배너를 표시하는 컴포저블
 * @param imageUrl 배너 이미지 URL
 * @param onClick 배너 클릭 시 호출될 콜백
 */
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

/**
 * 추천 태그 섹션을 표시하는 컴포저블
 * @param suggestedTags 추천 태그 목록
 * @param canLoadMore 더 불러올 수 있는지 여부
 * @param isLoading 로딩 상태
 * @param onLoadMore 더 불러오기 클릭 시 호출될 콜백
 */
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
                if (isLoading) {
                    // Show 6 shimmer cards (2 rows x 3 columns)
                    items(6) {
                        SuggestedTagShimmerCard()
                    }
                    
                    // Show shimmer for "more" button
                    item(
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(24.dp)
                            )
                        }
                    }
                } else {
                    items(items = suggestedTags) { tag ->
                        MainHomeMyTagScreen_SuggestedTagCard(tag)
                    }

                    if (canLoadMore && !isLoading) {
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
}

/**
 * 마이태그 시머 카드를 표시하는 컴포저블
 * @param modifier 외부에서 전달받은 Modifier
 */
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

/**
 * 추천 태그 시머 카드를 표시하는 컴포저블
 * @param modifier 외부에서 전달받은 Modifier
 */
@Composable
fun SuggestedTagShimmerCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(100.dp)
            .wrapContentHeight()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Shimmer for image
        ShimmerEffect(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Shimmer for tag name
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        // Shimmer for subscriber count
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreenPreview() {
    TokitokiTheme {
        MainHomeMyTagScreenContent(
            uiState = MainHomeMyTagUiState(
                todayTag = MainHomeTagItemUiState(
                    id = "1",
                    name = "今日のタグ",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 1000,
                    description = "",
                    categoryId = "1",
                    tagType = TagType.HOBBY
                ),
                trendingTags = listOf(
                    MainHomeTagItemUiState(
                        id = "2",
                        name = "トレンドタグ1",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 800,
                        description = "",
                        categoryId = "1",
                        tagType = TagType.LIFESTYLE
                    ),
                    MainHomeTagItemUiState(
                        id = "3",
                        name = "トレンドタグ2",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 600,
                        description = "",
                        categoryId = "1",
                        tagType = TagType.VALUE
                    )
                ),
                myTags = listOf(
                    MainHomeTagItemUiState(
                        id = "4",
                        name = "マイタグ1",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 400,
                        description = "",
                        categoryId = "2",
                        tagType = TagType.HOBBY
                    ),
                    MainHomeTagItemUiState(
                        id = "5",
                        name = "マイタグ2",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 300,
                        description = "",
                        categoryId = "2",
                        tagType = TagType.LIFESTYLE
                    )
                ),
                isLoadingTodayAndTrending = false,
                isLoadingMyTags = false,
                isLoadingSuggestedTags = false
            ),
            suggestedTagsUiState = SuggestedTagsUiState(
                tags = listOf(
                    MainHomeTagItemUiState(
                        id = "6",
                        name = "おすすめタグ1",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 200,
                        description = "",
                        categoryId = "3",
                        tagType = TagType.VALUE
                    ),
                    MainHomeTagItemUiState(
                        id = "7",
                        name = "おすすめタグ2",
                        imageUrl = "https://picsum.photos/200/200",
                        subscriberCount = 150,
                        description = "",
                        categoryId = "3",
                        tagType = TagType.HOBBY
                    )
                ),
                canLoadMore = true
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onLoadMoreSuggestedTags = {},
            onNavigateToTagSearch = {},
            onNavigateToMyTagList = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreenLoadingPreview() {
    TokitokiTheme {
        MainHomeMyTagScreenContent(
            uiState = MainHomeMyTagUiState(
                isLoadingTodayAndTrending = true,
                isLoadingMyTags = true,
                isLoadingSuggestedTags = true
            ),
            suggestedTagsUiState = SuggestedTagsUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onLoadMoreSuggestedTags = {},
            onNavigateToTagSearch = {},
            onNavigateToMyTagList = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreenEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreenContent(
            uiState = MainHomeMyTagUiState(),
            suggestedTagsUiState = SuggestedTagsUiState(),
            snackbarHostState = remember { SnackbarHostState() },
            onLoadMoreSuggestedTags = {},
            onNavigateToTagSearch = {},
            onNavigateToMyTagList = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_NormalSearchBarPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_NormalSearchBar(
            onSearchBarClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TodayAndTrendingTags(
            tags = listOf(
                MainHomeTagItemUiState(
                    id = "1",
                    name = "今日のタグ",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 1000,
                    description = "",
                    categoryId = "1",
                    tagType = TagType.HOBBY
                ),
                MainHomeTagItemUiState(
                    id = "2",
                    name = "トレンドタグ1",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 800,
                    description = "",
                    categoryId = "1",
                    tagType = TagType.LIFESTYLE
                )
            ),
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTagsLoadingPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TodayAndTrendingTags(
            tags = emptyList(),
            isLoading = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_MySelectedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MySelectedTags(
            myTags = listOf(
                MainHomeTagItemUiState(
                    id = "1",
                    name = "マイタグ1",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 400,
                    description = "",
                    categoryId = "2",
                    tagType = TagType.HOBBY
                ),
                MainHomeTagItemUiState(
                    id = "2",
                    name = "マイタグ2",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 300,
                    description = "",
                    categoryId = "2",
                    tagType = TagType.LIFESTYLE
                )
            ),
            isLoading = false,
            onMoreClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_MySelectedTagsLoadingPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MySelectedTags(
            myTags = emptyList(),
            isLoading = true,
            onMoreClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_MySelectedTagsEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MySelectedTags(
            myTags = emptyList(),
            isLoading = false,
            onMoreClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_PromotionBannerPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_PromotionBanner(
            imageUrl = "https://picsum.photos/400/100",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_SuggestedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTags(
            suggestedTags = listOf(
                MainHomeTagItemUiState(
                    id = "1",
                    name = "おすすめタグ1",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 200,
                    description = "",
                    categoryId = "3",
                    tagType = TagType.VALUE
                ),
                MainHomeTagItemUiState(
                    id = "2",
                    name = "おすすめタグ2",
                    imageUrl = "https://picsum.photos/200/200",
                    subscriberCount = 150,
                    description = "",
                    categoryId = "3",
                    tagType = TagType.HOBBY
                )
            ),
            canLoadMore = true,
            isLoading = false,
            onLoadMore = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_SuggestedTagsLoadingPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTags(
            suggestedTags = emptyList(),
            canLoadMore = false,
            isLoading = true,
            onLoadMore = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_SuggestedTagsEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTags(
            suggestedTags = emptyList(),
            canLoadMore = false,
            isLoading = false,
            onLoadMore = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_TrendingTagCardPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TrendingTagCard(
            tag = MainHomeTagItemUiState(
                id = "1",
                name = "トレンドタグ",
                imageUrl = "https://picsum.photos/200/200",
                subscriberCount = 1000,
                description = "",
                categoryId = "1",
                tagType = TagType.HOBBY
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_MyTagCardPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MyTagCard(
            tag = MainHomeTagItemUiState(
                id = "1",
                name = "マイタグ",
                imageUrl = "https://picsum.photos/200/200",
                subscriberCount = 500,
                description = "",
                categoryId = "2",
                tagType = TagType.LIFESTYLE
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainHomeMyTagScreen_SuggestedTagCardPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTagCard(
            tag = MainHomeTagItemUiState(
                id = "1",
                name = "おすすめタグ",
                imageUrl = "https://picsum.photos/200/200",
                subscriberCount = 300,
                description = "",
                categoryId = "3",
                tagType = TagType.VALUE
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerEffectPreview() {
    TokitokiTheme {
        ShimmerEffect(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrendingTagShimmerCardPreview() {
    TokitokiTheme {
        TrendingTagShimmerCard()
    }
}

@Preview(showBackground = true)
@Composable
fun MyTagShimmerCardPreview() {
    TokitokiTheme {
        MyTagShimmerCard()
    }
}

@Preview(showBackground = true)
@Composable
fun SuggestedTagShimmerCardPreview() {
    TokitokiTheme {
        SuggestedTagShimmerCard()
    }
}
