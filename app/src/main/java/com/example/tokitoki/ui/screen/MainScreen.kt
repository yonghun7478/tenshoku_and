package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.ui.screen.mypage.MyPageScreen
import com.example.tokitoki.ui.state.MainBottomItem
import com.example.tokitoki.ui.state.MainUiEvent
import com.example.tokitoki.ui.state.MainUiState
import com.example.tokitoki.ui.viewmodel.MainViewModel
import com.example.tokitoki.ui.viewmodel.SharedPickupViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    sharedViewModel: SharedPickupViewModel,
    onAshiatoClick: () -> Unit,
    onFavoriteUsersClick: () -> Unit,
    onIineSitaHitoClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit,
    onNavigateToTagSearch: () -> Unit,
    onNavigateToMyTagList: () -> Unit,
    onNavigateToTagDetail: (String) -> Unit
) {
    // StateFlow를 사용하여 UI 상태를 관찰
    val uiState by viewModel.uiState.collectAsState()

    // SharedFlow 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainUiEvent.SelectBottomItem -> {
                    // 탭이 변경될 때 처리
                }
            }
        }
    }

    // UI 상태를 MainContents에 전달
    MainContents(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) },
        onAshiatoClick = onAshiatoClick,
        onFavoriteUsersClick = onFavoriteUsersClick,
        onIineSitaHitoClick = onIineSitaHitoClick,
        onNavigateToSignIn = onNavigateToSignIn,
        onNavigateToUserDetail = onNavigateToUserDetail,
        onNavigateToTagSearch = onNavigateToTagSearch,
        onNavigateToMyTagList = onNavigateToMyTagList,
        onNavigateToTagDetail = onNavigateToTagDetail,
        sharedViewModel = sharedViewModel
    )
}

@Composable
fun MainContents(
    uiState: MainUiState,
    onEvent: (MainUiEvent) -> Unit,
    onAshiatoClick: () -> Unit,
    onFavoriteUsersClick: () -> Unit,
    onIineSitaHitoClick: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit,
    onNavigateToTagSearch: () -> Unit,
    onNavigateToMyTagList: () -> Unit,
    onNavigateToTagDetail: (String) -> Unit,
    sharedViewModel: SharedPickupViewModel
) {
    Scaffold(
        bottomBar = {
            MainBottomNavigation(
                selectedItem = uiState.selectedBottomItem,
                onItemSelected = { onEvent(MainUiEvent.SelectBottomItem(it)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState.selectedBottomItem) {
                MainBottomItem.HOME -> {
                    MainHomeScreen(
                        onNavigateToUserDetail = onNavigateToUserDetail,
                        onNavigateToTagSearch = onNavigateToTagSearch,
                        onNavigateToMyTagList = onNavigateToMyTagList,
                        onNavigateToTagDetail = onNavigateToTagDetail,
                        sharedViewModel = sharedViewModel
                    )
                }

                MainBottomItem.LIKE -> {
                    LikeScreen()
                }

                MainBottomItem.MESSAGE -> {
                    MessageListScreen()
                }

                MainBottomItem.MY_PAGE -> {
                    MyPageScreen(
                        onAshiatoClick = onAshiatoClick,
                        onFavoriteUsersClick = onFavoriteUsersClick,
                        onIineSitaHitoClick = onIineSitaHitoClick,
                        onNavigateToSignIn = onNavigateToSignIn,
                    )
                }
            }
        }
    }
}

@Composable
fun MainBottomNavigation(
    selectedItem: MainBottomItem,
    onItemSelected: (MainBottomItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // 네비게이션 바 배경색
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainBottomItem.entries.forEach { item ->
            CustomBottomNavigationItem(
                modifier = Modifier.weight(1f),
                item = item,
                isSelected = selectedItem == item,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    modifier: Modifier,
    item: MainBottomItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (item) {
        MainBottomItem.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
        MainBottomItem.LIKE -> if (isSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
        MainBottomItem.MESSAGE -> if (isSelected) Icons.Filled.Email else Icons.Outlined.Email
        MainBottomItem.MY_PAGE -> if (isSelected) Icons.Filled.Person else Icons.Outlined.Person
    }

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = item.label,
                tint = if (isSelected) Color.Black else Color.Gray
            )
        }

        Text(
            text = item.label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}
