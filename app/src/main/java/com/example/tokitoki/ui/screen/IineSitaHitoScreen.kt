package com.example.tokitoki.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.ui.state.IineSitaHitoUiState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.IineSitaHitoViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@Composable
fun IineSitaHitoScreen(
    modifier: Modifier = Modifier,
    viewModel: IineSitaHitoViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IineSitaHitoContents(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadMoreUsers,
        onBackClick = onBackClick,
        onNavigateToUserDetail = { userId ->
            viewModel.onUserClick(userId)
            onNavigateToUserDetail(userId, "IineSitaHitoScreen")
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IineSitaHitoContents(
    uiState: IineSitaHitoUiState,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToUserDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(uiState.isRefreshing),
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalColor.current.white)
            ) {
                // Header
                TopAppBar(
                    title = { Text("あなたからのいいね") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LocalColor.current.white
                    )
                )

                AnimatedVisibility(
                    visible = uiState.users.isEmpty() && !uiState.isLoading,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                AnimatedVisibility(
                    visible = uiState.users.isNotEmpty() || uiState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.users,
                            key = { user -> user.id }
                        ) { user ->
                            LikedUserCard(
                                user = user,
                                onNavigateToUserDetail = onNavigateToUserDetail,
                                modifier = Modifier
                            )

                            if (user == uiState.users.lastOrNull() && !uiState.isLoading && uiState.hasMoreItems) {
                                LaunchedEffect(user.id) {
                                    onLoadMore()
                                }
                            }
                        }

                        if (uiState.isLoading) {
                            item {
                                LoadingIndicator()
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                modifier = modifier.align(Alignment.BottomCenter),
                visible = uiState.error != null,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                ErrorSnackbar(
                    message = uiState.error,
                    onDismiss = { /* TODO: Add error dismiss handler */ }
                )
            }
        }
    }
}

@Composable
private fun LikedUserCard(
    user: LikeItem,
    onNavigateToUserDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateToUserDetail(user.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColor.current.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AsyncImage(
                model = user.thumbnail,
                contentDescription = "Profile image of ${user.nickname}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.nickname,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${user.age}歳",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                val hasOccupation = !user.occupation.isNullOrEmpty()
                val hasPersonalityTrait = !user.personalityTrait.isNullOrEmpty()

                if (hasOccupation || hasPersonalityTrait) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (hasOccupation) {
                            Text(
                                text = "職業: ${user.occupation}",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (hasPersonalityTrait) {
                            Text(
                                text = "性格: ${user.personalityTrait}",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                val hasDatingPhilosophy = !user.datingPhilosophy.isNullOrEmpty()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ライフスタイル: ${user.lifestyle}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    user.datingPhilosophy?.let { philosophy ->
                        Text(
                            text = "恋愛観: $philosophy",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                user.marriageView?.let { marriage ->
                    Text(
                        text = "結婚観: $marriage",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .scale(scale)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Text(
            text = "まだ「いいね」した人がいません",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = "気になる人を探してみましょう！",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun ErrorSnackbar(
    message: String?,
    onDismiss: () -> Unit
) {
    message?.let {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = onDismiss) {
                    Text("閉じる")
                }
            }
        ) {
            Text(text = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IineSitaHitoContentsEmptyPreview() {
    TokitokiTheme {
        IineSitaHitoContents(
            uiState = IineSitaHitoUiState(
                isLoading = false,
                users = emptyList()
            ),
            onRefresh = {},
            onLoadMore = {},
            onBackClick = {},
            onNavigateToUserDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IineSitaHitoContentsLoadingPreview() {
    TokitokiTheme {
        IineSitaHitoContents(
            uiState = IineSitaHitoUiState(
                isLoading = true,
                users = emptyList()
            ),
            onRefresh = {},
            onLoadMore = {},
            onBackClick = {},
            onNavigateToUserDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IineSitaHitoContentsWithDataPreview() {
    TokitokiTheme {
        IineSitaHitoContents(
            uiState = IineSitaHitoUiState(
                isLoading = false,
                users = listOf(
                    LikeItem(
                        id = "1",
                        nickname = "하나코",
                        age = 28,
                        location = "도쿄",
                        thumbnail = "",
                        introduction = "안녕하세요! 취미는 여행과 요리입니다.",
                        occupation = "디자이너",
                        receivedTime = System.currentTimeMillis(),
                        likedAt = System.currentTimeMillis(),
                        personalityTrait = "真面目な",
                        lifestyle = "規則正しい生活を大切にしています。",
                        datingPhilosophy = "真剣な出会いを求める",
                        marriageView = "結婚に前向き"
                    ),
                    LikeItem(
                        id = "2",
                        nickname = "유이",
                        age = 25,
                        location = "오사카",
                        thumbnail = "",
                        introduction = "음악을 좋아합니다.",
                        occupation = "회사원",
                        receivedTime = System.currentTimeMillis(),
                        likedAt = System.currentTimeMillis(),
                        personalityTrait = "活発な",
                        lifestyle = "自由な魂です！",
                        datingPhilosophy = "良い友達から始めたい",
                        marriageView = "まだよく分かりません"
                    )
                )
            ),
            onRefresh = {},
            onLoadMore = {},
            onBackClick = {},
            onNavigateToUserDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IineSitaHitoContentsErrorPreview() {
    TokitokiTheme {
        IineSitaHitoContents(
            uiState = IineSitaHitoUiState(
                isLoading = false,
                users = emptyList(),
                error = "네트워크 오류가 발생했습니다."
            ),
            onRefresh = {},
            onLoadMore = {},
            onBackClick = {},
            onNavigateToUserDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    TokitokiTheme {
        EmptyState()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingIndicatorPreview() {
    TokitokiTheme {
        LoadingIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorSnackbarPreview() {
    TokitokiTheme {
        ErrorSnackbar(
            message = "네트워크 오류가 발생했습니다.",
            onDismiss = {}
        )
    }
} 