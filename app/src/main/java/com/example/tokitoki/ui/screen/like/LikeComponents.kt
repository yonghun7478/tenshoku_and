package com.example.tokitoki.ui.screen.like

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.clickable
import coil.compose.AsyncImage
import androidx.compose.material3.CardDefaults

@Composable
fun LazyGridState.OnLastItemVisible(onLastItemVisible: () -> Unit) {
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
    listState: LazyGridState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onUserClick: (String) -> Unit
) {
    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(likes, key = { "${it.id}-${it.receivedTime}" }) { like ->
                LikeReceivedItemComponent(
                    like = like,
                    onUserClick = onUserClick
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

// 時間フォーマット用ユーティリティ関数
fun formatReceivedTime(receivedTimeMillis: Long): String {
    val currentTimeMillis = System.currentTimeMillis()
    val diffMillis = currentTimeMillis - receivedTimeMillis

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        minutes < 60 -> "${minutes}分前"
        hours < 24 -> "${hours}時間前"
        days < 7 -> "${days}日前"
        else -> "${java.time.Instant.ofEpochMilli(receivedTimeMillis).atZone(java.time.ZoneId.systemDefault()).toLocalDate()}"
    }
}

// Note: This was moved from LikeScreen.kt
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikeReceivedItemComponent(
    like: LikeItemUiState,
    onUserClick: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onUserClick(like.id) },
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "Received Time",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatReceivedTime(like.receivedTime),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            AsyncImage(
                model = like.thumbnail,
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = like.nickname,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${like.age}歳",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        ),
        onUserClick = {}
    )
} 