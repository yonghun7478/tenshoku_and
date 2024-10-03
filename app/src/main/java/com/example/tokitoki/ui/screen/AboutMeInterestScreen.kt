package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.screen.components.icons.TkRoundedIcon
import com.example.tokitoki.ui.state.AboutMeInterestEvent
import com.example.tokitoki.ui.state.AboutMeInterestState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeInterestViewModel
import kotlinx.coroutines.launch

@Composable
fun AboutMeInterestScreen(
    onAboutMeSecondScreen: () -> Unit = {},
    onAboutMeThirdScreen: () -> Unit = {},
    viewModel: AboutMeInterestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeInterestContents(
        uiState = uiState,
        aboutMeInterestAction = viewModel::aboutMeInterestAction,
    )

    LaunchedEffect(Unit) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeInterestEvent.ACTION -> {
                    when (event.action) {
                        AboutMeInterestAction.CLICK_INTEREST -> {}
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

@Composable
fun AboutMeInterestContents(
    uiState: AboutMeInterestState = AboutMeInterestState(),
    aboutMeInterestAction: (AboutMeInterestAction) -> Unit = {},
) {
    Column(
        modifier = Modifier.background(
            color = LocalColor.current.white
        )
    ) {
        TkRoundedIcon(
            iconRes = R.drawable.baseline_kitesurfing_24
        )
        AboutMeInterestTitle()
        HorizontalPagerWithTabs()
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

@Composable
fun AboutMeInterestItem(
    modifier: Modifier = Modifier,
    drawableId: Int = R.drawable.dokushou,
    text: String = "test"
) {
    val colorStops = arrayOf(
        0.1f to Color.Transparent,
        1f to Color.Black,
    )

    Box {
        Image(
            painter = painterResource(drawableId),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(30.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(Brush.verticalGradient(colorStops = colorStops))
                .align(Alignment.BottomCenter)
        )

        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            color = LocalColor.current.white,
            fontSize = 20.sp,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithTabs() {
    val pagerState = rememberPagerState { 3 }
    val coroutineScope = rememberCoroutineScope()

    // 탭 제목
    val tabs = listOf("취미", "라이프타일", "가치관")
    val tabWidth = 1f / tabs.size

    // 화면의 너비에 따라 탭 너비를 동적으로 계산
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val tabWidth = maxWidth / tabs.size

        // 현재 페이지에 따른 인디케이터 위치 애니메이션
        val indicatorOffset by animateDpAsState(targetValue = pagerState.currentPage * tabWidth)

        Column(modifier = Modifier.fillMaxSize()) {
            // Row로 탭 UI 생성
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
                            .weight(1f) // 각 탭을 동일하게 1/3로 나눔
                            .clickable(
                                indication = null, // Ripple 효과 제거
                                interactionSource = remember { MutableInteractionSource() } // 클릭 상호작용 소스 (빈 값으로 설정)
                            ) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index) // 탭 클릭 시 해당 페이지로 이동
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

            // 애니메이션 인디케이터 (탭 밑)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {
                // 인디케이터 박스
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset) // 애니메이션으로 인디케이터 이동
                        .width(tabWidth) // 각 탭의 너비에 맞게 인디케이터 너비 설정
                        .height(2.dp)
                        .background(LocalColor.current.blue) // 인디케이터 색상
                )
            }

            // HorizontalPager로 페이지 구성
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> HobbyPage()         // 첫 번째 페이지: 취미
                    1 -> LifestylePage()     // 두 번째 페이지: 라이프스타일
                    2 -> ValuesPage()        // 세 번째 페이지: 가치관
                }
            }
        }
    }
}

@Composable
fun HobbyPage() {
    // 취미 페이지의 내용
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "취미 입력")
    }
}

@Composable
fun LifestylePage() {
    // 라이프스타일 페이지의 내용
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "라이프스타일 입력")
    }
}

@Composable
fun ValuesPage() {
    // 가치관 페이지의 내용
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "가치관 입력")
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeInterestContentsPreview() {
    TokitokiTheme {
        AboutMeInterestContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeInterestItemPreview() {
    TokitokiTheme {
        AboutMeInterestItem()
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHorizontalPagerWithTabs() {
    HorizontalPagerWithTabs()
}