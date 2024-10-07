package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.model.InterestItemUiModel
import com.example.tokitoki.ui.screen.components.icons.TkRoundedIcon
import com.example.tokitoki.ui.state.AboutMeInterestEvent
import com.example.tokitoki.ui.state.AboutMeInterestState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeInterestViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeInterestScreen(
    onAboutMeSecondScreen: () -> Unit = {},
    onAboutMeThirdScreen: () -> Unit = {},
    viewModel: AboutMeInterestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 3 }

    AboutMeInterestContents(
        uiState = uiState,
        coroutineScope = coroutineScope,
        pagerState = pagerState,
        aboutMeInterestAction = viewModel::aboutMeInterestAction,
    )

    LaunchedEffect(true) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeInterestEvent.ACTION -> {
                    when (event.action) {
                        is AboutMeInterestAction.SelectedTab -> {
                            pagerState.animateScrollToPage(event.action.index)
                        }
                        AboutMeInterestAction.DIALOG_OK -> {}
                        AboutMeInterestAction.NEXT -> {}
                        AboutMeInterestAction.NOTHING -> {}
                        AboutMeInterestAction.PREVIOUS -> {}
                    }
                }

                AboutMeInterestEvent.NOTHING -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeInterestContents(
    uiState: AboutMeInterestState = AboutMeInterestState(),
    pagerState: PagerState,
    aboutMeInterestAction: (AboutMeInterestAction) -> Unit = {},
    coroutineScope: CoroutineScope,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LocalColor.current.white)
    ) {
        TkRoundedIcon(
            iconRes = R.drawable.baseline_kitesurfing_24
        )
        AboutMeInterestTitle()
        AboutMeInterestPagerTab(
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            tabs = uiState.categoryList,
            aboutMeInterestAction = aboutMeInterestAction
        )
        AboutMeInterestPager(
            modifier = Modifier.weight(1f),
            pagerState = pagerState,
            hobbyList = uiState.hobbyList,
            lifeStyleList = uiState.lifeStyleList,
            valuesList = uiState.valuesList,
            coroutineScope = coroutineScope
        )
    }
}

@Composable
fun AboutMeInterestTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.about_me_interest_title1),
            fontSize = 15.sp
        )
        Text(
            text = stringResource(R.string.about_me_interest_title2),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.about_me_interest_title3),
            fontSize = 10.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeInterestPagerTab(
    modifier: Modifier = Modifier,
    tabs: List<String> = listOf("趣味", "ライフスタイル", "価値観"),
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    aboutMeInterestAction: (AboutMeInterestAction) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val tabWidth = maxWidth / tabs.size

        val indicatorOffset by animateDpAsState(
            targetValue = pagerState.currentPage * tabWidth,
            label = "",
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch {
                                    aboutMeInterestAction(AboutMeInterestAction.SelectedTab(index))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (pagerState.currentPage == index) LocalColor.current.blue else Color.LightGray,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(tabWidth)
                        .height(2.dp)
                        .background(LocalColor.current.blue)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeInterestPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    hobbyList: List<InterestItemUiModel> = listOf(),
    lifeStyleList: List<InterestItemUiModel> = listOf(),
    valuesList: List<InterestItemUiModel> = listOf(),
    coroutineScope: CoroutineScope
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        when (page) {
            0 -> AboutMeInterestHobbyPage(
                hobbyList = hobbyList
            )

            1 -> AboutMeInterestLifestylePage(
                lifeStyleList = lifeStyleList
            )

            2 -> AboutMeInterestValuesPage(
                valuesList = valuesList
            )
        }
    }
}

@Composable
fun AboutMeInterestHobbyPage(
    modifier: Modifier = Modifier,
    hobbyList: List<InterestItemUiModel> = listOf()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(hobbyList) { item ->
            AboutMeInterestGridItem(
                modifier = Modifier.padding(4.dp),
                title = item.title,
                url = item.url
            )
        }
    }
}

@Composable
fun AboutMeInterestLifestylePage(
    modifier: Modifier = Modifier,
    lifeStyleList: List<InterestItemUiModel> = listOf()
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "lifestyle")
    }
}

@Composable
fun AboutMeInterestValuesPage(
    modifier: Modifier = Modifier,
    valuesList: List<InterestItemUiModel> = listOf()
) {
    // 가치관 페이지의 내용
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "kachikan")
    }
}

@Composable
fun AboutMeInterestGridItem(
    modifier: Modifier = Modifier,
    title: String = "",
    url: String = "",
    showBadge: Boolean = false,
    badgeNum: Int = 0,
) {
    val colorStops = arrayOf(
        0.1f to Color.Transparent,
        1f to Color.Black,
    )

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .then(
                if (showBadge) {
                    Modifier.border(
                        width = 4.dp,
                        color = LocalColor.current.blue,
                        shape = RoundedCornerShape(30.dp)
                    )
                } else {
                    Modifier
                }
            )
            .border(
                width = 4.dp,
                color = LocalColor.current.blue,
                shape = RoundedCornerShape(30.dp)
            ),
    ) {
        AsyncImage(
            model = url,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f),
        )

        if (showBadge) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(maxWidth / 5)
                    .clip(CircleShape)
                    .background(LocalColor.current.blue)
                    .align(Alignment.TopEnd)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "$badgeNum",
                    fontSize = 13.sp,
                    color = LocalColor.current.white
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Brush.verticalGradient(colorStops = colorStops))
                .align(Alignment.BottomCenter)
        )

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            color = LocalColor.current.white,
            fontSize = 20.sp,
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeInterestContentsPreview() {
    val coroutineScope = rememberCoroutineScope()

    TokitokiTheme {
        AboutMeInterestContents(
            pagerState = rememberPagerState { 3 },
            coroutineScope = coroutineScope
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeInterestItemPreview() {
    TokitokiTheme {
        AboutMeInterestGridItem(
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            showBadge = true
        )
    }
}
