package com.example.tokitoki.ui.screens.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    otherUserId: String,
    onNavigateUp: () -> Unit,
    viewModel: MessageDetailViewModel = hiltViewModel()
) {
    // Collect state from ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()

    LaunchedEffect(otherUserId) {
        viewModel.initialize(otherUserId)
    }

    val pagerState = rememberPagerState(pageCount = { 2 }) // 2 pages: Message and User Detail
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.userProfile?.name ?: "채팅") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = { // Modified actions based on screenshot
                    // Placeholder for thumbnail button
                    IconButton(onClick = { /* TODO: Handle thumbnail click */ }) {
                        // Replace with actual user thumbnail later
                         Text("\uD83D\uDCF7", style = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.titleLarge.fontSize))
                    }
                    // More options menu
                    IconButton(onClick = { /* TODO: Handle more options click */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "더보기")
                    }
                }
            )
        },
        bottomBar = { // Simple indicator for now
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }) { Text("메시지") }
                    Button(onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }) { Text("상세정보") }
                }
            }
        }
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
                        onSendMessage = { message -> viewModel.sendMessage(otherUserId, message) }
                    )
                }
                1 -> {
                    // User Detail Page
                    UserDetailPageContent()
                }
            }
        }
    }
}

@Composable
private fun MessagePageContent(
    messages: List<Message>,
    uiState: MessageDetailUiState,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Placeholder for Profile Summary Area (Based on screenshot)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp) // Adjust height as needed
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("프로필 요약 영역 (구현 예정)")
        }

        // Placeholder for Common Topics Buttons (Based on screenshot)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Example buttons, replace with actual logic/data later
            OutlinedButton(onClick = { /*TODO*/ }) { Text("#공통점1") }
            OutlinedButton(onClick = { /*TODO*/ }) { Text("#공통점2") }
            OutlinedButton(onClick = { /*TODO*/ }) { Text("#공통점3") }
        }
        
        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f) // Takes available space
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            reverseLayout = true // Show latest messages at the bottom
        ) {
            items(messages) { message ->
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

@Composable
private fun UserDetailPageContent() {
    // TODO: Implement User Detail Page UI
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Text("유저 상세 정보 페이지 (구현 예정)")
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
            color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
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
private fun MessageInput(
    onSendMessage: (String) -> Unit,
    isSending: Boolean
) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("메시지를 입력하세요") },
            enabled = !isSending
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
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
                Text("전송")
            }
        }
    }
}
