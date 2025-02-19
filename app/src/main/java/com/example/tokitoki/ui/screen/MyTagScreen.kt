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

// 최상위 Composable (Scaffold 사용)
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
                    selectedTags = uiState.selectedTags,
                    isExpanded = isExpanded,
                    searchQuery = uiState.searchQuery, // ViewModel의 StateFlow에서 가져옴
                    onSearchQueryChanged = { query -> viewModel.onTagSearchQueryChanged(query) },
                    onTagRemoved = { tag -> viewModel.onTagRemoved(tag) },
                    onSearchBarClicked = { isExpanded = true },
                    onSearchPerformed = {
                        viewModel.onSearchPerformed()
                        isExpanded = false // 검색 후 확장 상태 false
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
                    onTagSelected = { tag -> viewModel.onTagSelected(tag) },
                    onTagRemoved = { tag -> viewModel.onTagRemoved(tag) },
                    isVisible = isExpanded
                )
                Divider()
            }

            item {
                // 오늘의 태그 & 트렌딩 태그
                MyTagScreen_TodayAndTrendingTags(uiState.todayTags, uiState.trendingTags)
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
    selectedTags: List<String>,
    onSearchBarClicked: () -> Unit,
    onTagRemoved: (String) -> Unit,
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
            MyTagScreen_SelectedTagsRow(
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
    selectedTags: List<String>,
    isExpanded: Boolean, // 확장 상태 변수
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
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
    selectedTags: List<String>,
    onTagRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),

        ) {
        selectedTags.forEach { tag ->
            MyTagScreen_TagChip(
                tag = tag,
                onTagRemoved = onTagRemoved,
                isRemovable = true
            )
        }
    }
}


// 확장된 검색 바의 내용 (최근 검색, 급상승 태그, 검색 결과)
@Composable
fun MyTagScreen_ExpandedSearchContent(
    searchQuery: String,
    recentSearches: List<String>,
    trendingTags: List<TagItemUiState>,
    searchResults: List<String>,
    selectedTags: List<String>,
    onTagSelected: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
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
                onTagClick = onTagRemoved, // 이미 선택된 태그는 제거
                isRemovable = true
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
            } else {
                // 검색 결과
                MyTagScreen_TagSection(
                    title = "검색 결과",
                    tags = searchResults,
                    onTagClick = onTagSelected // 검색 결과 태그 클릭 시 추가
                )
            }
        }
    }
}

// 태그 섹션 (제목 + 칩 목록)
@Composable
fun MyTagScreen_TagSection(
    title: String,
    tags: List<String>,
    onTagClick: (String) -> Unit,
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
        if (tags.isEmpty()) {
            Text(
                text = "없음",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            MyTagScreen_ChipRow(tags = tags, onTagClick = onTagClick, isRemovable = isRemovable)
        }
    }
}

// 태그 섹션 (제목 + 칩 목록)
@Composable
fun MyTagScreen_TrendingTagSection( //이름 변경
    title: String,
    trendingTags: List<TagItemUiState>, // List<String> -> List<TagItemUiState>
    onTagClick: (String) -> Unit,
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
                MyTagScreen_TagCard(tag = tag)

            }
        }
    }
}

// 칩(Chip) 형태의 태그 (클릭 가능)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyTagScreen_ChipRow(
    tags: List<String>,
    onTagClick: (String) -> Unit,
    isRemovable: Boolean = false // 삭제 가능한 칩인지 (X 표시 여부)
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = 3
    ) {
        tags.forEach { tag ->
            MyTagScreen_TagChip(
                tag = tag,
                onTagRemoved = { onTagClick(tag) }, // onTagClick 재사용
                isRemovable = isRemovable
            )
        }
    }
}

@Composable
fun MyTagScreen_TagChip(
    tag: String,
    onTagRemoved: (String) -> Unit,
    isRemovable: Boolean = false
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isRemovable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Text(
                text = tag,
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
                        .clickable {
                            onTagRemoved(tag)
                        },
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
    todayTag: TagItemUiState,
    trendingTags: List<TagItemUiState>
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = "오늘의 태그 & 트렌딩 태그",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyTagScreen_TagCarousel(
            todayTag = todayTag,
            trendingTags = trendingTags
        )
    }
}

// Carousel 구현 (LazyRow 사용)
@Composable
fun MyTagScreen_TagCarousel(
    todayTag: TagItemUiState,
    trendingTags: List<TagItemUiState>
) {
    val items = listOf(todayTag) + trendingTags
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(items) { tag ->
            MyTagScreen_TagCard(
                tag = tag
            )
        }
    }
}

// 태그 카드 (섬네일, 태그 이름, 사용자 수)
@Composable
fun MyTagScreen_TagCard(
    tag: TagItemUiState
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant, // 배경색
        modifier = Modifier
            .width(150.dp) // 너비 고정
            .height(100.dp) // 높이 고정
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

// Preview

@Preview(showBackground = true, name = "Normal Search Bar Preview")
@Composable
fun MyTagScreen_NormalSearchBarPreview() {
    MyTagScreen_NormalSearchBar(
        selectedTags = listOf("태그1", "태그2", "태그3"),
        onSearchBarClicked = {},
        onTagRemoved = {}
    )
}


@Preview(showBackground = true, name = "Expanded Search Bar Preview")
@Composable
fun MyTagScreen_ExpandedSearchBarPreview() {
    MyTagScreen_ExpandedSearchBar(
        searchQuery = "",
        onSearchQueryChanged = {},
        focusRequester = FocusRequester()
    )
}

@Preview(showBackground = true, name = "Selected Tags Row Preview")
@Composable
fun MyTagScreen_SelectedTagsRowPreview() {
    MyTagScreen_SelectedTagsRow(
        selectedTags = listOf("태그1", "태그2", "태그3", "태그4", "태그5"),
        onTagRemoved = {}
    )
}

@Preview(showBackground = true, name = "Expanded Search Content Preview")
@Composable
fun MyTagScreen_ExpandedSearchContentPreview() {
    MyTagScreen_ExpandedSearchContent(
        searchQuery = "검색어",
        recentSearches = listOf("최근검색1", "최근검색2"),
        trendingTags = listOf(
            TagItemUiState("트렌딩 태그1", "image1", 50),
            TagItemUiState("트렌딩 태그2", "image2", 120),
            TagItemUiState("트렌딩 태그3", "image3", 80)
        ),//여기
        searchResults = listOf("검색결과1", "검색결과2"),
        selectedTags = listOf("선택된태그1", "선택된태그2"),
        onTagSelected = {},
        onTagRemoved = {},
        isVisible = true
    )
}

@Preview(showBackground = true, name = "Tag Section Preview")
@Composable
fun MyTagScreen_TagSectionPreview() {
    MyTagScreen_TagSection(
        title = "섹션 제목",
        tags = listOf("태그1", "태그2", "태그3"),
        onTagClick = {}
    )
}

@Preview(showBackground = true, name = "Trending Tag Section Preview")
@Composable
fun MyTagScreen_TrendingTagSectionPreview() {
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


@Preview(showBackground = true, name = "Tag Chip Preview")
@Composable
fun MyTagScreen_TagChipPreview() {
    MyTagScreen_TagChip(
        tag = "태그",
        onTagRemoved = {},
        isRemovable = true
    )
}

@Preview(showBackground = true, name = "Carousel Preview")
@Composable
fun MyTagScreen_TodayAndTrendingTagsPreview() {
    MyTagScreen_TodayAndTrendingTags(
        todayTag = TagItemUiState("오늘의 태그", "", 100),
        trendingTags = listOf(
            TagItemUiState("트렌딩 태그1", "image1", 50),
            TagItemUiState("트렌딩 태그2", "image2", 120),
            TagItemUiState("트렌딩 태그3", "image3", 80)
        )
    )
}

@Preview(showBackground = true, name = "Tag Card Preview")
@Composable
fun MyTagScreen_TagCardPreview() {
    MyTagScreen_TagCard(
        tag = TagItemUiState("태그 이름", "image_url", 123)
    )
}

@Preview(showBackground = true, name = "My Selected Tags Preview")
@Composable
fun MyTagScreen_MySelectedTagsPreview() {
    MyTagScreen_MySelectedTags(
        myTags = listOf(
            TagItemUiState("선택한 태그1", "", 30),
            TagItemUiState("선택한 태그2", "", 45),
            TagItemUiState("선택한 태그3", "", 60),
            TagItemUiState("선택한 태그4", "", 22)
        )
    )
}

@Preview(showBackground = true, name = "Promotion Banner Preview")
@Composable
fun MyTagScreen_PromotionBannerPreview() {
    MyTagScreen_PromotionBanner(
        imageUrl = "https://via.placeholder.com/350x150",
        onClick = {}
    )
}

@Preview(showBackground = true, name = "Suggested Tags Preview")
@Composable
fun MyTagScreen_SuggestedTagsPreview() {
    MyTagScreen_SuggestedTags(
        suggestedTags = listOf(
            TagItemUiState("추천 태그1", "image1", 15),
            TagItemUiState("추천 태그2", "image2", 33)
        )
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "MyTagScreen Dark Mode"
)
@Composable
fun MyTagScreenPreviewDarkMode() {
    TokitokiTheme { // 머티리얼 테마 적용
        MyTagScreen(MyTagViewModel())
    }
}

@Preview(showBackground = true, name = "MyTagScreen Light Mode")
@Composable
fun MyTagScreenPreviewLightMode() {
    TokitokiTheme { // 머티리얼 테마 적용
        MyTagScreen(MyTagViewModel())
    }
}