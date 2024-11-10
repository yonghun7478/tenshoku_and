package com.example.tokitoki.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.constants.AboutMeProfInputAction
import com.example.tokitoki.ui.model.MySelfSentenceItem
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.screen.components.etc.TkWormIndicator
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeProfInputViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeProfInputScreen(
    onAboutMePhotoUploadScreen: () -> Unit = {},
    onAboutMeProfScreen: () -> Unit = {},
    viewModel: AboutMeProfInputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState {
        if (uiState.isInitialized) uiState.myselfSentenceList.size else 1
    }

    if (uiState.isInitialized) {
        AboutMeProfInputContents(
            pagerState = pagerState,
            itemList = uiState.myselfSentenceList
        )
    }

    LaunchedEffect(Unit) {
        viewModel.init()

        viewModel.uiEvent.collect { event ->

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AboutMeProfInputContents(
    modifier: Modifier = Modifier,
    itemList: List<MySelfSentenceItem> = listOf(),
    pagerState: PagerState = rememberPagerState { 1 },
    aboutMeProfInputAction: (AboutMeProfInputAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
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
                .weight(1f),
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
                Text(
                    text = curItem.type,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = curItem.sentence,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
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
            sentence = "文章１"
        ),
        MySelfSentenceItem(
            id = 2,
            type = "タイプ2",
            sentence = "文章2"
        ),
        MySelfSentenceItem(
            id = 3,
            type = "タイプ3",
            sentence = "文章3"
        ),
    )

    val pagerState: PagerState = rememberPagerState { 3 }

    TokitokiTheme {
        AboutMeProfInputContents(
            pagerState = pagerState,
            itemList = itemList
        )
    }
}