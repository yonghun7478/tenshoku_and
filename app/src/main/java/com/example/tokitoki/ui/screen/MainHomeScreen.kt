package com.example.tokitoki.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tokitoki.R
import com.example.tokitoki.ui.model.PickupUserItem
import com.example.tokitoki.ui.model.UserUiModel
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.MainHomeTab
import com.example.tokitoki.ui.state.MainHomeUiEvent
import com.example.tokitoki.ui.state.MainHomeUiState
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.MainHomePickupViewModel
import com.example.tokitoki.ui.viewmodel.MainHomeSearchViewModel
import com.example.tokitoki.ui.viewmodel.MainHomeViewModel
import com.example.tokitoki.ui.viewmodel.PickupUserViewModel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

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

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainHomeSearchUiEvent.UserSelected -> {
                    println("userSelected ${event.index}")
                }

                is MainHomeSearchUiEvent.OrderSelected -> {
                    viewModel.resetState(orderType = event.orderType)
                    viewModel.fetchUsers()
                }

                is MainHomeSearchUiEvent.Error -> {

                }

                MainHomeSearchUiEvent.LoadMore -> {
                    viewModel.fetchUsers()
                }

                MainHomeSearchUiEvent.OnRefreshing -> {
                    viewModel.onPullToRefreshTrigger()
                }
            }
        }
    }

    MainHomeSearchContents(
        uiState = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeSearchContents(
    uiState: MainHomeSearchUiState,
    onEvent: (MainHomeSearchUiEvent) -> Unit
) {
    val lazyGridState = rememberLazyGridState()
    var isSortMenuVisible by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }

    // 스크롤 상태 감지
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemScrollOffset }
            .collect { currentOffset ->
                isSortMenuVisible =
                    currentOffset < previousScrollOffset || lazyGridState.firstVisibleItemIndex == 0
                previousScrollOffset = currentOffset
            }
    }

    // 무한 스크롤 트리거
    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo }
            .collect { layoutInfo ->
                val totalItemCount = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index

                if (lastVisibleItemIndex != null && totalItemCount > 0 &&
                    lastVisibleItemIndex >= totalItemCount - 5 && // 마지막에서 5번째
                    uiState.state != MainHomeSearchState.NOTHING &&
                    uiState.state != MainHomeSearchState.LOADING && // 로딩 중이 아니며
                    !uiState.isLastPage // 마지막 페이지가 아닐 때
                ) {
                    onEvent(MainHomeSearchUiEvent.LoadMore) // 추가 데이터 요청
                }
            }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = uiState.isRefreshing,
        onRefresh = { onEvent(MainHomeSearchUiEvent.OnRefreshing) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 데이터 표시 그리드
            MainHomeSearchGrid(
                uiState = uiState,
                users = if (uiState.orderType == OrderType.LOGIN) uiState.usersOrderByLogin else uiState.usersOrderByRegist,
                lazyGridState = lazyGridState,
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
                    currentOrder = uiState.orderType,
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
    uiState: MainHomeSearchUiState,
    users: List<UserUiModel>,
    lazyGridState: LazyGridState,
    onUserSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.state == MainHomeSearchState.LOADING) {
            items(10) {
                ShimmerGridItem()
            }
        } else if (uiState.state == MainHomeSearchState.COMPLETED) {
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

@Composable
fun MainHomePickupScreen(
    viewModel: PickupUserViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 카드 스택
        DraggableCardStack(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp), // 버튼 영역을 고려하여 패딩 추가
            cardStates = uiState.users,
            onCardRemoved = { removedCard ->
                when (removedCard.cardDirection.value) {
                    CardDirection.AUTO_LEFT, CardDirection.LEFT -> {
                        viewModel.dislikePickupUser()
                    }

                    CardDirection.AUTO_RIGHT, CardDirection.RIGHT -> {
                        viewModel.likePickupUser()
                    }

                    CardDirection.NONE -> {

                    }
                }
            },
        )

        // 좋아요 / 싫어요 버튼
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter) // 화면 하단 중앙에 배치
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.triggerAutoRemove(CardDirection.AUTO_LEFT)
                }
            ) {
                Text("싫어요")
            }

            Button(
                onClick = {
                    viewModel.triggerAutoRemove(CardDirection.AUTO_RIGHT)
                }
            ) {
                Text("좋아요")
            }
        }
    }
}

@Composable
fun DraggableCardStack(
    modifier: Modifier = Modifier, // Modifier 추가
    cardStates: List<PickupUserItem>,
    cardWidth: Dp = 300.dp,
    cardHeight: Dp = 400.dp,
    onCardRemoved: (PickupUserItem) -> Unit,
    cardContent: @Composable (PickupUserItem) -> Unit = { DefaultCardContent(it) }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        cardStates.reversed().forEachIndexed { index, cardState ->
            val isFrontCard = index == cardStates.lastIndex

            DraggableCard(
                cardState = cardState,
                isFrontCard = isFrontCard,
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                onRemove = { onCardRemoved(cardState) },
                content = { cardContent(cardState) }
            )
        }
    }
}

@Composable
fun DraggableCard(
    cardState: PickupUserItem,
    isFrontCard: Boolean,
    cardWidth: Dp,
    cardHeight: Dp,
    onRemove: () -> Unit,
    content: @Composable () -> Unit
) {
    val animatedOffset by animateOffsetAsState(
        targetValue = cardState.offset.value,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing), // 1초 동안 애니메이션
        label = "OffsetAnimation"
    )

    val animatedRotation by animateFloatAsState(
        targetValue = cardState.rotation.value,
        label = "RotationAnimation"
    )


    // 회전 임계값
    val removalAngle = 15f

    // 현재 드래그 방향 판별
    val isRightDrag = animatedRotation > 15f
    val isLeftDrag = animatedRotation < -15f

    // 좌측 동그라미 색상
    val leftCircleBorderColor = if (isLeftDrag) Color.Black else Color.Transparent
    val leftCircleFillColor = if (isLeftDrag) Color.White else Color.Transparent
    val leftIconFillColor = if (isLeftDrag) Color.Blue else Color.White

    // 우측 동그라미 색상
    val rightCircleBorderColor = if (isRightDrag) Color.Black else Color.Transparent
    val rightCircleFillColor = if (isRightDrag) Color.White else Color.Transparent
    val rightIconFillColor = if (isRightDrag) Color.Red else Color.White

    // 카드 제거 애니메이션 처리
    LaunchedEffect(cardState.isOut.value) {
        if (cardState.isOut.value) {
            delay(300) // 애니메이션 완료 대기
            onRemove()
        }
    }

    LaunchedEffect(cardState.cardDirection.value) {
        if (cardState.cardDirection.value == CardDirection.AUTO_RIGHT || cardState.cardDirection.value == CardDirection.AUTO_LEFT) {
            cardState.isOut.value = true
            cardState.offset.value = Offset(
                x = when (cardState.cardDirection.value) {
                    CardDirection.AUTO_RIGHT -> 2000f
                    CardDirection.AUTO_LEFT -> -2000f
                    else -> 0f
                },
                y = -2000f
            )
            delay(300) // 애니메이션 완료 대기
            onRemove()
        }
    }


    Box(
        modifier = Modifier
            .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
            .size(cardWidth, cardHeight)
            .rotate(animatedRotation)
            .zIndex(if (isFrontCard) 1f else 0f)
            .pointerInput(isFrontCard) {
                if (isFrontCard) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            cardState.offset.value += dragAmount
                            cardState.rotation.value = calculateRotation(cardState.offset.value.x)
                        },
                        onDragEnd = {
                            if (animatedRotation.absoluteValue > removalAngle) {
                                // 카드 제거 설정
                                cardState.isOut.value = true
                                cardState.offset.value = Offset(
                                    x = if (animatedRotation > 0) 2000f else -2000f,
                                    y = -2000f
                                )
                                cardState.cardDirection.value =
                                    if (animatedRotation > 0) CardDirection.RIGHT else CardDirection.LEFT
                            } else {
                                // 원래 위치로 복귀
                                cardState.offset.value = Offset.Zero
                                cardState.rotation.value = 0f
                            }
                        }
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 카드 콘텐츠
            content()

            // 배경 오버레이
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(calculateBackgroundColor(animatedRotation))
            )

            // 좌측 하단 동그라미와 아이콘
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(50.dp)
                    .background(leftCircleFillColor, shape = CircleShape)
                    .border(2.dp, leftCircleBorderColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Left Touch Icon",
                    tint = leftIconFillColor,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .rotate(180f)
                )
            }

            // 우측 하단 동그라미와 아이콘
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(50.dp)
                    .background(rightCircleFillColor, shape = CircleShape)
                    .border(2.dp, rightCircleBorderColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Right Touch Icon",
                    tint = rightIconFillColor,
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
        }
    }
}

@Composable
fun DefaultCardContent(item: PickupUserItem) {
    Image(
        painter = rememberAsyncImagePainter(item.thumbnail),
        contentDescription = "Loaded Image",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

// 회전 각도 기반 덮어씌우는 색상 계산
fun calculateBackgroundColor(rotation: Float): Color {
    val maxRotation = 15f
    val normalizedRotation = (rotation / maxRotation).coerceIn(-0.5f, 0.5f)
    return when {
        normalizedRotation > 0 -> Color.Red.copy(alpha = normalizedRotation) // 시계방향 -> 빨간색
        normalizedRotation < 0 -> Color.Blue.copy(alpha = -normalizedRotation) // 반시계방향 -> 파란색
        else -> Color.Transparent // 중립
    }
}

// 회전 각도 계산
fun calculateRotation(offsetX: Float, maxRotation: Float = 30f): Float {
    val screenWidth = 1080f
    val halfWidth = screenWidth / 2
    val normalizedOffset = offsetX / halfWidth
    return (maxRotation * normalizedOffset).coerceIn(-maxRotation, maxRotation)
}

// 카드 상태 데이터 클래스
data class CardState(
    val color: Color,
    val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val rotation: MutableState<Float> = mutableStateOf(0f),
    val isOut: MutableState<Boolean> = mutableStateOf(false),
    val cardDirection: MutableState<CardDirection> = mutableStateOf(CardDirection.NONE) // 자동 제거 방향 추가
)

enum class CardDirection {
    AUTO_LEFT, AUTO_RIGHT, NONE, LEFT, RIGHT
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