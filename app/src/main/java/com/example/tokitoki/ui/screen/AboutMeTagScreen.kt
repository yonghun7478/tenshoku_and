package com.example.tokitoki.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeTagAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.model.CategoryItem
import com.example.tokitoki.ui.model.TagItem
import com.example.tokitoki.ui.screen.components.dialog.TkAlertDialog
import com.example.tokitoki.ui.screen.components.etc.TkBottomArrowNavigation
import com.example.tokitoki.ui.screen.components.icons.TkRoundedIcon
import com.example.tokitoki.ui.state.AboutMeTagEvent
import com.example.tokitoki.ui.state.AboutMeTagState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeTagViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeTagScreen(
    tagIds: List<Int> = listOf(),
    onAboutMeSecondScreen: () -> Unit = {},
    onAboutMeThirdScreen: () -> Unit = {},
    viewModel: AboutMeTagViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState {
        uiState.categoryList.size
    }

    AboutMeTagContents(
        uiState = uiState,
        coroutineScope = coroutineScope,
        pagerState = pagerState,
        aboutMeTagAction = viewModel::aboutMeTagAction,
        isTest = viewModel.getIsTest()
    )

    LaunchedEffect(true) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeTagEvent.ACTION -> {
                    when (event.action) {
                        is AboutMeTagAction.SelectedTab -> {
                            pagerState.animateScrollToPage(event.action.index)
                        }

                        AboutMeTagAction.DIALOG_OK -> {
                            viewModel.updateShowDialogState(false)
                        }

                        AboutMeTagAction.NEXT -> {
                            if (viewModel.checkTags()) {
                                onAboutMeThirdScreen()
                            } else {
                                viewModel.updateShowDialogState(true)
                            }
                        }

                        AboutMeTagAction.NOTHING -> {}
                        AboutMeTagAction.PREVIOUS -> {
                            onAboutMeSecondScreen()
                        }

                        is AboutMeTagAction.ITEM_CLICKED -> {
                            viewModel.updateGridItem(event.action.category, event.action.index)
                        }
                    }
                }

                AboutMeTagEvent.NOTHING -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeTagContents(
    uiState: AboutMeTagState = AboutMeTagState(),
    pagerState: PagerState,
    aboutMeTagAction: (AboutMeTagAction) -> Unit = {},
    coroutineScope: CoroutineScope,
    isTest: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LocalColor.current.white)
            .testTag(TestTags.ABOUT_ME_TAG_CONTENTS)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TkRoundedIcon(
                modifier = Modifier.padding(top = 30.dp),
                iconRes = R.drawable.baseline_kitesurfing_24
            )
            AboutMeTagTitle(
                modifier = Modifier.padding(top = 10.dp)
            )

            AboutMeTagPagerTab(
                modifier = Modifier.padding(top = 30.dp),
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tabs = uiState.categoryList,
                aboutMeTagAction = aboutMeTagAction
            )
            AboutMeTagPager(
                modifier = Modifier.weight(1f),
                uiState = uiState,
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                aboutMeTagAction = aboutMeTagAction,
                isTest = isTest
            )
        }

        TkBottomArrowNavigation(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            action = aboutMeTagAction,
            previousActionParam = AboutMeTagAction.PREVIOUS,
            nextActionParam = AboutMeTagAction.NEXT
        )

        if (uiState.showDialog) {
            TkAlertDialog(
                message = stringResource(R.string.validate_about_me_tag),
                onDismissRequest = { aboutMeTagAction(AboutMeTagAction.DIALOG_OK) },
            )
        }
    }
}

@Composable
fun AboutMeTagTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.about_me_tag_title1),
            fontSize = 15.sp
        )
        Text(
            text = stringResource(R.string.about_me_tag_title2),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(R.string.about_me_tag_title3),
            fontSize = 10.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeTagPagerTab(
    modifier: Modifier = Modifier,
    tabs: List<CategoryItem> = listOf(),
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    aboutMeTagAction: (AboutMeTagAction) -> Unit = {},
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
                tabs.forEachIndexed { index, categoryItem ->
                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                coroutineScope.launch {
                                    aboutMeTagAction(AboutMeTagAction.SelectedTab(index))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = categoryItem.title,
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
fun AboutMeTagPager(
    modifier: Modifier = Modifier,
    uiState: AboutMeTagState = AboutMeTagState(),
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    aboutMeTagAction: (AboutMeTagAction) -> Unit = {},
    isTest: Boolean = false
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        // 현재 페이지의 카테고리 이름을 가져옴
        val currentCategoryTitle: String = uiState.categoryList.getOrNull(page)?.title ?: ""

        // 해당 카테고리의 관심사 리스트를 가져옴, 없으면 빈 리스트
        val currentTagList: List<TagItem> =
            uiState.tagsByCategory[currentCategoryTitle] ?: emptyList()


        // 각 카테고리별 페이지 표시
        AboutMeTagPage(
            categoryTitle = currentCategoryTitle,
            tagList = currentTagList,
            aboutMeTagAction = aboutMeTagAction,
            isTest = isTest
        )
    }
}

@Composable
fun AboutMeTagPage(
    categoryTitle: String,
    tagList: List<TagItem>,
    aboutMeTagAction: (AboutMeTagAction) -> Unit = {},
    isTest: Boolean = false
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3개의 열을 고정
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.ABOUT_ME_TAG_PAGE),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // 항목 간의 세로 간격
        horizontalArrangement = Arrangement.spacedBy(8.dp) // 항목 간의 가로 간격
    ) {
        itemsIndexed(tagList) { index, tag ->
            AboutMeTagGridItem(
                index = index,
                title = tag.title,
                categoryTitle = categoryTitle,
                url = tag.url,
                showBadge = tag.showBadge,
                aboutMeTagAction = aboutMeTagAction,
                isTest = isTest
            )
        }
    }
}

@Composable
fun AboutMeTagGridItem(
    modifier: Modifier = Modifier,
    categoryTitle: String = "",
    index: Int = 0,
    title: String = "",
    url: String = "",
    showBadge: Boolean = false,
    aboutMeTagAction: (AboutMeTagAction) -> Unit = {},
    isTest: Boolean = false
) {
    val colorStops = arrayOf(
        0.1f to Color.Transparent,
        1f to Color.Black,
    )

    // 이미지 로더를 테스트 환경에 맞게 설정
    val painter = if (isTest) {
        painterResource(R.drawable.couple_1) // 테스트용 로컬 이미지
    } else {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .error(R.drawable.no_image_icon) // 이미지 로드 실패 시 대체 이미지
                .diskCachePolicy(CachePolicy.ENABLED) // 디스크 캐시 활성화
                .memoryCachePolicy(CachePolicy.ENABLED) // 메모리 캐시 활성화
                .crossfade(true) // 부드러운 전환 애니메이션
                .build()
        )
    }

    // 이미지 로드 성공 여부를 확인 (테스트일 경우 성공 처리)
    val isImageLoaded = if (painter is AsyncImagePainter) {
        painter.state is AsyncImagePainter.State.Success  // 네트워크 이미지는 로딩 성공 여부 확인
    } else {
        true  // 로컬 이미지는 항상 성공으로 간주
    }

    val isImageError = if (painter is AsyncImagePainter) {
        painter.state is AsyncImagePainter.State.Error  // 네트워크 이미지는 로딩 실패 여부 확인
    } else {
        false  // 로컬 이미지는 실패하지 않음
    }

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .aspectRatio(1f)
            .then(
                if (showBadge && !isImageError) {
                    Modifier.border(
                        width = 2.dp,
                        color = LocalColor.current.blue,
                        shape = RoundedCornerShape(30.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                aboutMeTagAction(
                    AboutMeTagAction.ITEM_CLICKED(categoryTitle, index)
                )
            }
    ) {
        // 이미지 표시
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1f)
        )

        // 이미지가 성공적으로 로드되었을 때만 그라데이션 및 텍스트 표시
        if (isImageLoaded) {
            // 이미지 아래 그라데이션 처리
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Brush.verticalGradient(colorStops = colorStops))
                    .align(Alignment.BottomCenter)
            )

            // Badge 표시
            if (showBadge) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(maxWidth / 5)
                        .clip(CircleShape)
                        .background(LocalColor.current.blue)
                        .align(Alignment.TopEnd)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "",
                    )
                }
            }

            // 타이틀 텍스트 표시
            Text(
                text = title,
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


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTagContentsPreview() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 1
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2,
            showBadge = true
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2,
            showBadge = true
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags
    )

    val pagerState = rememberPagerState(
        initialPage = 1
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeTagItemPreview() {
    TokitokiTheme {
        AboutMeTagGridItem(
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            showBadge = true
        )
    }
}
