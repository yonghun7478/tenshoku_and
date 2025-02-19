package com.example.tokitoki.ui.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.tokitoki.ui.state.TagItemUiState
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.MyTagViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTagScreen(viewModel: MyTagViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) } // 검색창 확장 상태

    Scaffold(
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {

            item {
                // 상단 태그 검색 바
                MyTagScreen_SearchBar(
                    selectedTags = uiState.selectedTags, // 변경
                    isExpanded = isExpanded,
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChanged = { query -> viewModel.onTagSearchQueryChanged(query) },
                    onTagRemoved = { tag -> viewModel.onTagRemoved(tag) }, // 변경
                    onSearchBarClicked = { isExpanded = true },
                    onSearchPerformed = {
                        viewModel.onSearchPerformed()
                        isExpanded = false
                    },
                    onBackButtonClicked = { isExpanded = false },
                    focusRequester = FocusRequester()
                )
                Divider()
            }
            item {
                // 검색바 확장되었을 때의 UI
                MyTagScreen_ExpandedSearchContent(
                    searchQuery = uiState.searchQuery,
                    recentSearches = uiState.recentSearches,
                    trendingTags = uiState.trendingTags,
                    searchResults = uiState.searchResults,
                    selectedTags = uiState.selectedTags,
                    onTagSelected = { tag -> viewModel.onTagSelected(tag) }, // 변경
                    onTagRemoved = { tag -> viewModel.onTagRemoved(tag) }, // 변경
                    isVisible = isExpanded
                )
                Divider()
            }

            item {
                // 오늘의 태그 & 트렌딩 태그
                MyTagScreen_TodayAndTrendingTags(listOf(uiState.todayTags) + uiState.trendingTags) // 여기서 합침
                Divider()
            }
            item {
                // 내가 선택한 태그
                MyTagScreen_MySelectedTags(uiState.myTags)
                Divider()
            }

            item {
                // 프로모션 배너 (임시)
                MyTagScreen_PromotionBanner(
                    imageUrl = "https://via.placeholder.com/350x150", // 임시 이미지
                    onClick = { /* TODO: Handle banner click */ }
                )
                Divider()
            }
            item {
                // 새로운 태그 추천
                MyTagScreen_SuggestedTags(uiState.suggestedTags)
            }


        }
    }
}

// 검색 바 (일반 상태)
@Composable
fun MyTagScreen_NormalSearchBar(
    selectedTags: List<TagItemUiState>, // 변경
    onSearchBarClicked: () -> Unit,
    onTagRemoved: (TagItemUiState) -> Unit, // 변경
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
            MyTagScreen_SelectedTagsRow( // 변경
                selectedTags = selectedTags,
                onTagRemoved = onTagRemoved
            )
        }
    }
}

// 검색 바 (확장 상태)
@Composable
fun MyTagScreen_ExpandedSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier, // 추가: 외부에서 Modifier를 받을 수 있도록
) {
    Row(
        modifier = modifier
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
                .fillMaxWidth()
                .focusRequester(focusRequester), // focusRequester 적용
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth() // 너비를 최대로 설정
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "興味があるマイタグを検索", // 플레이스홀더
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField() // 입력 필드
                }
            }
        )
    }
}

// 상단 검색 바 (확장/축소 애니메이션 포함)
@Composable
fun MyTagScreen_SearchBar(
    selectedTags: List<TagItemUiState>, // 변경
    isExpanded: Boolean, // 확장 상태 변수
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onTagRemoved: (TagItemUiState) -> Unit, //변경
    onSearchBarClicked: () -> Unit, // 검색 바 클릭 이벤트 핸들러
    onSearchPerformed: () -> Unit, // 검색 버튼 클릭 이벤트 핸들러
    onBackButtonClicked: () -> Unit,
    focusRequester: FocusRequester,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // 애니메이션 효과 적용
    ) {
        if (isExpanded) {
            MyTagScreen_ExpandedSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                focusRequester = focusRequester,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onSearchPerformed) {
                    Text("검색")
                }
                Button(onClick = onBackButtonClicked) {
                    Text("돌아가기")
                }
            }

        } else {
            MyTagScreen_NormalSearchBar(
                selectedTags = selectedTags,
                onSearchBarClicked = onSearchBarClicked,
                onTagRemoved = onTagRemoved,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// 선택된 태그들을 보여주는 가로 스크롤 리스트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyTagScreen_SelectedTagsRow(
    selectedTags: List<TagItemUiState>, // 변경
    onTagRemoved: (TagItemUiState) -> Unit, // 변경
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),

        ) {
        selectedTags.forEach { tag ->
            MyTagScreen_TagChip( // 변경
                tag = tag,
                onTagClick = { onTagRemoved(tag) }, //변경
                isRemovable = true
            )
        }
    }
}

// 확장된 검색 바의 내용 (최근 검색, 급상승 태그, 검색 결과)
@Composable
fun MyTagScreen_ExpandedSearchContent(
    searchQuery: String,
    recentSearches: List<TagItemUiState>,
    trendingTags: List<TagItemUiState>,
    searchResults: List<TagItemUiState>,
    selectedTags: List<TagItemUiState>,
    onTagSelected: (TagItemUiState) -> Unit,
    onTagRemoved: (TagItemUiState) -> Unit,
    isVisible: Boolean
) {
    AnimatedVisibility(isVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // 선택된 태그
            MyTagScreen_TagSection(
                title = "선택된 태그",
                tags = selectedTags,
                onTagClick = onTagRemoved,
                isRemovable = true
            )

            // 검색 결과
            MyTagScreen_TagSection(
                title = "검색 결과",
                tags = searchResults,
                onTagClick = onTagSelected
            )

            if (searchQuery.isBlank()) {
                // 최근 검색 태그
                MyTagScreen_TagSection(
                    title = "최근 검색 태그",
                    tags = recentSearches,
                    onTagClick = onTagSelected
                )

                // 급상승 태그
                MyTagScreen_TrendingTagSection(
                    title = "급상승 태그",
                    trendingTags = trendingTags,
                    onTagClick = onTagSelected
                )
            }
        }
    }
}

// 태그 섹션 (제목 + 칩 목록)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyTagScreen_TagSection(
    title: String,
    tags: List<TagItemUiState>, // List<String> -> List<TagItemUiState>
    onTagClick: (TagItemUiState) -> Unit, // (String) -> Unit 에서 변경
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
            // MyTagScreen_ChipRow(tags = tags, onTagClick = onTagClick, isRemovable = isRemovable)
            // 여기
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3
            ) {
                tags.forEach { tag ->
                    MyTagScreen_TagChip( // 변경
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

// 태그 섹션 (제목 + 칩 목록)
@Composable
fun MyTagScreen_TrendingTagSection( //이름 변경
    title: String,
    trendingTags: List<TagItemUiState>, // List<String> -> List<TagItemUiState>
    onTagClick: (TagItemUiState) -> Unit, // 변경된 부분
    isRemovable: Boolean = false // 삭제 가능한 칩인지 여부
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
        if (trendingTags.isEmpty()) {
            Text(
                text = "없음",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            //MyTagScreen_ChipRow(tags = trendingTags, onTagClick = onTagClick, isRemovable = isRemovable)
            //칩을 TagCard 형태로 변경
            trendingTags.forEach { tag ->
                MyTagScreen_TagCard(tag = tag, onClick = { onTagClick(tag) }) //여기

            }
        }
    }
}


@Composable
fun MyTagScreen_TagChip(
    tag: TagItemUiState, // String -> TagItemUiState
    onTagClick: () -> Unit, // 변경
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
                .clickable(onClick = onTagClick) //여기
        ) {
            Text(
                text = tag.name, // tag -> tag.name
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
                        .clickable(onClick = onTagClick), // 여기
                    contentAlignment = Alignment.Center // X 아이콘을 중앙에 배치
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
fun MyTagScreen_TodayAndTrendingTags(
    tags: List<TagItemUiState> // 단일 리스트로 변경
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
        MyTagScreen_TagCarousel(
            tags = tags // 단일 리스트 전달
        )
    }
}

// Carousel 구현 (LazyRow 사용)
@Composable
fun MyTagScreen_TagCarousel(
    tags: List<TagItemUiState> // 단일 리스트로 받음
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tags) { tag ->
            MyTagScreen_TagCard(tag = tag)
        }
    }
}

// 태그 카드 (섬네일, 태그 이름, 사용자 수)
@Composable
fun MyTagScreen_TagCard(
    tag: TagItemUiState,
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
fun MyTagScreen_MySelectedTags(
    myTags: List<TagItemUiState>
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
                    MyTagScreen_TagCard(tag = tag)
                }
            }
        }

    }
}

// 프로모션 배너
@Composable
fun MyTagScreen_PromotionBanner(
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
fun MyTagScreen_SuggestedTags(
    suggestedTags: List<TagItemUiState>
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
                MyTagScreen_TagCard(tag)
            }
        }
    }
}

@Preview(showBackground = true, name = "Normal Search Bar Preview")
@Composable
fun MyTagScreen_NormalSearchBarPreview() {
    TokitokiTheme { // Theme 적용
        MyTagScreen_NormalSearchBar(
            selectedTags = listOf(
                TagItemUiState("태그1", "image1", 10),
                TagItemUiState("태그2", "image2", 20),
                TagItemUiState("태그3", "image3", 30)
            ),
            onSearchBarClicked = {},
            onTagRemoved = {}
        )
    }
}

@Preview(showBackground = true, name = "Normal Search Bar Empty Preview")
@Composable
fun MyTagScreen_NormalSearchBarEmptyPreview() {
    TokitokiTheme { // Theme 적용
        MyTagScreen_NormalSearchBar(
            selectedTags = listOf(), // 빈 리스트
            onSearchBarClicked = {},
            onTagRemoved = {}
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Bar Preview")
@Composable
fun MyTagScreen_ExpandedSearchBarPreview() {
    TokitokiTheme { // Theme 적용
        MyTagScreen_ExpandedSearchBar(
            searchQuery = "",
            onSearchQueryChanged = {},
            focusRequester = FocusRequester()
        )
    }
}

@Preview(showBackground = true, name = "Expanded Search Bar with text Preview")
@Composable
fun MyTagScreen_ExpandedSearchBarTextPreview() {
    TokitokiTheme { // Theme 적용
        MyTagScreen_ExpandedSearchBar(
            searchQuery = "검색어 입력됨",
            onSearchQueryChanged = {},
            focusRequester = FocusRequester()
        )
    }
}


@Preview(showBackground = true, name = "SearchBar Expanded Preview")
@Composable
fun MyTagScreen_SearchBarExpandedPreview() {
    TokitokiTheme {
        MyTagScreen_SearchBar(
            selectedTags = listOf(
                TagItemUiState("태그1", "image1", 1),
                TagItemUiState("태그2", "image2", 2)
            ),
            isExpanded = true,
            searchQuery = "",
            onSearchQueryChanged = {},
            onTagRemoved = {},
            onSearchBarClicked = {},
            onSearchPerformed = {},
            onBackButtonClicked = {},
            focusRequester = FocusRequester()
        )
    }
}

@Preview(showBackground = true, name = "SearchBar Normal Preview")
@Composable
fun MyTagScreen_SearchBarNormalPreview() {
    TokitokiTheme {
        MyTagScreen_SearchBar(
            selectedTags = listOf(
                TagItemUiState("태그1", "image1", 1),
                TagItemUiState("태그2", "image2", 2)
            ),
            isExpanded = false,
            searchQuery = "",
            onSearchQueryChanged = {},
            onTagRemoved = {},
            onSearchBarClicked = {},
            onSearchPerformed = {},
            onBackButtonClicked = {},
            focusRequester = FocusRequester()
        )
    }
}



@Preview(showBackground = true, name = "Selected Tags Row Preview")
@Composable
fun MyTagScreen_SelectedTagsRowPreview() {
    TokitokiTheme{
        MyTagScreen_SelectedTagsRow(
            selectedTags = listOf(
                TagItemUiState("태그1", "image1", 10),
                TagItemUiState("태그2", "image2", 20),
                TagItemUiState("태그3", "image3", 30),
                TagItemUiState("태그4", "image4", 40),
                TagItemUiState("태그5", "image5", 50)
            ),
            onTagRemoved = {}
        )
    }
}


@Preview(showBackground = true, name = "Expanded Search Content Preview")
@Composable
fun MyTagScreen_ExpandedSearchContentPreview() {
    TokitokiTheme {
        MyTagScreen_ExpandedSearchContent(
            searchQuery = "",
            recentSearches =  listOf(
                TagItemUiState("최근검색1", "recent1", 1),
                TagItemUiState("최근검색2", "recent2", 2)
            ),
            trendingTags =  listOf(
                TagItemUiState("트렌딩 태그1", "image1", 50),
                TagItemUiState("트렌딩 태그2", "image2", 120),
                TagItemUiState("트렌딩 태그3", "image3", 80)
            ),
            searchResults =  listOf(
                TagItemUiState("검색결과1", "search_result_image1", 10),
                TagItemUiState("검색결과2", "search_result_image2", 20)
            ),
            selectedTags =  listOf(
                TagItemUiState("선택된태그1", "selected_image1", 100),
                TagItemUiState("선택된태그2", "selected_image2", 200)
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}
@Preview(showBackground = true, name = "Expanded Search Content SearchQuery Preview")
@Composable
fun MyTagScreen_ExpandedSearchContentSearchQueryPreview() {
    TokitokiTheme {
        MyTagScreen_ExpandedSearchContent(
            searchQuery = "검색어있음",
            recentSearches =  listOf(
                TagItemUiState("최근검색1", "recent1", 1),
                TagItemUiState("최근검색2", "recent2", 2)
            ),
            trendingTags =  listOf(
                TagItemUiState("트렌딩 태그1", "image1", 50),
                TagItemUiState("트렌딩 태그2", "image2", 120),
                TagItemUiState("트렌딩 태그3", "image3", 80)
            ),
            searchResults =  listOf(
                TagItemUiState("검색결과1", "search_result_image1", 10),
                TagItemUiState("검색결과2", "search_result_image2", 20)
            ),
            selectedTags =  listOf(
                TagItemUiState("선택된태그1", "selected_image1", 100),
                TagItemUiState("선택된태그2", "selected_image2", 200)
            ),
            onTagSelected = {},
            onTagRemoved = {},
            isVisible = true
        )
    }
}
@Preview(showBackground = true, name = "Tag Section Preview")
@Composable
fun MyTagScreen_TagSectionPreview() {
    TokitokiTheme{
        MyTagScreen_TagSection(
            title = "섹션 제목",
            tags = listOf(
                TagItemUiState("태그1", "image1", 10),
                TagItemUiState("태그2", "image2", 20),
                TagItemUiState("태그3", "image3", 30)
            ),
            onTagClick = {},
            isRemovable = false
        )
    }
}
@Preview(showBackground = true, name = "Tag Section Preview Removable")
@Composable
fun MyTagScreen_TagSectionPreviewRemovable() {
    TokitokiTheme{
        MyTagScreen_TagSection(
            title = "섹션 제목",
            tags = listOf(
                TagItemUiState("태그1", "image1", 10),
                TagItemUiState("태그2", "image2", 20),
                TagItemUiState("태그3", "image3", 30)
            ),
            onTagClick = {},
            isRemovable = true
        )
    }
}
@Preview(showBackground = true, name = "Trending Tag Section Preview")
@Composable
fun MyTagScreen_TrendingTagSectionPreview() {
    TokitokiTheme{
        MyTagScreen_TrendingTagSection(
            title = "급상승 태그",
            trendingTags = listOf(
                TagItemUiState("트렌딩 태그1", "image1", 50),
                TagItemUiState("트렌딩 태그2", "image2", 120),
                TagItemUiState("트렌딩 태그3", "image3", 80)
            ),
            onTagClick = {}
        )
    }
}
@Preview(showBackground = true, name = "Tag Chip Preview")
@Composable
fun MyTagScreen_TagChipPreview() {
    TokitokiTheme{
        MyTagScreen_TagChip(
            tag = TagItemUiState("태그", "image1", 10),
            onTagClick = {},
            isRemovable = true
        )
    }
}
@Preview(showBackground = true, name = "Tag Chip Preview2")
@Composable
fun MyTagScreen_TagChipPreview2() {
    TokitokiTheme{
        MyTagScreen_TagChip(
            tag = TagItemUiState("태그", "image1", 10),
            onTagClick = {},
            isRemovable = false
        )
    }
}