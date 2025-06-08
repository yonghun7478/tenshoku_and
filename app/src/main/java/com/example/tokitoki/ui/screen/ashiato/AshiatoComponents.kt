package com.example.tokitoki.ui.screen.ashiato

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tokitoki.domain.model.AshiatoViewerInfo
import com.example.tokitoki.domain.model.DailyAshiatoLog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.tokitoki.domain.model.AshiatoTimeline

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
    onUserClick: (date: String, userId: String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
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
                    date = dailyLog.date,
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
    date: String,
    viewerInfo: AshiatoViewerInfo,
    onUserClick: (date: String, userId: String) -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp) // 카드 너비 고정 (디자인에 맞게 조절)
            .clickable { onUserClick(date, viewerInfo.id) },
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
                        .align(Alignment.BottomEnd)
                        .padding(all = 6.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            CircleShape
                        )
                        .padding(6.dp)
                        .size(16.dp)
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

            totalItems > 0 && lastVisibleItemIndex >= (totalItems - buffer)
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
        AshiatoUserCard(date = "", viewerInfo = dummyViewer, onUserClick = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyAshiatoSection() {
    val dummyViewers = List(5) { index ->
        AshiatoViewerInfo(
            id = "user$index",
            thumbnailUrl = "https.co/150x150/E0E0E0/BDBDBD?text=User$index",
            age = 20 + index,
            region = if (index % 2 == 0) "오사카" else "후쿠오카",
            viewedTime = "1${index}:00"
        )
    }
    val dummyLog = DailyAshiatoLog(date = "2025-10-21", viewers = dummyViewers)

    MaterialTheme {
        DailyAshiatoSection(dailyLog = dummyLog, onUserClick = { _, _ -> })
    }
} 