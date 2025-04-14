package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tokitoki.domain.model.AshiatoTimeline
import com.example.tokitoki.domain.model.AshiatoViewerInfo
import com.example.tokitoki.domain.model.DailyAshiatoLog
import com.example.tokitoki.ui.state.AshiatoUiState
import com.example.tokitoki.ui.viewmodel.AshiatoViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 아시아토 화면의 메인 Composable 함수.
 * ViewModel로부터 상태를 수집하고 Content Composable을 호출합니다.
 */
@Composable
fun AshiatoScreen(
    viewModel: AshiatoViewModel = hiltViewModel(),
    onNavigateToUserProfile: (userId: String) -> Unit // 사용자 프로필 화면 이동 콜백
) {
    val uiState by viewModel.uiState.collectAsState()

    // 실제 UI 레이아웃은 AshiatoContent에 위임
    AshiatoContent(
        uiState = uiState,
        onUserClick = onNavigateToUserProfile, // 클릭 이벤트 전달
        onLoadMore = viewModel::loadMore,      // 더 로드하기 이벤트 전달
        onRefresh = viewModel::refresh         // 새로고침 이벤트 전달 (PullRefresh 라이브러리와 연동 가정)
    )
}

/**
 * 아시아토 화면의 실제 UI 컨텐츠를 표시하는 Composable.
 * 상태 객체와 이벤트 핸들러를 파라미터로 받습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshiatoContent(
    uiState: AshiatoUiState,
    onUserClick: (userId: String) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit // 새로고침 콜백 추가
) {
    val listState = rememberLazyListState() // LazyColumn 스크롤 상태

    // TODO: 여기에 Pull-to-refresh 라이브러리 연동 필요
    // 예: val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isRefreshing, onRefresh = onRefresh)
    // Box(Modifier.fillMaxSize().pullRefresh(pullRefreshState)) { ... }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- 추가된 부분 ---
            // 화면 헤더
            Text(
                text = "足あと", // 아시아토 헤더 텍스트
                style = MaterialTheme.typography.headlineSmall, // 헤더 스타일 적용
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp) // 패딩 추가
            )
            // 구분선
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp), // 좌우 패딩
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant // 구분선 색상
            )
            // --- 추가된 부분 끝 ---

            // 상단 정보 배너
            AshiatoInfoBanner() // 헤더와 배너 사이 간격은 Banner의 패딩으로 조절됨

            // 로딩 상태 처리
            if (uiState.isLoadingInitial) {
                // 초기 로딩 시 중앙에 프로그레스 바 표시
                Box(modifier = Modifier.fillMaxSize().padding(top=50.dp), contentAlignment = Alignment.Center) { // 패딩 추가하여 헤더/배너 피함
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                // 오류 발생 시 오류 메시지 표시 (간단한 Text 예시)
                Box(modifier = Modifier.fillMaxSize().padding(top=50.dp), contentAlignment = Alignment.Center) { // 패딩 추가
                    Text("오류가 발생했습니다: ${uiState.error.localizedMessage ?: "알 수 없는 오류"}")
                    // TODO: 여기에 '다시 시도' 버튼 추가 고려
                }
            } else if (uiState.timeline.dailyLogs.isEmpty()) {
                // 데이터가 없을 때 메시지 표시
                Box(modifier = Modifier.fillMaxSize().padding(top=50.dp), contentAlignment = Alignment.Center) { // 패딩 추가
                    Text("아직 받은 足跡(아시아토)가 없어요.")
                }
            }
            else {
                // 데이터 목록 표시
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // 날짜 섹션 간 간격
                ) {
                    // 날짜별 섹션 구성
                    items(
                        items = uiState.timeline.dailyLogs,
                        key = { it.date } // 각 날짜를 고유 키로 사용
                    ) { dailyLog ->
                        DailyAshiatoSection(
                            dailyLog = dailyLog,
                            onUserClick = onUserClick
                        )
                    }

                    // TODO: 추가 로딩 인디케이터 (필요하다면)
                    // if (uiState.canLoadMore && !uiState.isLoadingInitial) {
                    //     item {
                    //         Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    //             CircularProgressIndicator()
                    //         }
                    //     }
                    // }
                }

                // 무한 스크롤 로직
                InfiniteListHandler(listState = listState) {
                    if (uiState.canLoadMore) { // 더 로드할 수 있을 때만 실행
                        onLoadMore()
                    }
                }
            }
            // TODO: Pull-to-refresh 인디케이터 (라이브러리 사용 시)
            // PullRefreshIndicator(refreshing = uiState.isRefreshing, state = pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

/**
 * 아시아토 화면 상단의 정보 배너 Composable
 */
@Composable
fun AshiatoInfoBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp) // 위쪽 패딩만 적용 (헤더/구분선과 간격)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "정보",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "足あと(아시아토)는, 당신의 프로필을 본 상대가 표시됩니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 특정 날짜의 아시아토 섹션 Composable (날짜 헤더 + 사용자 목록)
 */
@Composable
fun DailyAshiatoSection(
    dailyLog: DailyAshiatoLog,
    onUserClick: (userId: String) -> Unit
) {
    Column {
        // 날짜 헤더
        Text(
            text = formatDateString(dailyLog.date), // 날짜 형식 변환 (예: "10/21 (金)")
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp) // 왼쪽 패딩 추가
        )
        // 사용자 카드 목록 (가로 스크롤)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 카드 간 간격
        ) {
            items(
                items = dailyLog.viewers,
                key = { it.id } // 사용자 ID를 고유 키로 사용
            ) { viewer ->
                AshiatoUserCard(
                    viewerInfo = viewer,
                    onUserClick = onUserClick
                )
            }
        }
    }
}

// 날짜 문자열 형식 변환 함수 (간단 예시)
fun formatDateString(dateStr: String): String {
    return try {
        // 예: 2025-10-21 -> 10/21 (金)
        val date = LocalDate.parse(dateStr)
        // DateTimeFormatter는 앱의 로케일에 맞게 설정 필요
        val formatter = DateTimeFormatter.ofPattern("MM/dd (E)", java.util.Locale.JAPANESE)
        date.format(formatter)
    } catch (e: Exception) {
        dateStr // 파싱 실패 시 원본 반환
    }
}

/**
 * 개별 사용자 카드 Composable
 */
@OptIn(ExperimentalMaterial3Api::class) // Badge 사용을 위해 필요
@Composable
fun AshiatoUserCard(
    viewerInfo: AshiatoViewerInfo,
    onUserClick: (userId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp) // 카드 너비 고정 (디자인에 맞게 조절)
            .clickable { onUserClick(viewerInfo.id) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 이미지 영역 1:1 비율
            ) {
                // 사용자 이미지 (Coil 사용)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(viewerInfo.thumbnailUrl)
                        .crossfade(true)
                        // .placeholder(R.drawable.placeholder) // 플레이스홀더 이미지 리소스
                        // .error(R.drawable.error) // 오류 시 이미지 리소스
                        .build(),
                    contentDescription = "사용자 썸네일",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // 이미지 비율 유지하며 채우기
                )

                // "NEW" 배지 (조건부 표시 - 로직 필요)
                // 여기서는 예시로 항상 표시
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                    containerColor = Color(0xFFFFA500) // 주황색 계열
                ) {
                    Text("NEW", fontSize = 10.sp, color = Color.White)
                }

                // 별 아이콘 (오른쪽 상단)
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "즐겨찾기",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        .padding(4.dp)
                        .size(16.dp)
                )

                // 좋아요 아이콘 (하단 중앙)
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "좋아요",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), CircleShape)
                        .padding(6.dp)
                        .size(20.dp)
                )
            } // Box 끝

            // 사용자 정보 텍스트
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${viewerInfo.age}세 ${viewerInfo.region}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = viewerInfo.viewedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        } // Column 끝
    } // Card 끝
}

/**
 * LazyList의 끝에 도달했는지 감지하여 콜백을 실행하는 Composable 핸들러
 */
@Composable
fun InfiniteListHandler(
    listState: LazyListState,
    buffer: Int = 2, // 리스트 끝에서 몇 개 아이템 전에 로드할지 결정
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // 로드할 아이템이 있고(totalItems > 0),
            // 마지막으로 보이는 아이템 인덱스가 리스트 끝에서 buffer만큼 앞에 도달했고,
            // 현재 스크롤 중이 아닐 때 (선택적: 스크롤 멈췄을 때만 로드)
            totalItems > 0 && lastVisibleItemIndex >= (totalItems - buffer) // && !listState.isScrollInProgress
        }
    }

    LaunchedEffect(loadMore.value) {
        if (loadMore.value) {
            onLoadMore()
        }
    }
}


// ==========================================================================
//  Preview Functions (미리보기 함수)
// ==========================================================================

@Preview(showBackground = true)
@Composable
fun PreviewAshiatoUserCard() {
    val dummyViewer = AshiatoViewerInfo(
        id = "user1",
        thumbnailUrl = "https://placehold.co/150x150/E0E0E0/BDBDBD?text=User",
        age = 26,
        region = "도쿄",
        viewedTime = "10:33"
    )
    MaterialTheme { // Preview에서도 Theme 적용
        AshiatoUserCard(viewerInfo = dummyViewer, onUserClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyAshiatoSection() {
    val dummyViewers = List(5) { index ->
        AshiatoViewerInfo(
            id = "user$index",
            thumbnailUrl = "https://placehold.co/150x150/E0E0E0/BDBDBD?text=User$index",
            age = 20 + index,
            region = if (index % 2 == 0) "오사카" else "후쿠오카",
            viewedTime = "1${index}:00"
        )
    }
    val dummyLog = DailyAshiatoLog(date = "2025-10-21", viewers = dummyViewers)

    MaterialTheme {
        DailyAshiatoSection(dailyLog = dummyLog, onUserClick = {})
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAshiatoContent_Loading() {
    MaterialTheme {
        AshiatoContent(
            uiState = AshiatoUiState(isLoadingInitial = true),
            onUserClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAshiatoContent_Data() {
    val dummyLogs = listOf(
        DailyAshiatoLog(date = "2025-10-21", viewers = List(3) { AshiatoViewerInfo("u1$it", "url", 25+it, "도쿄", "1$it:00") }),
        DailyAshiatoLog(date = "2025-10-20", viewers = List(2) { AshiatoViewerInfo("u2$it", "url", 30+it, "오사카", "0$it:30") })
    )
    MaterialTheme {
        AshiatoContent(
            uiState = AshiatoUiState(
                isLoadingInitial = false,
                timeline = AshiatoTimeline(dailyLogs = dummyLogs),
                canLoadMore = true
            ),
            onUserClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAshiatoContent_Empty() { // 데이터 없을 때 Preview 추가
    MaterialTheme {
        AshiatoContent(
            uiState = AshiatoUiState(
                isLoadingInitial = false,
                timeline = AshiatoTimeline(emptyList()), // 빈 리스트
                canLoadMore = false
            ),
            onUserClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAshiatoContent_Error() {
    MaterialTheme {
        AshiatoContent(
            uiState = AshiatoUiState(
                isLoadingInitial = false,
                error = RuntimeException("네트워크 오류 테스트")
            ),
            onUserClick = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}