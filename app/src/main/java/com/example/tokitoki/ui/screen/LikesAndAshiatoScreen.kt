package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.screen.ashiato.DailyAshiatoSection
import com.example.tokitoki.ui.screen.ashiato.InfiniteListHandler
import com.example.tokitoki.ui.screen.like.LikeReceivedListComponent
import com.example.tokitoki.ui.state.AshiatoUiState
import com.example.tokitoki.ui.state.LikesAndAshiatoTab
import com.example.tokitoki.ui.state.LikesAndAshiatoUiState
import com.example.tokitoki.ui.viewmodel.LikesAndAshiatoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikesAndAshiatoScreen(
    viewModel: LikesAndAshiatoViewModel = hiltViewModel(),
    onNavigateToUserProfile: (String, String) -> Unit,
    initialTab: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { LikesAndAshiatoTab.entries.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(initialTab) {
        initialTab?.let { tabName ->
            val initialPageIndex = when (tabName) {
                LikesAndAshiatoTab.ASHIATO.name -> LikesAndAshiatoTab.ASHIATO.ordinal
                LikesAndAshiatoTab.LIKES.name -> LikesAndAshiatoTab.LIKES.ordinal
                else -> pagerState.currentPage
            }
            pagerState.scrollToPage(initialPageIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.changeTab(pagerState.currentPage)
        }
    }

    LikesAndAshiatoContent(
        uiState = uiState,
        pagerState = pagerState,
        onTabClick = { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        },
        onRefresh = { viewModel.refresh() },
        onLoadMore = { viewModel.loadMore() },
        onUserClickFromLikes = { userId ->
            viewModel.addLikesUserIdsToCache()
            onNavigateToUserProfile(userId, "LikeScreen")
        },
        onUserClickFromAshiato = { date, userId ->
            viewModel.addAshiatoUserIdsToCache(date)
            onNavigateToUserProfile(userId, "AshiatoScreen")
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LikesAndAshiatoContent(
    uiState: LikesAndAshiatoUiState,
    pagerState: PagerState,
    onTabClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onUserClickFromLikes: (String) -> Unit,
    onUserClickFromAshiato: (date: String, userId: String) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.weight(1f))
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.weight(2f),
                        containerColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        LikesAndAshiatoTab.entries.forEachIndexed { index, tab ->
                            Tab(
                                selected = index == pagerState.currentPage,
                                onClick = { onTabClick(index) },
                                text = { Text(text = tab.title, color = Color.Black) }
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        LikesAndAshiatoTab.LIKES.ordinal -> {
                            val likeListState = rememberLazyGridState()
                            LikeReceivedListComponent(
                                likes = uiState.likeState.receivedLikes,
                                listState = likeListState,
                                isRefreshing = uiState.likeState.receivedLikesIsRefreshing,
                                onRefresh = onRefresh,
                                onLoadMore = onLoadMore,
                                onUserClick = onUserClickFromLikes
                            )
                        }

                        LikesAndAshiatoTab.ASHIATO.ordinal -> {
                            AshiatoPageContent(
                                uiState = uiState.ashiatoState,
                                onRefresh = onRefresh,
                                onLoadMore = onLoadMore,
                                onUserClick = onUserClickFromAshiato
                            )
                        }
                    }
                }
            }
        }
    )
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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun LikesAndAshiatoContentPreview() {
    val pagerState = rememberPagerState(pageCount = { LikesAndAshiatoTab.entries.size })
    LikesAndAshiatoContent(
        uiState = LikesAndAshiatoUiState(),
        pagerState = pagerState,
        onTabClick = { },
        onRefresh = { },
        onLoadMore = { },
        onUserClickFromLikes = { },
        onUserClickFromAshiato = { _, _ -> }
    )
}