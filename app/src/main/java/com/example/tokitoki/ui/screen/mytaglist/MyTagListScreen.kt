package com.example.tokitoki.ui.screen.mytaglist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tokitoki.R
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.ui.state.MyTagListUiState
import com.example.tokitoki.ui.viewmodel.MyTagListViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTagListScreen(
    modifier: Modifier = Modifier,
    viewModel: MyTagListViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToTagDetail: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyTagListScreenContents(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onNavigateUp = onNavigateUp,
        onNavigateToTagDetail = onNavigateToTagDetail,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTagListScreenContents(
    uiState: MyTagListUiState,
    onTabSelected: (TagType) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToTagDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = uiState.selectedTab.ordinal) { TagType.values().size }

    // Pager의 페이지가 변경될 때 탭 선택 상태 업데이트
    LaunchedEffect(pagerState.currentPage) {
        onTabSelected(TagType.values()[pagerState.currentPage])
    }

    // 탭이 변경될 때 Pager의 페이지 업데이트
    LaunchedEffect(uiState.selectedTab) {
        pagerState.animateScrollToPage(uiState.selectedTab.ordinal)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "登録タグリスト",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        TabRow(
            selectedTabIndex = uiState.selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            TagType.values().forEachIndexed { index, tagType ->
                Tab(
                    selected = uiState.selectedTab == tagType,
                    onClick = { onTabSelected(tagType) },
                    text = { Text(tagType.toKoreanString()) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val tagType = TagType.values()[page]
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.error != null -> {
                        Text(
                            text = uiState.error ?: "不明なエラー",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    uiState.tagLists[tagType].isNullOrEmpty() -> {
                        Text(
                            text = "登録中のマイタグがありません",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.tagLists[tagType] ?: emptyList()) { tag ->
                                MyTagListItem(
                                    tag = tag,
                                    onClick = { onNavigateToTagDetail(tag.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyTagListItem(
    tag: MainHomeTag,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 섬네일 이미지 (1:1 비율)
        AsyncImage(
            model = tag.imageUrl,
            contentDescription = "Tag Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image_icon)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 태그명
        Text(
            text = tag.name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        // 구독자 정보
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Subscriber Icon",
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "${tag.subscriberCount}人",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun TagType.toKoreanString(): String = when (this) {
    TagType.HOBBY -> "趣味"
    TagType.LIFESTYLE -> "ライフスタイル"
    TagType.VALUE -> "価値観"
}

@Preview(showBackground = true)
@Composable
fun MyTagListItemPreview() {
    MyTagListItem(
        tag = MainHomeTag(
            id = "1",
            name = "映画鑑賞",
            description = "映画を見ることが好きです",
            imageUrl = "",
            subscriberCount = 100,
            categoryId = "cat1",
            tagType = TagType.HOBBY
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MyTagListScreenContentsPreview() {
    MyTagListScreenContents(
        uiState = MyTagListUiState(
            selectedTab = TagType.HOBBY,
            tagLists = mapOf(
                TagType.HOBBY to listOf(
                    MainHomeTag(
                        id = "1",
                        name = "映画鑑賞",
                        description = "映画を見ることが好きです",
                        imageUrl = "",
                        subscriberCount = 100,
                        categoryId = "cat1",
                        tagType = TagType.HOBBY
                    )
                )
            ),
            isLoading = false,
            error = null
        ),
        onTabSelected = {},
        onNavigateUp = {},
        onNavigateToTagDetail = {},
        modifier = Modifier
    )
} 