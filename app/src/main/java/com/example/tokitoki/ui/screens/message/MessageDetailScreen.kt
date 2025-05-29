package com.example.tokitoki.ui.screens.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.viewmodel.MessageDetailViewModel
import com.example.tokitoki.domain.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    otherUserId: String,
    onNavigateUp: () -> Unit,
    viewModel: MessageDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    
    LaunchedEffect(otherUserId) {
        viewModel.initialize(otherUserId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.userProfile?.name ?: "채팅") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 메시지 목록
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    reverseLayout = true
                ) {
//                    items(messages) { message ->
//                        MessageItem(
//                            message = message,
//                            isCurrentUser = message.isFromMe
//                        )
//                    }
                }

                // 메시지 입력 영역
                MessageInput(
                    onSendMessage = { message ->
                        viewModel.sendMessage(otherUserId, message)
                    },
                    isSending = uiState.isSending
                )
            }

            // 로딩 및 에러 상태 처리
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
    }
}

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