package com.example.tokitoki.ui.screen.mytaglist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.ui.state.MyTagListUiState
import com.example.tokitoki.ui.viewmodel.MyTagListViewModel

@Composable
fun MyTagListScreen(
    modifier: Modifier = Modifier,
    viewModel: MyTagListViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyTagListScreenContents(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onNavigateUp = onNavigateUp,
        modifier = modifier
    )
}

@Composable
fun MyTagListScreenContents(
    uiState: MyTagListUiState,
    onTabSelected: (TagType) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
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
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "알 수 없는 오류",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.tagLists[uiState.selectedTab].isNullOrEmpty() -> {
                    Text(
                        text = "등록중인 마이태그가 없습니다",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.tagLists[uiState.selectedTab]?.size ?: 0) { idx ->
                            val tag = uiState.tagLists[uiState.selectedTab]?.get(idx)
                            tag?.let {
                                Text(
                                    text = it.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun TagType.toKoreanString(): String = when (this) {
    TagType.HOBBY -> "취미"
    TagType.LIFESTYLE -> "라이프스타일"
    TagType.VALUE -> "가치관"
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
                        name = "영화감상",
                        description = "영화를 보는 것을 좋아해요",
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
        modifier = Modifier
    )
} 