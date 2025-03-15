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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val listStates = remember {
        LikeTab.values().associateWith { LazyListState() }.toMutableMap() // MutableMap으로 변경
    }

    Scaffold(
        topBar = {
            LikeTopBarComponent(
                title = "いいね",
                isDeleteMode = uiState.deleteModeState.isDeleteMode,
                onToggleDeleteMode = { viewModel.onToggleDeleteMode() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
//                    backgroundColor = Color.Gray, // Material 3에서는 containerColor, contentColor로 변경
//                    contentColor = Color.White
                    containerColor = MaterialTheme.colorScheme.inverseSurface, // Material3 Color
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface  // Material3 Color
                )
            }
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            LikeTabBarComponent(
                selectedTab = uiState.selectedTab,
                onTabSelected = { tab -> viewModel.onTabSelected(tab) },
                isDeleteMode = uiState.deleteModeState.isDeleteMode
            )
            //삭제모드일때 삭제하기 버튼, 모두선택 체크박스 표시
            if (uiState.deleteModeState.isDeleteMode) {
                LikeDeleteModeBarComponent(
                    selectedItems = uiState.deleteModeState.selectedItems,
                    receivedLikes = uiState.receivedLikes,
                    sentLikes = uiState.sentLikes,
                    matchedLikes = uiState.matchedLikes,
                    selectedTab = uiState.selectedTab,
                    onSelectAllItems = { viewModel.onSelectAllItems() },
                    onConfirmDeleteSelectedItems = {
                        viewModel.onDeleteSelectedItems()
                    }
                )
            }
            // 탭에 따른 리스트 표시
            LikeTabContentComponent(
                uiState = uiState,
                listStates = listStates, // listStates를 전달
                onItemLongClicked = { itemId -> viewModel.onItemLongClicked(itemId) },//viewModel::onItemLongClicked,
                onSelectItem = viewModel::onSelectItem, // Pass item selection handler
                onRefresh = { viewModel.refreshLikes() },
                onLoadMore = { viewModel.loadMoreLikes() }
            )
            //삭제 다이얼로그
            if (uiState.deleteModeState.showDialog) { // 삭제 모드
                LikeDeleteConfirmDialogComponent(
                    onConfirmMultiDelete = { viewModel.onConfirmDeleteSelectedItems() },
                    onConfirmSingleDelete = {}, // 사용 안 함
                    onDismiss = { viewModel.onDismissDeleteDialog() }
                )
            } else if (uiState.deleteItemState.showDialog) { // 단일 아이템 삭제
                LikeDeleteConfirmDialogComponent(
                    onConfirmSingleDelete = { viewModel.onConfirmDeleteItem() },
                    onConfirmMultiDelete = null, // 사용 안 함
                    onDismiss = { viewModel.onDismissDeleteDialog() }
                )
            }

            //삭제 스낵바
            if (uiState.showSnackBar) {
                LaunchedEffect(uiState.showSnackBar) {
                    scope.launch {
                        snackbarHostState.showSnackbar("削除されました")
                        viewModel.onDismissSnackBar()
                    }
                }
            }
        }
    }
}

// Top Bar Composable
@Composable
fun LikeTopBarComponent(
    title: String,
    isDeleteMode: Boolean,
    onToggleDeleteMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge) // Material3 Typography
        IconButton(onClick = onToggleDeleteMode) {
            Icon(
                imageVector = if (isDeleteMode) Icons.Default.Close else Icons.Outlined.Delete, // Filled -> Outlined
                contentDescription = if (isDeleteMode) "Exit Delete Mode" else "Delete"
            )
        }
    }
}


// Tab Bar Composable
@Composable
fun LikeTabBarComponent(
    selectedTab: LikeTab,
    onTabSelected: (LikeTab) -> Unit,
    isDeleteMode: Boolean
) {
    //삭제모드일때는 탭바를 비활성화
    if (!isDeleteMode) {
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface, // Material3: Use surface color
            contentColor = MaterialTheme.colorScheme.onSurface // Material3: Use onSurface color
        ) {
            LikeTab.values().forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    text = {
                        Text(
                            tab.title,
                            color = if (tab == selectedTab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant // Material3: Use colorScheme
                        )
                    }
                )
            }
        }
    }
}

// Tab Content Composable (Handles displaying the list for each tab)
@Composable
fun LikeTabContentComponent(
    uiState: LikeScreenUiState,
    listStates: Map<LikeTab, LazyListState>, // Map으로 받음
    onItemLongClicked: (Int) -> Unit,
    onSelectItem: (Int) -> Unit, // Add item selection handler
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit // onLoadMore 파라미터 추가
) {
    when (uiState.selectedTab) {
        LikeTab.RECEIVED -> LikeReceivedListComponent(
            likes = uiState.receivedLikes,
            onItemLongClicked = onItemLongClicked,
            isDeleteMode = uiState.deleteModeState.isDeleteMode, // Pass delete mode
            selectedItems = uiState.deleteModeState.selectedItems,   // Pass selected items
            onSelectItem = onSelectItem, // Pass selection handler
            listState = listStates[LikeTab.RECEIVED]!!, // 올바른 LazyListState 사용,
            isRefreshing = uiState.receivedLikesIsRefreshing,
            onRefresh = onRefresh,
            onLoadMore = onLoadMore
        )

        LikeTab.SENT -> LikeSentListComponent(
            likes = uiState.sentLikes,
            onItemLongClicked = onItemLongClicked,
            isDeleteMode = uiState.deleteModeState.isDeleteMode,
            selectedItems = uiState.deleteModeState.selectedItems,
            onSelectItem = onSelectItem,
            listState = listStates[LikeTab.SENT]!!, // 올바른 LazyListState 사용
            isRefreshing = uiState.sentLikesIsRefreshing,
            onRefresh = onRefresh,
            onLoadMore = onLoadMore
        )

        LikeTab.MATCHED -> LikeMatchedListComponent(
            likes = uiState.matchedLikes,
            onItemLongClicked = onItemLongClicked,
            isDeleteMode = uiState.deleteModeState.isDeleteMode,
            selectedItems = uiState.deleteModeState.selectedItems,
            onSelectItem = onSelectItem,
            listState = listStates[LikeTab.MATCHED]!!, // 올바른 LazyListState 사용
            isRefreshing = uiState.matchedLikesIsRefreshing,
            onRefresh = onRefresh,
            onLoadMore = onLoadMore
        )
    }
}

// List Composables (for each tab)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeReceivedListComponent(
    likes: List<LikeItemUiState>,
    onItemLongClicked: (Int) -> Unit,
    isDeleteMode: Boolean,
    onSelectItem: (Int) -> Unit, // Pass item selection handler
    selectedItems: Set<Int> = emptySet(), //선택된 아이템 리스트 파라미터
    listState: LazyListState,
    isRefreshing: Boolean, // 추가
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit // onLoadMore 파라미터 추가
) {
    PullToRefreshBox( // LazyColumn을 PullToRefreshBox로 감쌉니다.
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing, // ViewModel의 isRefreshing 상태
        onRefresh = { onRefresh() } // ViewModel의 refreshLikes() 호출
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(likes, key = { it.id }) { like ->
                LikeReceivedItemComponent(
                    like = like,
                    onItemLongClicked = { onItemLongClicked(like.id) },
                    isDeleteMode = isDeleteMode, // Pass delete mode to item
                    onCheckedChange = { onSelectItem(like.id) },
                    isChecked = selectedItems.contains(like.id) // Pass checked state
                )
            }
        }

        // LaunchedEffect를 LazyColumn 바깥으로 이동, likes.isNotEmpty() 검사 추가.
        val isAtBottom = remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != null && lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1
            }
        }

        LaunchedEffect(isAtBottom.value) {
            if (isAtBottom.value && likes.isNotEmpty()) { // likes.isNotEmpty() 조건 검사
                onLoadMore()
            }
        }
    }
}

@Composable
fun LazyListState.OnLastItemVisible(onLastItemVisible: () -> Unit) {
    val isAtBottom = remember {
        derivedStateOf { // derivedStateOf를 사용하여 최적화
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != null && lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(isAtBottom.value) { // LaunchedEffect를 사용해서 recomposition시에도 감지
        if (isAtBottom.value) {
            onLastItemVisible()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeSentListComponent(
    likes: List<LikeItemUiState>,
    onItemLongClicked: (Int) -> Unit,
    isDeleteMode: Boolean,
    selectedItems: Set<Int>,
    onSelectItem: (Int) -> Unit,
    listState: LazyListState,
    isRefreshing: Boolean, // 추가
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
) {
    PullToRefreshBox( // LazyColumn을 PullToRefreshBox로 감쌉니다.
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing, // ViewModel의 isRefreshing 상태
        onRefresh = { onRefresh() } // ViewModel의 refreshLikes() 호출
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(likes, key = { it.id }) { like ->
                LikeSentItemComponent(
                    like = like,
                    onItemLongClicked = { onItemLongClicked(like.id) },
                    isDeleteMode = isDeleteMode,
                    isChecked = selectedItems.contains(like.id), // Pass checked state
                    onCheckedChange = { onSelectItem(like.id) }
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
            if (isAtBottom.value && likes.isNotEmpty()) { // likes.isNotEmpty() 조건 검사
                onLoadMore()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeMatchedListComponent(
    likes: List<LikeItemUiState>,
    onItemLongClicked: (Int) -> Unit,
    isDeleteMode: Boolean,
    selectedItems: Set<Int>,
    onSelectItem: (Int) -> Unit,
    listState: LazyListState,
    isRefreshing: Boolean, // 추가
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit
) {
    PullToRefreshBox( // LazyColumn을 PullToRefreshBox로 감쌉니다.
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing, // ViewModel의 isRefreshing 상태
        onRefresh = { onRefresh() } // ViewModel의 refreshLikes() 호출
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            items(likes, key = { it.id }) { like ->
                LikeMatchedItemComponent(
                    like = like,
                    onItemLongClicked = { onItemLongClicked(like.id) },
                    isDeleteMode = isDeleteMode,
                    isChecked = selectedItems.contains(like.id), // Pass checked state
                    onCheckedChange = { onSelectItem(like.id) }
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
            if (isAtBottom.value && likes.isNotEmpty()) { // likes.isNotEmpty() 조건 검사
                onLoadMore()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LikeReceivedItemComponent(
    like: LikeItemUiState,
    onItemLongClicked: () -> Unit,
    isDeleteMode: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElevatedCard( // Material3 Card
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable( // clickable 대신 combinedClickable 사용
                onClick = {
                    if (isDeleteMode) {
                        onCheckedChange(isChecked)
                    }
                },
                onLongClick = { onItemLongClicked() }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDeleteMode) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.profile_sample), // Replace with actual image loading
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LikeSentItemComponent(
    like: LikeItemUiState,
    onItemLongClicked: () -> Unit,
    isDeleteMode: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable( // clickable 대신 combinedClickable 사용
                onClick = {
                    if (isDeleteMode) {
                        onCheckedChange(isChecked)
                    }
                },
                onLongClick = { onItemLongClicked() }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDeleteMode) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
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
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LikeMatchedItemComponent(
    like: LikeItemUiState,
    onItemLongClicked: () -> Unit,
    isDeleteMode: Boolean,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable( // clickable 대신 combinedClickable 사용
                onClick = {
                    if (isDeleteMode) {
                        onCheckedChange(isChecked)
                    }
                },
                onLongClick = { onItemLongClicked() }
            )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDeleteMode) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.profile_sample), // Replace with actual image loading
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
                onClick = { /* TODO: Handle "Send Message" action */ },
            ) {
                Text("メッセージを送る")
            }
        }
    }
}

// 삭제모드 활성화시 보여지는 뷰
@Composable
fun LikeDeleteModeBarComponent(
    selectedItems: Set<Int>,
    receivedLikes: List<LikeItemUiState>,
    sentLikes: List<LikeItemUiState>,
    matchedLikes: List<LikeItemUiState>,
    selectedTab: LikeTab,
    onSelectAllItems: () -> Unit,
    onConfirmDeleteSelectedItems: () -> Unit
) {
    val allItemsInCurrentTab = when (selectedTab) {
        LikeTab.RECEIVED -> receivedLikes
        LikeTab.SENT -> sentLikes
        LikeTab.MATCHED -> matchedLikes
    }.map { it.id }

    // Check if all items in the current tab are selected
    val areAllSelected = selectedItems.containsAll(allItemsInCurrentTab)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //모두삭제 체크박스
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onSelectAllItems()
            }
        ) {
            Checkbox(
                checked = areAllSelected,
                onCheckedChange = { onSelectAllItems() }
            )
            Text(text = "すべて削除")
        }

        //삭제하기 버튼
        Button(
            onClick = {
                onConfirmDeleteSelectedItems()
            },
            enabled = selectedItems.isNotEmpty(), // Disable if nothing is selected

        ) {
            Text(text = "削除する")
        }
    }
}

//삭제 확인 다이얼로그
@Composable
fun LikeDeleteConfirmDialogComponent(
    onConfirmSingleDelete: () -> Unit, // 단일 삭제 콜백
    onConfirmMultiDelete: (() -> Unit)? = null, // 다중 삭제 콜백 (선택 사항)
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("削除確認") },
        text = { Text("本当に削除しますか？") },
        confirmButton = {
            TextButton(
                onClick = {
                    if (onConfirmMultiDelete != null) {
                        onConfirmMultiDelete()
                    } else {
                        onConfirmSingleDelete()
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

// Preview (Simplified)
@Preview(showBackground = true)
@Composable
fun LikeReceivedItemComponentPreview() {
    LikeReceivedItemComponent(
        like = LikeItemUiState(
            id = 1,
            thumbnail = "https://via.placeholder.com/150",
            nickname = "Test User",
            age = 25,
            introduction = "This is a sample introduction.",
            receivedTime = System.currentTimeMillis(), // 현재 시간 또는 임의의 값
            isChecked = false, // 기본값 명시해도 좋음
            isRefreshing = false // 기본값 명시해도 좋음
        ),
        onItemLongClicked = {},
        isDeleteMode = false,
        isChecked = false,
        onCheckedChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LikeScreenPreview() {
    val viewModel: LikeScreenViewModel = hiltViewModel()
    LikeScreen(viewModel = viewModel)
}