package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat
import com.example.tokitoki.ui.state.MessageListUiState
import com.example.tokitoki.ui.viewmodel.MessageListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageListScreen(viewModel: MessageListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val previousChatsListState = rememberLazyListState()
    val matchingUsersListState = rememberLazyListState()

    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                viewModel.refreshData()
                isRefreshing = false
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (uiState.isLoading) {
                LoadingShimmer()
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    MessageListTitle(title = "メッセージ")
                    Spacer(modifier = Modifier.height(16.dp))
                    MatchingUsersSection(matchingUsers = uiState.matchingUsers, listState = matchingUsersListState)
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    PreviousChatsSection(previousChats = uiState.previousChats, listState = previousChatsListState)

                    if (uiState.errorMessage != null) {
                        Text(
                            text = "Error: ${uiState.errorMessage}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(matchingUsersListState) {
        snapshotFlow { matchingUsersListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex > uiState.matchingUsers.size - 3 && !uiState.isLoading) {
                    viewModel.loadMoreMatchingUsers()
                }
            }
    }

    LaunchedEffect(previousChatsListState) {
        snapshotFlow { previousChatsListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex > uiState.previousChats.size - 3 && !uiState.isLoading) {
                    viewModel.loadMorePreviousChats()
                }
            }
    }
}

// 9. "메시지" 타이틀 Composable
@Composable
fun MessageListTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

// 10. "매칭" 섹션 Composable
@Composable
fun MatchingUsersSection(matchingUsers: List<MatchingUser>, listState: LazyListState) {
    Column {
        Text(
            text = "マッチング",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        MatchingUserList(users = matchingUsers, listState = listState)
    }
}

// 11. 매칭 유저 리스트 Composable
@Composable
fun MatchingUserList(users: List<MatchingUser>, listState: LazyListState) {
    LazyRow(state = listState) {
        items(users) { user ->
            MatchingUserItem(user = user)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

// 12. 매칭 유저 아이템 Composable
@Composable
fun MatchingUserItem(user: MatchingUser) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = user.thumbnail,
            contentDescription = user.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

// 13. "이전 채팅" 섹션 Composable
@Composable
fun PreviousChatsSection(previousChats: List<PreviousChat>, listState: LazyListState) {
    Column {
        Text(
            text = "Previous Chats",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        PreviousChatList(chats = previousChats, listState = listState)
    }
}

// 14. 이전 채팅 리스트 Composable
@Composable
fun PreviousChatList(chats: List<PreviousChat>, listState: LazyListState) {
    LazyColumn(state = listState) {
        items(chats) { chat ->
            PreviousChatItem(chat = chat)
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
        }
    }
}

// 15. 이전 채팅 아이템 Composable
@Composable
fun PreviousChatItem(chat: PreviousChat) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        ) {
        AsyncImage(
            model = chat.thumbnail,
            contentDescription = chat.nickname,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.nickname,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = chat.hometown, style = MaterialTheme.typography.bodySmall)
        }
        val formattedDate = chat.lastMessageDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit
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

    content(
        modifier = modifier
            .background(brush)
    )
}

@Composable
fun LoadingShimmer() {
    Column(modifier = Modifier.padding(16.dp)) {
        MessageListTitle(title = "メッセージ")
        Spacer(modifier = Modifier.height(16.dp))

        // 매칭 유저 섹션 Shimmer
        Column {
            Text(
                text = "マッチング",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
                items(5) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        ) { shimmerModifier ->
                            Box(modifier = shimmerModifier)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerEffect(
                            modifier = Modifier
                                .width(60.dp)
                                .height(12.dp)
                        ) { shimmerModifier ->
                            Box(modifier = shimmerModifier)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // 이전 채팅 섹션 Shimmer
        Column {
            Text(
                text = "Previous Chats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(10) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        ) { shimmerModifier ->
                            Box(modifier = shimmerModifier)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(16.dp)
                            ) { shimmerModifier ->
                                Box(modifier = shimmerModifier)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            ShimmerEffect(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(12.dp)
                            ) { shimmerModifier ->
                                Box(modifier = shimmerModifier)
                            }
                        }
                    }
                    Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                }
            }
        }
    }
}

// 16. 프리뷰 Composable
@Preview(showBackground = true)
@Composable
fun MessageListScreenPreview() {
    // 프리뷰에서는 ViewModel 대신 더미 데이터를 직접 사용하여 UI를 테스트합니다.
    val dummyUiState = MessageListUiState(
        matchingUsers = listOf(
            MatchingUser("1", "Alice", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            MatchingUser("2", "Bob", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            MatchingUser("3", "Charlie", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"), // 추가 데이터
            MatchingUser("4", "David", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"), // 추가 데이터
        ),
        previousChats = listOf(
            PreviousChat("4", "David", "Seoul", LocalDate.of(2024, 1, 20), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            PreviousChat("5", "Eve", "Busan", LocalDate.of(2024, 1, 15), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            PreviousChat("6", "Frank", "Incheon", LocalDate.of(2024, 1, 10), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            PreviousChat("7", "Grace", "Daegu", LocalDate.of(2024, 1, 5), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg")
        ),
        isLoading = false,
        errorMessage = null
    )

    //  MessageListScreen(viewModel = MessageListViewModel()) // 실제 ViewModel 사용 대신
    Column {
        MessageListTitle(title = "Messages")
        Spacer(modifier = Modifier.height(16.dp))
        MatchingUsersSection(matchingUsers = dummyUiState.matchingUsers, listState = rememberLazyListState())
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        PreviousChatsSection(previousChats = dummyUiState.previousChats, listState = rememberLazyListState())
    }
}

