package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.screen.ashiato.DailyAshiatoSection
import com.example.tokitoki.ui.screen.ashiato.InfiniteListHandler
import com.example.tokitoki.ui.screen.like.LikeReceivedListComponent
import com.example.tokitoki.ui.state.AshiatoUiState
import com.example.tokitoki.ui.state.LikesAndAshiatoTab
import com.example.tokitoki.ui.viewmodel.LikesAndAshiatoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LikesAndAshiatoScreen(
    viewModel: LikesAndAshiatoViewModel = hiltViewModel(),
    onNavigateToUserProfile: (userId: String) -> Unit,
    onBackClick: () -> Unit,
    showBackButton: Boolean
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { LikesAndAshiatoTab.entries.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.changeTab(pagerState.currentPage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("いいね・足あと") },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
                LikesAndAshiatoTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = tab.title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    LikesAndAshiatoTab.LIKES.ordinal -> {
                        val likeListState = rememberLazyListState()
                        LikeReceivedListComponent(
                            likes = uiState.likeState.receivedLikes,
                            listState = likeListState,
                            isRefreshing = uiState.likeState.receivedLikesIsRefreshing,
                            onRefresh = { viewModel.refresh() },
                            onLoadMore = { viewModel.loadMore() }
                        )
                    }
                    LikesAndAshiatoTab.ASHIATO.ordinal -> {
                        AshiatoPageContent(
                            uiState = uiState.ashiatoState,
                            onRefresh = { viewModel.refresh() },
                            onLoadMore = { viewModel.loadMore() },
                            onUserClick = { date, userId ->
                                viewModel.addUserIdsToCache(date)
                                onNavigateToUserProfile(userId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshiatoPageContent(
    uiState: AshiatoUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onUserClick: (date: String, userId: String) -> Unit
) {
    val listState = rememberLazyListState()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        when {
            uiState.isLoadingInitial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("오류가 발생했습니다: ${uiState.error.localizedMessage ?: "알 수 없는 오류"}")
                }
            }
            uiState.timeline.dailyLogs.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("아직 받은 足跡(아시아토)가 없어요.")
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.timeline.dailyLogs,
                        key = { it.date }
                    ) { dailyLog ->
                        DailyAshiatoSection(
                            dailyLog = dailyLog,
                            onUserClick = onUserClick
                        )
                    }
                }
                InfiniteListHandler(listState = listState) {
                    if (uiState.canLoadMore) {
                        onLoadMore()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LikesAndAshiatoScreenPreview() {
    LikesAndAshiatoScreen(
        onNavigateToUserProfile = {},
        onBackClick = {},
        showBackButton = true
    )
} 