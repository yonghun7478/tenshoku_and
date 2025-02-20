package com.example.tokitoki.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.MainHomeTagItemUiState
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.MainHomeMyTagViewModel


@Composable
fun MainHomeMyTagScreen(viewModel: MainHomeMyTagViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) } // 검색창 확장 상태
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    // BackHandler 추가: 물리적 뒤로 가기 버튼 처리
    BackHandler(enabled = isExpanded) {
        viewModel.restoreSelectedTags()
        viewModel.clearSearchQuery()
        isExpanded = false // isExpanded를 false로 설정하여 검색창 닫기
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) { // Box 추가: 전체를 감싸는 역할

            // 원래의 컨텐츠 (LazyColumn)
            AnimatedVisibility(
                visible = !isExpanded, // isExpanded가 false일 때만 보임
                enter = fadeIn(),
                exit = fadeOut()
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    item {
                        // 상단 태그 검색 바 (isExpanded == false 일때만)
                        MainHomeMyTagScreen_NormalSearchBar(
                            selectedTags = uiState.selectedTags,
                            onSearchBarClicked = {
                                viewModel.saveSelectedTags()
                                viewModel.clearSearchQuery()
                                isExpanded = true
                            },
                        )
                        Divider()
                    }

                    item {
                        // 오늘의 태그 & 트렌딩 태그
                        MainHomeMyTagScreen_TodayAndTrendingTags(listOf(uiState.todayTags) + uiState.trendingTags)
                        Divider()
                    }
                    item {
                        // 내가 선택한 태그
                        MainHomeMyTagScreen_MySelectedTags(uiState.myTags)
                        Divider()
                    }

                    item {
                        // 프로모션 배너 (임시)
                        MainHomeMyTagScreen_PromotionBanner(
                            imageUrl = "https://via.placeholder.com/350x150", // 임시 이미지
                            onClick = { /* TODO: Handle banner click */ }
                        )
                        Divider()
                    }
                    item {
                        // 새로운 태그 추천
                        MainHomeMyTagScreen_SuggestedTags(uiState.suggestedTags)
                    }
                }
            }

            // 확장된 검색 바 화면 (전체 화면, isExpanded == true 일때만)
            AnimatedVisibility(
                visible = isExpanded, // isExpanded가 true일 때만 보임
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) { // 배경색 추가

                    MainHomeMyTagScreen_ExpandedSearchBar(
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChanged = { query -> viewModel.onTagSearchQueryChanged(query) },
                        focusRequester = focusRequester,
                        onSearchPerformed = {  // 추가
                            viewModel.onSearchPerformed()
                            viewModel.clearSearchQuery()
                            isExpanded = false
                        },
                        onBackButtonClicked = {
                            viewModel.restoreSelectedTags()
                            viewModel.clearSearchQuery()
                            isExpanded = false
                        } // 추가
                    )

                    MainHomeMyTagScreen_ExpandedSearchContent(
                        searchQuery = uiState.searchQuery,
                        recentSearches = uiState.recentSearches,
                        trendingTags = uiState.trendingTags,
                        searchResults = uiState.searchResults,
                        selectedTags = uiState.selectedTags,
                        onTagSelected = { tag -> viewModel.onTagSelected(tag) },
                        onTagRemoved = { tag -> viewModel.onTagRemoved(tag) },
                        isVisible = true // AnimatedVisibility 안에 있으므로 항상 true
                    )
                }
            }
        }
    }
}

// 검색 바 (일반 상태)
@Composable
fun MainHomeMyTagScreen_NormalSearchBar(
    selectedTags: List<MainHomeTagItemUiState>, // 변경
    onSearchBarClicked: () -> Unit,
    modifier: Modifier = Modifier, // 추가: 외부에서 Modifier를 받을 수 있도록
) {
    Row(
        modifier = modifier // 외부에서 Modifier 적용
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onSearchBarClicked() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        if (selectedTags.isEmpty()) {
            Text(
                text = "興味があるマイタグを検索",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            MainHomeMyTagScreen_SelectedTagsRow(
                // 변경
                selectedTags = selectedTags,
            )
        }
    }
}

// 검색 바 (확장 상태)
// 검색 바 (확장 상태)
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    onSearchPerformed: () -> Unit, // 추가
    onBackButtonClicked: () -> Unit, // 추가
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) { // Column 추가
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                cursorBrush = SolidColor(Color.Black),
                singleLine = true,
                modifier = Modifier
                    .weight(1f) // 남은 공간 모두 차지
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "興味があるマイタグを検索",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Row( // 버튼들을 위한 Row
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End // 오른쪽 정렬
        ) {
            Button(onClick = onSearchPerformed) {
                Text("검색")
            }
            Spacer(modifier = Modifier.width(8.dp)) // 버튼 사이 간격
            Button(onClick = onBackButtonClicked) {
                Text("돌아가기")
            }
        }
    }
}

// 선택된 태그들을 보여주는 가로 스크롤 리스트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainHomeMyTagScreen_SelectedTagsRow(
    selectedTags: List<MainHomeTagItemUiState>, // 변경
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),

        ) {
        selectedTags.forEach { tag ->
            MainHomeMyTagScreen_TagChip( // 변경
                tag = tag,
                isRemovable = false
            )
        }
    }
}

// 확장된 검색 바의 내용 (최근 검색, 급상승 태그, 검색 결과)
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContent(
    searchQuery: String,
    recentSearches: List<MainHomeTagItemUiState>,
    trendingTags: List<MainHomeTagItemUiState>,
    searchResults: List<MainHomeTagItemUiState>,
    selectedTags: List<MainHomeTagItemUiState>,
    onTagSelected: (MainHomeTagItemUiState) -> Unit,
    onTagRemoved: (MainHomeTagItemUiState) -> Unit,
    isVisible: Boolean
) {
    AnimatedVisibility(isVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // 선택된 태그
            MainHomeMyTagScreen_TagSection(
                title = "선택된 태그",
                tags = selectedTags,
                onTagClick = onTagRemoved,
                isRemovable = true
            )

            // 검색 결과
            MainHomeMyTagScreen_TagSection(
                title = "검색 결과",
                tags = searchResults,
                onTagClick = onTagSelected
            )

            if (searchQuery.isBlank()) {
                // 최근 검색 태그
                MainHomeMyTagScreen_TagSection(
                    title = "최근 검색 태그",
                    tags = recentSearches,
                    onTagClick = onTagSelected
                )

                // 최근 검색 태그
                MainHomeMyTagScreen_TagSection(
                    title = "급상승 태그",
                    tags = trendingTags,
                    onTagClick = onTagSelected
                )
            }
        }
    }
}

// 태그 섹션 (제목 + 칩 목록)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainHomeMyTagScreen_TagSection(
    title: String,
    tags: List<MainHomeTagItemUiState>, // List<String> -> List<TagItemUiState>
    onTagClick: (MainHomeTagItemUiState) -> Unit, // (String) -> Unit 에서 변경
    isRemovable: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (tags.isEmpty()) {
            Text(
                text = "없음",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            // MainHomeMyTagScreen_ChipRow(tags = tags, onTagClick = onTagClick, isRemovable = isRemovable)
            // 여기
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3
            ) {
                tags.forEach { tag ->
                    MainHomeMyTagScreen_TagChip( // 변경
                        tag = tag,
                        onTagClick = {
                            onTagClick(tag) // 클릭 시 전체 객체 전달

                        },
                        isRemovable = isRemovable
                    )
                }
            }
        }
    }
}

@Composable
fun MainHomeMyTagScreen_TagChip(
    tag: MainHomeTagItemUiState,
    onTagClick: (() -> Unit)? = null, // Optional<() -> Unit>으로 변경,  null 허용, 기본값 null
    isRemovable: Boolean = false
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isRemovable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 4.dp)
                .then(if (onTagClick != null) Modifier.clickable(onClick = onTagClick) else Modifier) // 조건부 clickable
        ) {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(
                    start = 8.dp,
                    end = if (isRemovable) 0.dp else 8.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
            )
            if (isRemovable) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .then(if (onTagClick != null) Modifier.clickable(onClick = onTagClick) else Modifier), // 조건부 clickable
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "X",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 오늘의 태그 & 트렌딩 태그 (Carousel)
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTags(
    tags: List<MainHomeTagItemUiState> // 단일 리스트로 변경
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "오늘의 태그 & 트렌딩 태그",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MainHomeMyTagScreen_TagCarousel(
            tags = tags // 단일 리스트 전달
        )
    }
}

// Carousel 구현 (LazyRow 사용)
@Composable
fun MainHomeMyTagScreen_TagCarousel(
    tags: List<MainHomeTagItemUiState> // 단일 리스트로 받음
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tags) { tag ->
            MainHomeMyTagScreen_TagCard(tag = tag)
        }
    }
}

// 태그 카드 (섬네일, 태그 이름, 사용자 수)
@Composable
fun MainHomeMyTagScreen_TagCard(
    tag: MainHomeTagItemUiState,
    onClick: () -> Unit = {} // 기본값 추가
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant, // 배경색
        modifier = Modifier
            .width(150.dp) // 너비 고정
            .height(100.dp) // 높이 고정
            .clickable(onClick = onClick) //클릭 리스너
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // 이미지
            Image(
                painter = painterResource(R.drawable.couple_3), // 임시 이미지
                contentDescription = "Tag Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp)), // 둥근 모서리
                contentScale = ContentScale.Crop // 이미지를 꽉 채우도록
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 텍스트 (태그 이름, 사용자 수)
            Column {
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${tag.userCount} 명",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }

}

// 내가 선택한 태그
@Composable
fun MainHomeMyTagScreen_MySelectedTags(
    myTags: List<MainHomeTagItemUiState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내가 선택한 태그",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "더보기",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (myTags.isEmpty()) {
            Text(
                text = "프로필에서 태그를 선택해주세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        } else {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(3), // 3행 그리드
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                items(myTags) { tag ->
                    MainHomeMyTagScreen_TagCard(tag = tag)
                }
            }
        }

    }
}

// 프로모션 배너
@Composable
fun MainHomeMyTagScreen_PromotionBanner(
    imageUrl: String,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.couple_3), // 임시 이미지
        contentDescription = "Promotion Banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentScale = ContentScale.Crop
    )
}

// 새로운 태그 추천
@Composable
fun MainHomeMyTagScreen_SuggestedTags(
    suggestedTags: List<MainHomeTagItemUiState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "새로운 태그 추천",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .heightIn(max = 200.dp) // 최대 높이 제한
        ) {
            items(suggestedTags) { tag ->
                MainHomeMyTagScreen_TagCard(tag)
            }
        }
    }
}

// 1. MainHomeMyTagScreen_NormalSearchBar Preview
@Preview(showBackground = true, name = "Normal Search Bar (Empty)")
@Composable
fun MainHomeMyTagScreen_NormalSearchBarEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_NormalSearchBar(
            selectedTags = listOf(), // 빈 리스트
            onSearchBarClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Normal Search Bar (With Tags)")
@Composable
fun MainHomeMyTagScreen_NormalSearchBarWithTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_NormalSearchBar(
            selectedTags = listOf(
                MainHomeTagItemUiState("태그1", "image1", 10),
                MainHomeTagItemUiState("태그2", "image2", 25)
            ),
            onSearchBarClicked = {}
        )
    }
}

// 2. MainHomeMyTagScreen_ExpandedSearchBar Preview
@Preview(showBackground = true, name = "Expanded Search Bar (Empty)")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBarEmptyPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchBar(
            searchQuery = "", // 빈 검색어
            onSearchQueryChanged = {},
            focusRequester = FocusRequester(),
            onSearchPerformed = {},
            onBackButtonClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Bar (With Text)")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchBarWithTextPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchBar(
            searchQuery = "검색 중...", // 검색어 입력
            onSearchQueryChanged = {},
            focusRequester = FocusRequester(),
            onSearchPerformed = {},
            onBackButtonClicked = {}
        )
    }
}

// 4. MainHomeMyTagScreen_SelectedTagsRow Preview
@Preview(showBackground = true, name = "Selected Tags Row")
@Composable
fun MainHomeMyTagScreen_SelectedTagsRowPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SelectedTagsRow(
            selectedTags = listOf(
                MainHomeTagItemUiState("태그1", "image1", 10),
                MainHomeTagItemUiState("태그2", "image2", 25),
                MainHomeTagItemUiState("태그3", "image3", 5)
            ),
        )
    }
}

// 5. MainHomeMyTagScreen_ExpandedSearchContent Preview:  검색어 없는 경우 (최근, 급상승) + 있는 경우.
@Preview(showBackground = true, name = "Expanded Search Content Preview")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContentPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchContent(
            searchQuery = "", // 빈 검색어
            recentSearches = listOf(
                MainHomeTagItemUiState("최근검색1", "recent1", 1),
                MainHomeTagItemUiState("최근검색2", "recent2", 2)
            ),
            trendingTags = listOf(
                MainHomeTagItemUiState("트렌딩 태그1", "image1", 50),
                MainHomeTagItemUiState("트렌딩 태그2", "image2", 120),
                MainHomeTagItemUiState("트렌딩 태그3", "image3", 80)
            ),
            searchResults = listOf(
                MainHomeTagItemUiState("검색결과1", "search_result_image1", 10),
                MainHomeTagItemUiState("검색결과2", "search_result_image2", 20)
            ),
            selectedTags = listOf(
                MainHomeTagItemUiState("선택된태그1", "selected_image1", 100),
                MainHomeTagItemUiState("선택된태그2", "selected_image2", 200)
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Content with Query")
@Composable
fun MainHomeMyTagScreen_ExpandedSearchContentSearchQueryPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_ExpandedSearchContent(
            searchQuery = "검색어있음", // 검색어 있음
            recentSearches = listOf(
                MainHomeTagItemUiState("최근검색1", "recent1", 1),
                MainHomeTagItemUiState("최근검색2", "recent2", 2)
            ),
            trendingTags = listOf(
                MainHomeTagItemUiState("트렌딩 태그1", "image1", 50),
                MainHomeTagItemUiState("트렌딩 태그2", "image2", 120),
                MainHomeTagItemUiState("트렌딩 태그3", "image3", 80)
            ),
            searchResults = listOf(
                MainHomeTagItemUiState("검색결과1", "search_result_image1", 10),
                MainHomeTagItemUiState("검색결과2", "search_result_image2", 20)
            ),
            selectedTags = listOf(
                MainHomeTagItemUiState("선택된태그1", "selected_image1", 100),
                MainHomeTagItemUiState("선택된태그2", "selected_image2", 200)
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}

// 6. MainHomeMyTagScreen_TagSection:  isRemovable = false 만. (true는 Chip에서 확인)
@Preview(showBackground = true, name = "Tag Section Preview")
@Composable
fun MainHomeMyTagScreen_TagSectionPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagSection(
            title = "섹션 제목",
            tags = listOf(
                MainHomeTagItemUiState("태그1", "image1", 10),
                MainHomeTagItemUiState("태그2", "image2", 20),
                MainHomeTagItemUiState("태그3", "image3", 30)
            ),
            onTagClick = {},
            isRemovable = false
        )
    }
}

// 8. MainHomeMyTagScreen_TagChip: isRemovable = true, false  2가지
@Preview(showBackground = true, name = "Tag Chip Removable")
@Composable
fun MainHomeMyTagScreen_TagChipRemovablePreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagChip(
            tag = MainHomeTagItemUiState("태그", "image1", 10),
            onTagClick = {},
            isRemovable = true
        )
    }
}

@Preview(showBackground = true, name = "Tag Chip Not Removable")
@Composable
fun MainHomeMyTagScreen_TagChipNotRemovablePreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagChip(
            tag = MainHomeTagItemUiState("태그", "image1", 10),
            onTagClick = {},
            isRemovable = false
        )
    }
}

@Preview(showBackground = true, name = "Carousel Preview")
@Composable
fun MainHomeMyTagScreen_TodayAndTrendingTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TodayAndTrendingTags(
            // 더 이상 todayTag, trendingTags를 분리하지 않음.
            tags = listOf(
                MainHomeTagItemUiState("오늘의 태그", "", 100),
                MainHomeTagItemUiState("트렌딩 태그1", "image1", 50),
                MainHomeTagItemUiState("트렌딩 태그2", "image2", 120),
                MainHomeTagItemUiState("트렌딩 태그3", "image3", 80)
            )

        )
    }
}

@Preview(showBackground = true, name = "Tag Card Preview")
@Composable
fun MainHomeMyTagScreen_TagCardPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_TagCard(
            tag = MainHomeTagItemUiState("태그 이름", "image_url", 123),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "My Selected Tags Preview")
@Composable
fun MainHomeMyTagScreen_MySelectedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_MySelectedTags(
            myTags = listOf(
                MainHomeTagItemUiState("선택한 태그1", "", 30),
                MainHomeTagItemUiState("선택한 태그2", "", 45),
                MainHomeTagItemUiState("선택한 태그3", "", 60),
                MainHomeTagItemUiState("선택한 태그4", "", 22)
            )
        )
    }
}

@Preview(showBackground = true, name = "Promotion Banner Preview")
@Composable
fun MainHomeMyTagScreen_PromotionBannerPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_PromotionBanner(
            imageUrl = "https://via.placeholder.com/350x150",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Suggested Tags Preview")
@Composable
fun MainHomeMyTagScreen_SuggestedTagsPreview() {
    TokitokiTheme {
        MainHomeMyTagScreen_SuggestedTags(
            suggestedTags = listOf(
                MainHomeTagItemUiState("추천 태그1", "image1", 15),
                MainHomeTagItemUiState("추천 태그2", "image2", 33)
            )
        )
    }
}
