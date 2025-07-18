package com.example.tokitoki.ui.screens.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.ui.viewmodel.MessageDetailViewModel
import kotlinx.coroutines.launch
import com.example.tokitoki.ui.state.MessageDetailUiState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import android.util.Log
import com.example.tokitoki.ui.screen.UserDetailScreen
import com.example.tokitoki.ui.theme.LocalColor
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Send

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    otherUserId: String,
    onNavigateUp: () -> Unit,
    viewModel: MessageDetailViewModel = hiltViewModel(),
    source: String? = null
) {
    LaunchedEffect(source) {
        Log.d("MessageDetailScreen", "Source: $source")
    }
    // Collect state from ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    LaunchedEffect(otherUserId) {
        viewModel.initialize(otherUserId)
    }

    val pagerState = rememberPagerState(pageCount = { 2 }) // 2 pages: Message and User Detail
    val coroutineScope = rememberCoroutineScope()
 
    val onNavigateToUserDetailTab: () -> Unit = remember(coroutineScope, pagerState) {
        { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
    }

    Scaffold(
        topBar = {
            MessageDetailTopAppBar(
                userName = uiState.userProfile?.name ?: "채팅",
                userThumbnailUrl = uiState.userProfile?.thumbnailUrl,
                onNavigateUp = onNavigateUp,
                onThumbnailClick = onNavigateToUserDetailTab,
            )
        },
        containerColor = LocalColor.current.white
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> {
                    // Message Page
                    MessagePageContent(
                        messages = messages,
                        uiState = uiState,
                        onSendMessage = { message -> viewModel.sendMessage(otherUserId, message) },
                        onProfileAreaClick = onNavigateToUserDetailTab,
                        otherUserId = otherUserId,
                        onLoadMore = { userId -> viewModel.loadMoreMessages(userId) },
                        viewModel = viewModel,
                        source = source
                    )
                }
                1 -> {
                    // User Detail Page
                    UserDetailScreen(
                        selectedUserId = otherUserId,
                        screenName = "MessageListScreen",
                        onBackClick = {},
                        viewModel = hiltViewModel(),
                        sharedViewModel = hiltViewModel()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailTopAppBar(
    userName: String,
    userThumbnailUrl: String?,
    onNavigateUp: () -> Unit,
    onThumbnailClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        title = { Text(userName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = LocalColor.current.white),
        actions = {
            IconButton(onClick = onThumbnailClick) {
                AsyncImage(
                    model = userThumbnailUrl,
                    contentDescription = "User Thumbnail",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun MessagePageContent(
    messages: List<Message>,
    uiState: MessageDetailUiState,
    onSendMessage: (String) -> Unit,
    onProfileAreaClick: () -> Unit,
    otherUserId: String,
    onLoadMore: (String) -> Unit,
    viewModel: MessageDetailViewModel = hiltViewModel(),
    source: String?
) {
    val listState = rememberLazyListState()
    val shouldScrollToBottom by viewModel.shouldScrollToBottom.collectAsStateWithLifecycle()

    // 새 메시지 도착 시 스크롤을 맨 아래로 이동
    LaunchedEffect(shouldScrollToBottom) {
        if (shouldScrollToBottom && messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
            viewModel.onScrollToBottomHandled()
        }
    }

    // 무한 스크롤: 오래된 메시지 로드 (리버스 레이아웃)
    LaunchedEffect(listState, messages.size) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndexInViewport ->
                if (lastVisibleIndexInViewport == null) return@collect // null 체크 추가

                Log.d("InfiniteScroll", "lastVisibleIndexInViewport: $lastVisibleIndexInViewport, messages.size: ${messages.size}")
                val threshold = 10 // 사용자가 요청한 값
                val conditionResult = lastVisibleIndexInViewport >= messages.size - 1 - threshold
                Log.d("InfiniteScroll", "Condition (lastVisibleIndexInViewport >= messages.size - 1 - threshold): $conditionResult")

                if (conditionResult) {
                    Log.d("InfiniteScroll", "Calling onLoadMore for userId: $otherUserId")
                    onLoadMore(otherUserId)
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(4.dp)
    ) {
        // Group Profile Summary and Common Topics for centering
        if (source != "chat") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Placeholder for Profile Summary Area (Based on screenshot)
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clickable { onProfileAreaClick() },
                    contentAlignment = Alignment.Center
                ) {
                    // 프로필 정보 표시 (썸네일, 이름)
                    uiState.userProfile?.let { userProfile ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) { // 세로 배치, 아이템 중앙 정렬
                            AsyncImage(
                                model = userProfile.thumbnailUrl,
                                contentDescription = userProfile.name,
                                modifier = Modifier
                                    .size(width = 40.dp, height = 60.dp) // 세로 직사각형 형태
                                    .clip(RoundedCornerShape(8.dp)), // 모서리 둥글게
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp)) // 간격
                            Text(
                                text = userProfile.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally) // 텍스트 너비를 내용에 맞추고 중앙 정렬
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp)) // 프로필 영역과 버튼 영역 사이 간격 줄임 (16dp -> 12dp)

                // Placeholder for Common Topics Buttons (Based on screenshot)
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.userTags.take(3).forEach { tag -> // uiState에서 userTags를 가져와 최대 3개까지만 출력
                        OutlinedButton(
                            onClick = { /* TODO: 태그 클릭 시 동작 정의 */ },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) { Text("#${tag.name}") }
                    }
                }
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f) // Takes available space
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true // Show latest messages at the bottom
        ) {
            items(
                items = messages,
                key = { message -> message.id }
            ) { message ->
                MessageItem(
                    message = message,
                    isCurrentUser = message.isFromMe // Assuming Message model has isFromMe
                )
            }
        }

        // Message Input Area
        MessageInput(
            onSendMessage = onSendMessage,
            isSending = uiState.isSending // Assuming uiState has isSending
        )
    }
}

// Re-add MessageItem composable
@Composable
private fun MessageItem(
    message: Message,
    isCurrentUser: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = if (isCurrentUser) LocalColor.current.blue else MaterialTheme.colorScheme.surface,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Re-add MessageInput composable
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MessageInput(
    onSendMessage: (String) -> Unit,
    isSending: Boolean
) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("メッセージを入力してください", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)) },
            enabled = !isSending,
            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF0F0F0),
                unfocusedContainerColor = Color(0xFFF0F0F0),
                disabledContainerColor = Color(0xFFF0F0F0),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = CircleShape
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSendMessage(messageText)
                    messageText = ""
                }
            },
            enabled = messageText.isNotBlank() && !isSending
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(Icons.Default.Send, contentDescription = "메시지 전송")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageDetailTopAppBarPreview() {
    MessageDetailTopAppBar(
        userName = "사용자 이름",
        userThumbnailUrl = "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg",
        onNavigateUp = {},
        onThumbnailClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun MessagePageContentPreview() {
    val dummyMessages = listOf(
        Message("1", "other_user", "current_user", "안녕하세요!", 1678886400000L, true, false),
        Message("2", "current_user", "other_user", "네, 안녕하세요!", 1678886460000L, true, true),
        Message("3", "other_user", "current_user", "점심 식사 하셨나요?", 1678886520000L, false, false)
    )
    val dummyUiState = MessageDetailUiState(
        isLoading = false,
        isSending = false,
        userProfile = null, // 또는 더미 UserProfile 제공
        error = null
    )

    MessagePageContent(
        messages = dummyMessages,
        uiState = dummyUiState,
        onSendMessage = {}, // 미리보기에서는 실제 전송 로직 대신 빈 람다 제공
        onProfileAreaClick = {}, // 미리보기에서는 빈 람다 제공
        otherUserId = "",
        onLoadMore = {}, // Add dummy lambda for preview
        viewModel = hiltViewModel(),
        source = null
    )
}