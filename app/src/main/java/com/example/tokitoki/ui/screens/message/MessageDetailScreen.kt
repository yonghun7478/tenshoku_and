package com.example.tokitoki.ui.screens.message

import androidx.compose.foundation.layout.*
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
import com.example.tokitoki.ui.viewmodel.MessageDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    otherUserId: String,
    onNavigateUp: () -> Unit,
    viewModel: MessageDetailViewModel = hiltViewModel()
) {
    // Collect state from ViewModel (will be used in later steps)
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
                    // Message Page Placeholder
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("메시지 페이지 (구현 예정)")
                    }
                }
                1 -> {
                    // User Detail Page Placeholder
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("유저 상세 정보 페이지 (구현 예정)")
                    }
                }
            }
        }
    }
}
