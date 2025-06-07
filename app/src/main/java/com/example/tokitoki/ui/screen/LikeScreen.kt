package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.LikeItemUiState
import com.example.tokitoki.ui.state.LikeScreenUiState
import com.example.tokitoki.ui.state.LikeTab
import com.example.tokitoki.ui.viewmodel.LikeScreenViewModel
import kotlinx.coroutines.launch

// Main Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeScreen(viewModel: LikeScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            LikeTopBarComponent(
                title = "いいね"
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            // 탭에 따른 리스트 표시
            LikeReceivedListComponent(
                likes = uiState.receivedLikes,
                listState = listState,
                isRefreshing = uiState.receivedLikesIsRefreshing,
                onRefresh = { viewModel.refreshLikes() },
                onLoadMore = { viewModel.loadMoreLikes() }
            )
        }
    }
}

// Top Bar Composable
@Composable
fun LikeTopBarComponent(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge) // Material3 Typography
    }
}

// List Composables (for each tab)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeReceivedListComponent(
    likes: List<LikeItemUiState>,
    listState: LazyListState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
) {
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(likes, key = { it.id }) { like ->
                LikeReceivedItemComponent(
                    like = like
                )
            }
        }

        val isAtBottom = remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != null && lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
            }
        }

        LaunchedEffect(isAtBottom.value) {
            if (isAtBottom.value && likes.isNotEmpty()) {
                onLoadMore()
            }
        }
    }
}

@Composable
fun LazyListState.OnLastItemVisible(onLastItemVisible: () -> Unit) {
    val isAtBottom = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(isAtBottom.value) {
        if (isAtBottom.value) {
            onLastItemVisible()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LikeReceivedItemComponent(
    like: LikeItemUiState
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = like.nickname, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "${like.age}歳",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = like.introduction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* TODO: Handle "Like Back" action */ },
            ) {
                Text(text = "いいね！する")
            }
        }
    }
}

// Preview (Simplified)
@Preview(showBackground = true)
@Composable
fun LikeReceivedItemComponentPreview() {
    LikeReceivedItemComponent(
        like = LikeItemUiState(
            id = "1",
            thumbnail = "https://via.placeholder.com/150",
            nickname = "Test User",
            age = 25,
            introduction = "This is a sample introduction.",
            receivedTime = System.currentTimeMillis(),
            isRefreshing = false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LikeScreenPreview() {
    val viewModel: LikeScreenViewModel = hiltViewModel()
    LikeScreen(viewModel = viewModel)
}