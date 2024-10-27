package com.example.tokitoki.ui.screen.components.dialog

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TkBottomDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current // 밀도 가져오기
    val bufferPadding = 30.dp
    var dialogHeight by remember { mutableStateOf(0.dp) } // 콘텐츠 높이 저장
    val offsetY = remember { Animatable(0f) }
    val backgroundAlpha = remember { Animatable(0f) }
    var contentAlpha by remember { mutableFloatStateOf(0f) } // 초기 렌더링에서 다이얼로그를 숨김

    val coroutineScope = rememberCoroutineScope()

    val dragState = rememberDraggableState { delta ->
        coroutineScope.launch {
            val newOffset =
                (offsetY.value + delta).coerceIn(0f, with(density) { dialogHeight.toPx() })
            offsetY.snapTo(newOffset)
        }
    }

    LaunchedEffect(isVisible, dialogHeight) {
        if (isVisible) {
            if (dialogHeight > 0.dp) {
                // 초기 설정: 다이얼로그를 화면 아래로 배치하고 보이지 않게 설정
                offsetY.snapTo(with(density) { dialogHeight.toPx() + bufferPadding.toPx() })
                contentAlpha = 1f // 다이얼로그를 화면에 보이게 함

                launch {
                    offsetY.animateTo(0f, animationSpec = tween(durationMillis = 300))
                }
                launch {
                    backgroundAlpha.animateTo(0.5f, animationSpec = tween(durationMillis = 300))
                }
            }
        } else {
            // 다이얼로그가 사라질 때 애니메이션 후 설정 초기화
            val offsetJob = launch {
                offsetY.animateTo(
                    with(density) { dialogHeight.toPx() + bufferPadding.toPx() },
                    animationSpec = tween(durationMillis = 300)
                )
            }
            val alphaJob = launch {
                backgroundAlpha.animateTo(0f, animationSpec = tween(durationMillis = 300))
            }
            offsetJob.join()
            alphaJob.join()

            contentAlpha = 0f // 다이얼로그를 다시 숨김
        }
    }

    if (isVisible || contentAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = backgroundAlpha.value))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = with(density) { offsetY.value.toDp() })
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color.White,
                    )
                    .padding(16.dp)
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            val thresholdVelocity = 1500f

                            if (velocity > thresholdVelocity) {
                                onDismiss()
                            } else {
                                offsetY.animateTo(
                                    0f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                            }
                        }
                    )
                    .onGloballyPositioned { coordinates ->
                        dialogHeight = with(density) { coordinates.size.height.toDp() }
                        Log.d("CYHH", "Actual content height: $dialogHeight")
                    }
            ) {
                content()
            }
        }
    }
}