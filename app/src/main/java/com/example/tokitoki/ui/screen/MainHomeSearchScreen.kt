package com.example.tokitoki.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tokitoki.ui.model.UserUiModel
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiStateData
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.state.currentData
import com.example.tokitoki.ui.viewmodel.MainHomeSearchViewModel

@Composable
fun MainHomeSearchScreen(
    viewModel: MainHomeSearchViewModel = hiltViewModel(),
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentData = uiState.currentData()

    LaunchedEffect(Unit) {
        if (currentData.state == MainHomeSearchState.NOTHING)
            viewModel.fetchUsers(showLoading = true)
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainHomeSearchUiEvent.UserSelected -> {
                    // 현재 상태에서 최신 데이터를 가져옴
                    val latestData = uiState.currentData()
                    if (latestData.users.isNotEmpty() && event.index < latestData.users.size) {
                        val selectedUser = latestData.users[event.index]
                        viewModel.addUserIdsToCache()
                        onNavigateToUserDetail(
                            selectedUser.id,
                            "MainHomeSearchScreen"
                        )
                    }
                }

                is MainHomeSearchUiEvent.OrderSelected -> {
                    viewModel.changeOrderType(event.orderType)
                    val state =
                        if (event.orderType == OrderType.LOGIN) uiState.dataByLogin.state else uiState.dataByRegister.state

                    if (state == MainHomeSearchState.NOTHING)
                        viewModel.fetchUsers(showLoading = true)
                }

                is MainHomeSearchUiEvent.Error -> {

                }

                MainHomeSearchUiEvent.LoadMore -> {
                    viewModel.fetchUsers()
                }

                MainHomeSearchUiEvent.OnRefreshing -> {
                    viewModel.resetState()
                    viewModel.onPullToRefreshTrigger()
                }
            }
        }
    }

    MainHomeSearchContents(
        orderType = uiState.orderType,
        data = currentData,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeSearchContents(
    orderType: OrderType,
    data: MainHomeSearchUiStateData,
    onEvent: (MainHomeSearchUiEvent) -> Unit
) {
    val lazyGridStateByLogin = rememberLazyGridState()
    val lazyGridStateByRegist = rememberLazyGridState()

    val curLazyGridState =
        if (orderType == OrderType.LOGIN) lazyGridStateByLogin else lazyGridStateByRegist

    var isSortMenuVisible by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }

    // 스크롤 상태 감지
    LaunchedEffect(curLazyGridState) {
        snapshotFlow { curLazyGridState.firstVisibleItemScrollOffset }
            .collect { currentOffset ->
                isSortMenuVisible =
                    currentOffset < previousScrollOffset || curLazyGridState.firstVisibleItemIndex == 0
                previousScrollOffset = currentOffset
            }
    }

    val rememberedUiStateData by rememberUpdatedState(data)

    // 🔥 무한 스크롤 트리거 - 중복 호출 방지 로직 추가
    LaunchedEffect(curLazyGridState) {
        snapshotFlow { curLazyGridState.layoutInfo }
            .collect { layoutInfo ->
                val totalItemCount = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index

                // ✅ 데이터가 증가했을 때만 LoadMore 실행
                if (lastVisibleItemIndex != null &&
                    lastVisibleItemIndex >= totalItemCount - 5 && // 마지막에서 5번째
                    rememberedUiStateData.state != MainHomeSearchState.NOTHING &&
                    rememberedUiStateData.state != MainHomeSearchState.LOADING && // 로딩 중이 아니며
                    !rememberedUiStateData.isLastPage // 마지막 페이지가 아닐 때
                ) {
                    println("CYHH snapshotFlow: Loading more items...")
                    onEvent(MainHomeSearchUiEvent.LoadMore) // 추가 데이터 요청
                }
            }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = data.isRefreshing,
        onRefresh = { onEvent(MainHomeSearchUiEvent.OnRefreshing) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 데이터 표시 그리드
            MainHomeSearchGrid(
                data = data,
                users = data.users,
                lazyGridState = curLazyGridState,
                onUserSelected = { index ->
                    onEvent(MainHomeSearchUiEvent.UserSelected(index))
                }
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
                        .padding(8.dp),
                    currentOrder = orderType,
                    onOrderSelected = { orderType ->
                        onEvent(MainHomeSearchUiEvent.OrderSelected(orderType))
                    }
                )
            }
        }
    }
}

@Composable
fun MainHomeSearchGrid(
    data: MainHomeSearchUiStateData,
    users: List<UserUiModel>,
    lazyGridState: LazyGridState,
    onUserSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        if (data.showShimmerEffect) {
            items(10) {
                ShimmerGridItem()
            }
        } else if (data.state == MainHomeSearchState.LOADING || data.state == MainHomeSearchState.COMPLETED) {
            itemsIndexed(users) { index, user ->
                MainHomeSearchGridItem(
                    user = user,
                    onClick = { onUserSelected(index) }
                )
            }
        } else {

        }
    }
}

@Composable
fun MainHomeSearchGridItem(
    user: UserUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .aspectRatio(0.7f)
            .clickable { onClick() },
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
                painter = rememberAsyncImagePainter(user.thumbnailUrl),
                contentDescription = "",
            )
        }

        Text(
            text = "${user.age}歳",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ShimmerAnimation(modifier: Modifier = Modifier): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val translateAnim = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.Gray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 200f, translateAnim.value + 200f)
    )

    return modifier.background(brush)
}

@Composable
fun ShimmerGridItem() {
    Column(
        modifier = Modifier
            .aspectRatio(0.7f)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .then(ShimmerAnimation())
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .then(ShimmerAnimation())
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