package com.example.tokitoki.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.tokitoki.domain.model.FavoriteUser
import com.example.tokitoki.ui.state.FavoriteUsersUiState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.FavoriteUsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteUsersScreen(
    viewModel: FavoriteUsersViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // 토스트 메시지 처리
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // 토스트 메시지를 표시한 후에는 상태를 초기화
            viewModel.clearToastMessage()
        }
    }
    
    FavoriteUsersContents(
        uiState = uiState,
        loadFavoriteUsers = viewModel::loadFavoriteUsers,
        refreshFavoriteUsers = viewModel::refreshFavoriteUsers,
        onBackClick = onBackClick,
        onMitenClick = { userId, isCurrentlySending -> viewModel.sendMiten(userId, isCurrentlySending) },
        onUserClick = { userId ->
            viewModel.onUserClick(userId)
            onNavigateToUserDetail(userId, "FavoriteUsersScreen")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteUsersContents(
    uiState:FavoriteUsersUiState,
    loadFavoriteUsers: () -> Unit,
    onBackClick: () -> Unit,
    refreshFavoriteUsers: () -> Unit,
    onMitenClick: (String, Boolean) -> Unit,
    onUserClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= layoutInfo.totalItemsCount - 2 // Load when 2 items away from the end
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && uiState.hasMore && !uiState.isLoading) {
            loadFavoriteUsers()
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = {refreshFavoriteUsers()},
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "お気に入り") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = LocalColor.current.white)
                )
            },
            containerColor = LocalColor.current.white
        ) { paddingValues ->
            when {
                uiState.isLoading && uiState.favoriteUsers.isEmpty() -> {
                    // 로딩중이며 item이 비어있을때
                    // 로딩 중 화면 표시
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    // 에러 화면 표시 (예: Text(uiState.error))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${uiState.error}")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        state = listState,
                    ) {
                        items(uiState.favoriteUsers) { user ->
                            FavoriteUserItem(
                                user = user,
                                onMitenClick = onMitenClick,
                                onUserClick = onUserClick
                            )
                        }

                        if (uiState.isLoading && uiState.favoriteUsers.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavoriteUserItem(
    user: FavoriteUser,
    onMitenClick: (String, Boolean) -> Unit,
    onUserClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 썸네일 영역을 클릭 가능하게 설정
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(user.id) }
            ) {
                Image(
                    painter = rememberImagePainter(user.thumbnailUrl),
                    contentDescription = "User Thumbnail",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.name,
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

                // 직업과 혈액형을 한 Row에 배치
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "職業: ${user.job}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "血液型: ${user.bloodType}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // 버튼 위 간격은 유지

                Button(
                    onClick = { onMitenClick(user.id, user.isSendingMiten) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    enabled = !user.isSendingMiten
                ) {
                    Text("みてね！")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteUsersScreenPreview() {
    //  샘플 데이터를 사용하여 UI 상태를 만듭니다. 실제 데이터와 유사하게 조정할 수 있습니다.
    val sampleUsers = listOf(
        FavoriteUser(
            id = "",
            thumbnailUrl = "https://example.com/user1.jpg", // 실제 이미지 URL 또는 drawable 리소스로 변경
            name = "User One",
            age = 25,
            location = "Seoul",
            job = "Developer",
            bloodType = "A",
            timestamp = System.currentTimeMillis()
        ),
        FavoriteUser(
            id = "",
            thumbnailUrl = "https://example.com/user2.jpg",
            name = "User Two",
            age = 30,
            location = "Busan",
            job = "Designer",
            bloodType = "B",
            timestamp = System.currentTimeMillis() - 1000
        )
    )

    val uiState = FavoriteUsersUiState(
        favoriteUsers = sampleUsers,
        isLoading = false,
        error = null,
        hasMore = true // 또는 false, 페이지네이션 상태에 따라
    )

    TokitokiTheme {
        FavoriteUsersContents(
            uiState = uiState,
            loadFavoriteUsers = {},
            onBackClick = {},
            refreshFavoriteUsers = {},
            onMitenClick = { _, _ -> },
            onUserClick = {}
        )
    }
}