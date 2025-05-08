package com.example.tokitoki.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.tokitoki.R
import com.example.tokitoki.ui.model.PickupUserItem
import com.example.tokitoki.ui.model.UserUiModel
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.state.MainHomeSearchState
import com.example.tokitoki.ui.state.MainHomeSearchUiEvent
import com.example.tokitoki.ui.state.MainHomeSearchUiState
import com.example.tokitoki.ui.state.MainHomeSearchUiStateData
import com.example.tokitoki.ui.state.MainHomeTab
import com.example.tokitoki.ui.state.MainHomeUiEvent
import com.example.tokitoki.ui.state.MainHomeUiState
import com.example.tokitoki.ui.state.OrderType
import com.example.tokitoki.ui.state.PickupUserState
import com.example.tokitoki.ui.state.PickupUserUiState
import com.example.tokitoki.ui.state.currentData
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.MainHomeMyTagViewModel
import com.example.tokitoki.ui.viewmodel.MainHomeSearchViewModel
import com.example.tokitoki.ui.viewmodel.MainHomeViewModel
import com.example.tokitoki.ui.viewmodel.PickupUserViewModel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun MainHomeScreen(
    viewModel: MainHomeViewModel = hiltViewModel(),
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> }
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
        onEvent = { viewModel.onEvent(it) },
        onNavigateToUserDetail = onNavigateToUserDetail
    )
}

@Composable
fun MainHomeContents(
    uiState: MainHomeUiState,
    onEvent: (MainHomeUiEvent) -> Unit,
    onNavigateToUserDetail: (String, String) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .zIndex(2f),
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
        ) {
            when (uiState.selectedTab) {
                MainHomeTab.SEARCH -> MainHomeSearchScreen(
                    onNavigateToUserDetail = onNavigateToUserDetail
                )
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