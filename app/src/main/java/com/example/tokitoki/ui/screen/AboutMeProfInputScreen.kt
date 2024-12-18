package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.constants.AboutMeProfInputAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.model.MySelfSentenceItem
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.screen.components.etc.TkWormIndicator
import com.example.tokitoki.ui.state.AboutMeProfInputEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeProfInputViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeProfInputScreen(
    selfSentenceId: Int,
    onAboutMePhotoUploadScreen: () -> Unit = {},
    onAboutMeMyProfileScreen: () -> Unit = {},
    onPrevScreen: () -> Unit = {},
    viewModel: AboutMeProfInputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState {
        uiState.myselfSentenceList.size
    }

    AboutMeProfInputContents(
        isEditMode = uiState.isEditMode,
        pagerState = pagerState,
        itemList = uiState.myselfSentenceList,
        aboutMeProfInputAction = viewModel::aboutMeProfInputAction
    )

    LaunchedEffect(uiState.offset) {
        if (uiState.offset >= 0) {
            pagerState.scrollToPage(uiState.offset)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.init(selfSentenceId)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeProfInputEvent.ACTION -> {
                    when (event.action) {
                        AboutMeProfInputAction.SUBMIT -> {
                            viewModel.saveMySelfSentence(uiState.myselfSentenceList[pagerState.currentPage].id)
                            onAboutMeMyProfileScreen()
                        }

                        AboutMeProfInputAction.EDIT_OK -> {
                            viewModel.saveMySelfSentence(uiState.myselfSentenceList[pagerState.currentPage].id)
                            onPrevScreen()
                        }

                        else -> {}
                    }

                }

                AboutMeProfInputEvent.NOTHING -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeProfInputContents(
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    itemList: List<MySelfSentenceItem> = listOf(),
    pagerState: PagerState = rememberPagerState { 1 },
    aboutMeProfInputAction: (AboutMeProfInputAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(TestTags.ABOUT_ME_PROF_INPUT_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TkIndicator(
            modifier = Modifier.padding(top = 30.dp),
            total = 20,
            current = 20
        )

        AboutMeProfInputTitle(
            modifier = Modifier.padding(top = 10.dp)
        )

        AboutMeProfInputMySelf(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            pagerState = pagerState,
            itemList = itemList
        )

        if(isEditMode) {
            TkBtn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                text = "修正する",
                textColor = LocalColor.current.white,
                backgroundColor = LocalColor.current.blue,
                action = aboutMeProfInputAction,
                actionParam = AboutMeProfInputAction.EDIT_OK
            )
        } else {
            TkBtn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                text = "この自己紹介文で次へ",
                textColor = LocalColor.current.white,
                backgroundColor = LocalColor.current.blue,
                action = aboutMeProfInputAction,
                actionParam = AboutMeProfInputAction.SUBMIT
            )
        }
    }
}

@Composable
fun AboutMeProfInputTitle(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "自己紹介を\n選んでください",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeProfInputMySelf(
    modifier: Modifier = Modifier,
    itemList: List<MySelfSentenceItem> = listOf(),
    pagerState: PagerState = rememberPagerState { 1 }

) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .testTag(TestTags.ABOUT_ME_PROF_INPUT_MY_SELF_PAGER),
            state = pagerState
        ) { page ->

            val curItem = itemList[page]

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = LocalColor.current.white,
                ),
                modifier = modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = curItem.type,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color(curItem.typeColor.toLong(radix = 16)))
                            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                        textAlign = TextAlign.Center,
                        color = LocalColor.current.white
                    )

                    Text(
                        text = curItem.sentence,
                        modifier = Modifier
                            .padding(16.dp),
                    )
                }
            }
        }

        TkWormIndicator(
            count = 3,
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeProfileInputContentsPreview() {
    val itemList = listOf(
        MySelfSentenceItem(
            id = 1,
            type = "タイプ１",
            sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n"
        ),
        MySelfSentenceItem(
            id = 2,
            type = "タイプ2",
            sentence = "Curabitur pretium tincidunt lacus. Nulla gravida orci a odio. Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris. Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate vehicula. Donec lobortis risus a elit. Etiam tempor. Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id tincidunt sapien risus a quam. Maecenas fermentum consequat mi. Donec fermentum. Pellentesque malesuada nulla a mi. Duis sapien sem, aliquet nec, commodo eget, consequat quis, neque. Aliquam faucibus, elit ut dictum aliquet, felis nisl adipiscing sapien, sed malesuada diam lacus eget erat. Cras mollis scelerisque nunc. Nullam arcu. Aliquam consequat. \n"
        ),
        MySelfSentenceItem(
            id = 3,
            type = "タイプ3",
            sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst.\n"
        ),
    )

    val pagerState: PagerState = rememberPagerState { 3 }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(2)
    }

    TokitokiTheme {
        AboutMeProfInputContents(
            pagerState = pagerState,
            itemList = itemList
        )
    }
}