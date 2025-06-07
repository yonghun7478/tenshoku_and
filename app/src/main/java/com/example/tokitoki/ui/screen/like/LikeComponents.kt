package com.example.tokitoki.ui.screen.like

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.LikeItemUiState

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

// Note: This was moved from LikeScreen.kt
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

// Note: This was moved from LikeScreen.kt
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

        listState.OnLastItemVisible {
             if (likes.isNotEmpty() && listState.layoutInfo.totalItemsCount > 0) {
                onLoadMore()
            }
        }
    }
}

// Note: This was moved from LikeScreen.kt
@OptIn(ExperimentalFoundationApi::class)
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