package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.MainHomeTab
import com.example.tokitoki.ui.state.MainHomeUiEvent
import com.example.tokitoki.ui.state.MainHomeUiState
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.viewmodel.MainHomeSearchViewModel
import com.example.tokitoki.ui.viewmodel.MainHomeViewModel

@Composable
fun MainHomeScreen(
    viewModel: MainHomeViewModel = hiltViewModel()
) {
    // StateFlow 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // SharedFlow 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            // 필요한 이벤트 처리 (현재는 없음)
        }
    }

    // UI 전달
    MainHomeContents(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@Composable
fun MainHomeContents(
    uiState: MainHomeUiState,
    onEvent: (MainHomeUiEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MainHomeTab.entries.forEach { tab ->
                    MainHomeTabItem(
                        tab = tab,
                        isSelected = uiState.selectedTab == tab,
                        onClick = { onEvent(MainHomeUiEvent.TabSelected(tab)) }
                    )
                }
            }

            if(uiState.selectedTab == MainHomeTab.SEARCH) {

            }
        }

        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
        )

        // 🟦 탭 콘텐츠 표시
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (uiState.selectedTab) {
                MainHomeTab.SEARCH -> MainHomeSearchScreen()
                MainHomeTab.PICKUP -> MainHomePickupScreen()
                MainHomeTab.MY_TAG -> MainHomeMyTagScreen()
            }
        }
    }
}

@Composable
fun MainHomeTabItem(
    tab: MainHomeTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (tab) {
                MainHomeTab.SEARCH -> "検索"
                MainHomeTab.PICKUP -> "pick up!"
                MainHomeTab.MY_TAG -> "マイタグ"
            },
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}

@Composable
fun MainHomeSearchScreen(
    viewModel: MainHomeSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainHomeSearchUiEvent.UserSelected -> {
                    // Navigate to User Detail (Placeholder)
                    // TODO: Implement navigation logic here
                }

                is MainHomeSearchUiEvent.OrderSelected -> {

                }
            }
        }
    }

    MainHomeSearchContents(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@Composable
fun MainHomeSearchContents(
    uiState: MainHomeSearchUiState,
    onEvent: (MainHomeSearchUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        when (uiState.state) {
            MainHomeSearchState.LOADING -> {
                ShimmerGridPlaceholder()
            }

            MainHomeSearchState.INITIALIZED -> {
                UserGrid(
                    users = listOf(), // Replace with actual user data
                    onUserSelected = { index ->
                        onEvent(MainHomeSearchUiEvent.UserSelected(index))
                    }
                )
            }

            else -> {}
        }
    }
}

@Composable
fun SortMenu(
    modifier: Modifier,
    currentOrder: OrderType,
    onOrderSelected: (OrderType) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val iconRes = when (currentOrder) {
        OrderType.LOGIN -> Icons.Default.AccountBox
        OrderType.REGISTRATION -> Icons.Default.DateRange
    }

    IconButton(
        modifier = modifier,
        onClick = { showDialog = true }
    ) {
        Icon(iconRes, contentDescription = "Sort")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("정렬 선택") },
            text = {
                Column {
                    TextButton(onClick = {
                        onOrderSelected(OrderType.LOGIN)
                        showDialog = false
                    }) {
                        Text("로그인순")
                    }
                    TextButton(onClick = {
                        onOrderSelected(OrderType.REGISTRATION)
                        showDialog = false
                    }) {
                        Text("등록순")
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun UserGrid(
    users: List<UserProfile>,
    onUserSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(users) { index, user ->
            UserProfileItem(user = user, onClick = { onUserSelected(index) })
        }
    }
}

@Composable
fun UserProfileItem(
    user: UserProfile,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .clickable { onClick() }
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("IMG")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Age: ${user.age}", fontSize = 14.sp)
    }
}

@Composable
fun ShimmerGridPlaceholder() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
        }
    }
}

// Placeholder data class
data class UserProfile(
    val age: Int
)

@Composable
fun MainHomePickupScreen() {
    Text("PICKUP")
}

@Composable
fun MainHomeMyTagScreen() {
    Text("MYTAG")
}