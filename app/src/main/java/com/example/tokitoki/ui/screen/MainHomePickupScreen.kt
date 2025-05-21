package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.tokitoki.ui.model.PickupUserItem
import com.example.tokitoki.ui.state.PickupUserState
import com.example.tokitoki.ui.state.PickupUserUiState
import com.example.tokitoki.ui.viewmodel.PickupUserViewModel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun MainHomePickupScreen(
    viewModel: PickupUserViewModel = hiltViewModel(),
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> },
    pickupEvent: PickupEvent? = null,
    onPickupEventProcessed: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // pickupEvent 처리
    LaunchedEffect(pickupEvent) {
        pickupEvent?.let { event ->
            delay(500) // 1초 딜레이 추가
            when (event) {
                is PickupEvent.Like -> viewModel.triggerAutoRemove(CardDirection.AUTO_RIGHT)
                is PickupEvent.Dislike -> viewModel.triggerAutoRemove(CardDirection.AUTO_LEFT)
            }
            onPickupEventProcessed()
        }
    }

    if (uiState.state == PickupUserState.COMPLETE) {
        MainHomePickupContents(
            uiState = uiState,
            onLike = { viewModel.likePickupUser() },
            onDislike = { viewModel.dislikePickupUser() },
            onRefresh = { viewModel.loadPickupUsers() },
            triggerRemove = { viewModel.triggerAutoRemove(it) },
            onNavigateToUserDetail = onNavigateToUserDetail,
            onUserClick = { viewModel.onUserClick(it) }
        )
    }
}

@Composable
fun MainHomePickupContents(
    uiState: PickupUserUiState,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onRefresh: () -> Unit,
    triggerRemove: (CardDirection) -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> },
    onUserClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            fontSize = 20.sp,
            text = "気に入る相手に\nいいねしよう！",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 35.sp
        )

        if (uiState.users.isNotEmpty()) {
            DraggableCardStack(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.7f)
                    .fillMaxWidth(),
                cardStates = uiState.users,
                onCardRemoved = { removedCard ->
                    when (removedCard.cardDirection.value) {
                        CardDirection.AUTO_LEFT, CardDirection.LEFT -> onDislike()
                        CardDirection.AUTO_RIGHT, CardDirection.RIGHT -> onLike()
                        CardDirection.NONE -> {}
                    }
                },
                onCardClick = { userId ->
                    onUserClick(userId)
                    onNavigateToUserDetail(userId, "MainHomePickupScreen")
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "残されたカードはございません。\n 再ロードをおねがいいたします！",
                    textAlign = TextAlign.Center
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp)
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { triggerRemove(CardDirection.AUTO_LEFT) },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Blue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "いまいち", tint = Color.Blue)
                Text("いまいち")
            }

            OutlinedButton(
                onClick = { onRefresh() },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "再ロード", tint = Color.Black)
                Text("再ロード")
            }

            OutlinedButton(
                onClick = { triggerRemove(CardDirection.AUTO_RIGHT) },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Red),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "いいね", tint = Color.Red)
                Text("いいね")
            }
        }
    }
}

@Composable
fun DraggableCardStack(
    modifier: Modifier = Modifier,
    cardStates: List<PickupUserItem>,
    onCardRemoved: (PickupUserItem) -> Unit,
    onCardClick: (String) -> Unit,
    cardContent: @Composable (PickupUserItem, () -> Unit) -> Unit = { item, onClick -> DefaultCardContent(item, onClick) }
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
                onRemove = { onCardRemoved(cardState) },
                onCardClick = { onCardClick(cardState.id) },
                content = { cardContent(cardState, { onCardClick(cardState.id) }) }
            )
        }
    }
}

@Composable
fun DraggableCard(
    cardState: PickupUserItem,
    isFrontCard: Boolean,
    onRemove: () -> Unit,
    onCardClick: () -> Unit,
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
            .fillMaxSize()
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
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)),
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
fun DefaultCardContent(
    item: PickupUserItem,
    onCardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.thumbnail),
            contentDescription = "Loaded Image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Gray.copy(alpha = 0.5f))
                .align(Alignment.BottomCenter)
                .clickable { onCardClick() },
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${item.age}歳",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Text(
                        text = item.location,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
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

enum class CardDirection {
    AUTO_LEFT, AUTO_RIGHT, NONE, LEFT, RIGHT
}