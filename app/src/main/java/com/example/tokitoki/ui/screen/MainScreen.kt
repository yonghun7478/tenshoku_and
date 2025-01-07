package com.example.tokitoki.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.tokitoki.ui.state.MainBottomItem
import com.example.tokitoki.ui.state.MainUiEvent
import com.example.tokitoki.ui.state.MainUiState
import com.example.tokitoki.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    // StateFlow를 사용하여 UI 상태를 관찰
    val uiState by viewModel.uiState.collectAsState()

    // SharedFlow 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainUiEvent.SelectBottomItem -> {

                }
            }
        }
    }

    // UI 상태를 MainContents에 전달
    MainContents(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@Composable
fun MainContents(
    uiState: MainUiState,
    onEvent: (MainUiEvent) -> Unit
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
                    MainHomeContents()
                }

                MainBottomItem.LIKE -> {
                    MainLikeContents()
                }

                MainBottomItem.MESSAGE -> {
                    MainMessageContents()
                }

                MainBottomItem.MY_PAGE -> {
                    MypageContents()
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
    NavigationBar {
        MainBottomItem.values().forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text(item.name) }
            )
        }
    }
}

@Composable
fun MainHomeContents() {
    Text("MainHomeContents")
}

@Composable
fun MainLikeContents() {
    Text("MainLikeContents")
}

@Composable
fun MainMessageContents() {
    Text("MainMessageContents")
}

@Composable
fun MypageContents() {
    Text("MypageContents")
}
