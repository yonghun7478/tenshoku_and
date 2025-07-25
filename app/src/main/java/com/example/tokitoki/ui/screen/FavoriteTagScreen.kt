package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.FavoriteTagAction
import com.example.tokitoki.ui.model.TagTypeItem
import com.example.tokitoki.ui.model.TagItem
import com.example.tokitoki.ui.state.FavoriteTagEvent
import com.example.tokitoki.ui.state.FavoriteTagUiState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.FavoriteTagViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteTagScreen(
    viewModel: FavoriteTagViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { uiState.tagTypeList.size }

    LaunchedEffect(Unit) {
        viewModel.loadTagsByTagType()

        viewModel.uiEvent.collect {
            when (it) {
                is FavoriteTagEvent.ACTION -> {
                    when(it.action) {
                        FavoriteTagAction.BackBtnClicked -> {
                            onBackClick()
                        }
                        is FavoriteTagAction.TagTypeTabClicked -> {
                            pagerState.scrollToPage(it.action.index)
                        }
                        FavoriteTagAction.NOTHING -> {

                        }
                    }
                }
                FavoriteTagEvent.NOTHING -> {

                }
            }
        }
    }

    FavoriteTagContent(
        uiState = uiState,
        pagerState = pagerState,
        coroutineScope = coroutineScope,
        onBackBtnClicked = { viewModel.onBackBtnClicked() },
        onTagTypeTabClicked = { index -> viewModel.onTagTypeTabClicked(index) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteTagContent(
    uiState: FavoriteTagUiState,
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onBackBtnClicked: () -> Unit,
    onTagTypeTabClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onBackBtnClicked()
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                tint = LocalColor.current.black,
                contentDescription = ""
            )
            Text(
                text = "あなたの登録マイタグ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FavoriteTagPagerTab(
            modifier = Modifier.padding(top = 20.dp),
            tabs = uiState.tagTypeList,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            onTabSelected = { index ->
                onTagTypeTabClicked(index)
            }
        )

        FavoriteTagPager(
            modifier = Modifier.weight(1f),
            pagerState = pagerState,
            uiState = uiState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteTagPagerTab(
    modifier: Modifier = Modifier,
    tabs: List<TagTypeItem> = listOf(),
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onTabSelected: (Int) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val tabWidth: Dp = if (tabs.isNotEmpty()) maxWidth / tabs.size else 0.dp

        val indicatorOffset by animateDpAsState(
            targetValue = pagerState.currentPage * tabWidth,
            label = "",
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tagTypeItem ->
                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch {
                                    onTabSelected(index)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tagTypeItem.title,
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
fun FavoriteTagPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    uiState: FavoriteTagUiState,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        val currentTagTypeTitle: String = uiState.tagTypeList.getOrNull(page)?.title ?: ""
        val currentTagList: List<TagItem> = uiState.tagsByTagType[currentTagTypeTitle] ?: emptyList()

        val colorStops = arrayOf(
            0.1f to Color.Transparent,
            1f to Color.Black,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currentTagList) { tag ->

                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tag.url)
                        .error(R.drawable.no_image_icon) // 이미지 로드 실패 시 대체 이미지
                        .size(Size.ORIGINAL)
                        .diskCachePolicy(CachePolicy.ENABLED) // 디스크 캐시 활성화
                        .memoryCachePolicy(CachePolicy.ENABLED) // 메모리 캐시 활성화
                        .crossfade(true) // 부드러운 전환 애니메이션
                        .build()
                )

                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(30.dp))
                        .aspectRatio(1f)
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .background(Brush.verticalGradient(colorStops = colorStops))
                            .align(Alignment.BottomCenter)
                    )

                    Text(
                        text = tag.title,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 5.dp),
                        color = LocalColor.current.white,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFavoriteTagContent() {
    TokitokiTheme {
        val testTagTypeList = listOf(
            TagTypeItem(0, "趣味"),
            TagTypeItem(1, "ライフスタイル"),
            TagTypeItem(2, "価値観")
        )
        val tags = mutableMapOf(
            "趣味" to listOf(
                TagItem(
                    id = 1,
                    title = "ヨガ",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 1
                ),
                TagItem(
                    id = 2,
                    title = "Hobby Activity 2",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 1
                ),
                TagItem(
                    id = 3,
                    title = "Hobby Adventure",
                    url = "https://example.com/hobby3",
                    tagTypeId = 1
                ),
            ),
            "ライフスタイル" to listOf(
                TagItem(
                    id = 1,
                    title = "ヨガ",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 2,
                    showBadge = true
                ),
                TagItem(
                    id = 2,
                    title = "Hobby Activity 2",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 2,
                    showBadge = true
                ),
            ),
            "価値観" to listOf(
                TagItem(
                    id = 1,
                    title = "ヨガ",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 3
                ),
                TagItem(
                    id = 2,
                    title = "Hobby Activity 2",
                    url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
                    tagTypeId = 3
                ),
            )
        )

        val uiState = FavoriteTagUiState(
            tagTypeList = testTagTypeList,
            tagsByTagType = tags
        )

        FavoriteTagContent(
            uiState = uiState,
            pagerState = rememberPagerState { uiState.tagTypeList.size },
            coroutineScope = rememberCoroutineScope(),
            onBackBtnClicked = {},
            onTagTypeTabClicked = {}
        )
    }
}
