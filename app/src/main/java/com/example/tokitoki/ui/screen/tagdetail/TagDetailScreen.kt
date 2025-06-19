package com.example.tokitoki.ui.screen.tagdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.User
import com.example.tokitoki.ui.state.TagDetailUiState
import com.example.tokitoki.ui.viewmodel.TagDetailViewModel
import androidx.compose.material.icons.filled.ArrowBack
import com.example.tokitoki.ui.theme.TokitokiTheme

@Composable
fun TagDetailScreen(
    tagId: String,
    onNavigateUp: () -> Unit = {},
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    viewModel: TagDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tagId) {
        viewModel.loadTagDetail(tagId)
    }

    TagDetailScreenContents(
        uiState = uiState,
        onLoadMoreSubscribers = { viewModel.loadMoreSubscribers(tagId) },
        onSubscriptionToggle = { isSubscribed, tagType ->
            uiState.tag?.let { tag ->
                viewModel.toggleSubscription(tagId, isSubscribed, tagType)
            }
        },
        onNavigateUp = onNavigateUp,
        onNavigateToUserDetail = { userId, screenName ->
            viewModel.addUserIdsToCache()
            onNavigateToUserDetail(userId, screenName)
        },
        modifier = modifier
    )
}

@Composable
fun TagDetailScreenContents(
    uiState: TagDetailUiState,
    onLoadMoreSubscribers: () -> Unit,
    onSubscriptionToggle: (Boolean, TagType) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading && uiState.tag == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    uiState.error?.let { errorMsg ->
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("오류", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
            }
        }
        return
    }

    uiState.tag?.let { actualTag ->
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            // 헤더 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Transparent)
            ) {
                IconButton(
                    onClick = onNavigateUp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // 태그 정보 영역
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // 상단 태그 정보
                Spacer(Modifier.height(16.dp)) // 상단 여백
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = actualTag.imageUrl),
                        contentDescription = actualTag.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = actualTag.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "총 ${actualTag.subscriberCount}명 구독중",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { onSubscriptionToggle(uiState.isSubscribed, actualTag.tagType) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.isSubscribed) Color(0xFFEEEEEE) else MaterialTheme.colorScheme.primary,
                        contentColor = if (uiState.isSubscribed) Color.DarkGray else MaterialTheme.colorScheme.onPrimary
                    ),
                    border = if (uiState.isSubscribed) BorderStroke(1.dp, Color.LightGray) else null
                ) {
                    Text(
                        text = if (uiState.isSubscribed) "구독중" else "구독하기",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "등록중인 유저",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                if (uiState.subscribers.isEmpty() && !uiState.isLoading) {
                    Text(
                        text = "아직 구독자가 없습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    // 구독자 리스트를 LazyVerticalGrid로 변경
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.subscribers.size) { index ->
                            UserSubscriberCard(
                                user = uiState.subscribers[index],
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onNavigateToUserDetail(uiState.subscribers[index].id, "TagDetailScreen") }
                            )
                        }

                        // 무한스크롤 트리거 및 로딩 인디케이터
                        if (!uiState.isLastPage) {
                            item(span = { GridItemSpan(maxLineSpan) }) { // 로딩 아이템은 한 줄 전체 차지
                                if (uiState.isLoading && uiState.subscribers.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    }
                                } else if (!uiState.isLoading) {
                                    // 마지막 아이템이 화면에 보이면 다음 페이지 로드 (실제로는 스크롤 위치 기반으로 트리거하는 것이 더 정확)
                                    LaunchedEffect(uiState.subscribers.size) {
                                         if (uiState.subscribers.isNotEmpty()) { // 최초 로딩 시 중복 호출 방지
                                            onLoadMoreSubscribers()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserSubscriberCard(
    user: User,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = user.thumbnailUrl),
                contentDescription = "User ${user.id}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 이미지 비율 1:1로 유지
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // 상하 패딩만 적용
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${user.age}세",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "TagDetailScreen Content Preview")
@Composable
fun TagDetailScreenContentsPreview() {
    MaterialTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(
                isLoading = false,
                tag = MainHomeTag(
                    id = "1",
                    name = "洋画が好き",
                    description = "영화를 좋아하는 사람들의 모임",
                    imageUrl = "https://picsum.photos/id/1060/200/200",
                    subscriberCount = 86488,
                    categoryId = "4",
                    tagType = TagType.LIFESTYLE
                ),
                isSubscribed = true,
                subscribers = List(4) { index -> // 미리보기용 데이터 늘리기
                    User(thumbnailUrl = "https://picsum.photos/id/100${index}/200/200", age = 25 + index, id = "${index + 1}")
                },
                nextCursor = "cursor_abc",
                isLastPage = false,
                error = null
            ),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "TagDetailScreen Content Empty Subscribers")
@Composable
fun TagDetailScreenContentsEmptyPreview() {
    MaterialTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(
                isLoading = false,
                tag = MainHomeTag(
                    id = "1",
                    name = "洋画が好き",
                    description = "영화를 좋아하는 사람들의 모임",
                    imageUrl = "https://picsum.photos/id/1060/200/200",
                    subscriberCount = 15,
                    categoryId = "4",
                    tagType = TagType.LIFESTYLE
                ),
                isSubscribed = false,
                subscribers = emptyList(),
                error = null
            ),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "TagDetailScreen Loading")
@Composable
fun TagDetailScreenLoadingPreview() {
    MaterialTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(isLoading = true),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "TagDetailScreen Error")
@Composable
fun TagDetailScreenErrorPreview() {
    MaterialTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(error = "데이터를 불러오는데 실패했습니다."),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingTagDetailScreenPreview() {
    TokitokiTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(isLoading = true),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorTagDetailScreenPreview() {
    TokitokiTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(error = "데이터를 불러오는데 실패했습니다."),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoSubscribersTagDetailScreenPreview() {
    TokitokiTheme {
        TagDetailScreenContents(
            uiState = TagDetailUiState(tag = MainHomeTag(
                id = "1",
                name = "독서",
                description = "책 읽는 것을 좋아하는 사람들의 모임",
                imageUrl = "https://picsum.photos/200/300?random=1",
                subscriberCount = 0,
                categoryId = "101",
                tagType = TagType.HOBBY,
                isSubscribed = false
            )),
            onLoadMoreSubscribers = {},
            onSubscriptionToggle = { _, _ -> },
            onNavigateUp = {},
            onNavigateToUserDetail = { _, _ -> }
        )
    }
} 