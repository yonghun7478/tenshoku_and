package com.example.tokitoki.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.MainHomeTab
import com.example.tokitoki.ui.state.MainHomeUiEvent
import com.example.tokitoki.ui.state.MainHomeUiState
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .offset(x = 10.dp)
                        .height(30.dp)
                        .then(DrawableSemantics.withDrawableId(resId = R.drawable.pink_heart_logo)),
                    painter = painterResource(id = R.drawable.pink_heart_logo),
                    contentDescription = "TopLogoImage",
                )

                Text(
                    text = stringResource(id = R.string.logo_name),
                    color = LocalColor.current.grayColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
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
    val lazyGridState = rememberLazyGridState()
    var isSortMenuVisible by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // 스크롤 상태 감지
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemScrollOffset }
            .collect { currentOffset ->
                isSortMenuVisible =
                    currentOffset < previousScrollOffset || lazyGridState.firstVisibleItemIndex == 0
                previousScrollOffset = currentOffset
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MainHomeSearchGrid(
            lazyGridState = lazyGridState
        )

        // SortMenu - 스크롤 상태에 따라 표시/숨김
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            visible = isSortMenuVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SortMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun MainHomeSearchGrid(
    lazyGridState: LazyGridState
) {
    // LazyVerticalGrid
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(50) { index ->
            MainHomeSearchGridItem(index)
        }
    }
}

@Composable
fun MainHomeSearchGridItem(index: Int) {
    Column(
        modifier = Modifier
            .aspectRatio(0.7f),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = "",
            )
        }

        Text(
            text = "Item $index",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SortMenu(
    modifier: Modifier = Modifier,
    currentOrder: OrderType = OrderType.LOGIN,
    onOrderSelected: (OrderType) -> Unit = {}
) {
    var selectedOrder by remember { mutableStateOf(currentOrder) }

    val indicatorOffset = remember { Animatable(if (selectedOrder == OrderType.LOGIN) 0f else 51f) }

    LaunchedEffect(selectedOrder) {
        indicatorOffset.animateTo(
            targetValue = if (selectedOrder == OrderType.LOGIN) 0f else 51f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(100.dp)
                .align(Alignment.Center)
                .background(
                    color = Color(0xFFD3D3D3),
                    shape = RoundedCornerShape(50.dp)
                )
                .border(1.dp, Color.Gray, RoundedCornerShape(50.dp))
        ) {
            // 애니메이션으로 이동하는 원
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp)
                    .offset(x = indicatorOffset.value.dp)  // 애니메이션으로 위치 이동
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
            )

            // 아이콘 Row
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            selectedOrder = OrderType.LOGIN
                            onOrderSelected(OrderType.LOGIN)
                        },
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Account"
                )
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            selectedOrder = OrderType.REGISTRATION
                            onOrderSelected(OrderType.REGISTRATION)
                        },
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Date"
                )
            }
        }
    }
}

@Composable
fun MainHomePickupScreen() {
    Text("PICKUP")
}

@Composable
fun MainHomeMyTagScreen() {
    Text("MYTAG")
}

@Preview(showBackground = true)
@Composable
fun MainHomeContentsPreview() {
    TokitokiTheme {
        MainHomeContents(uiState = MainHomeUiState()) {

        }
    }
}