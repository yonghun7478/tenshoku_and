package com.example.tokitoki.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.MainHomeMyTagUiState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.MainHomeMyTagViewModel

@Composable
fun MainHomeMyTagScreen(viewModel: MainHomeMyTagViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    MainHomeMyTagContents(
        uiState = uiState,
        onSearchTextChanged = viewModel::onSearchTextChanged
    )
}

@Composable
fun MainHomeMyTagContents(
    uiState: MainHomeMyTagUiState,
    onSearchTextChanged: (String) -> Unit
) {
    // ✅ SearchBar와 나머지 UI를 감싸는 Box
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding()
        ) {
            Spacer(modifier = Modifier.height(100.dp)) // 검색바 높이만큼 간격 추가
            SectionTitle("오늘의 태그 & 트렌딩 태그")
            TrendingTags(uiState.trendingTags)
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("내가 선택한 태그")
            SelectedTags(uiState.selectedTags)
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("새로운 태그 추천")
            RecommendedTags(uiState.recommendedTags)
        }

        // ✅ 상단에 고정된 SearchBar
        TopSearchBar()
    }
}

/**
 * 최상위 TopSearchBar 컴포저블.
 * - Normal 모드와 Expanded 모드를 상태값(isExpanded)을 통해 전환함.
 * - 추후 ViewModel과 연결하여 상태 관리를 할 수 있도록 구성함.
 */
@Composable
fun TopSearchBar() {
    // 모드 전환 상태 (Normal / Expanded)
    var isExpanded by remember { mutableStateOf(false) }
    // Normal 모드에서 표시할 '선택한 취미' 리스트 (확정된 선택값)
    var confirmedSelectedHobbies by remember { mutableStateOf(listOf<String>()) }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (isExpanded) {
            TopSearchBarExpanded(
                initialSelectedHobbies = confirmedSelectedHobbies,
                onSearchConfirmed = { newSelectedHobbies ->
                    confirmedSelectedHobbies = newSelectedHobbies
                    isExpanded = false
                },
                onCancel = {
                    isExpanded = false
                }
            )
        } else {
            TopSearchBarNormal(
                selectedHobbies = confirmedSelectedHobbies,
                onClick = { isExpanded = true }
            )
        }
    }
}

/**
 * Normal 모드의 TopSearchBar.
 */
@Composable
fun TopSearchBarNormal(
    selectedHobbies: List<String>,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatSelectedHobbies(selectedHobbies),
                style = TextStyle(fontSize = 16.sp, color = Color.Black)
            )
        }
    }
}

/**
 * 선택한 취미 리스트 포맷팅 함수.
 */
private fun formatSelectedHobbies(selectedHobbies: List<String>): String {
    if (selectedHobbies.isEmpty()) return "興味があるマイタグを検索"
    val maxCount = 3
    return if (selectedHobbies.size > maxCount) {
        selectedHobbies.take(maxCount).joinToString(", ") + ", +${selectedHobbies.size - maxCount}"
    } else {
        selectedHobbies.joinToString(", ")
    }
}

/**
 * Expanded 모드의 TopSearchBar.
 */
@Composable
fun TopSearchBarExpanded(
    initialSelectedHobbies: List<String>,
    onSearchConfirmed: (List<String>) -> Unit,
    onCancel: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    // 여기서는 리스트의 참조가 변경되어야 하므로 immutable List로 관리함.
    var localSelectedHobbies by remember { mutableStateOf(initialSelectedHobbies) }

    // 더미 데이터 (실제 데이터는 ViewModel에서 제공)
    val recentSearchedHobbies = listOf("농구", "독서", "요리")
    val trendingHobbies = listOf("캠핑", "사진", "댄스")
    val allHobbies = listOf("test", "독서", "요리", "캠핑", "사진", "댄스", "수영", "등산", "게임")
    val searchResults = if (inputText.isBlank()) emptyList() else {
        allHobbies.filter { it.contains(inputText, ignoreCase = true) }
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BackHandler {
        onCancel()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .animateContentSize()
            .padding(16.dp)
    ) {
        // 상단 영역: 검색 입력바
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("검색어를 입력하세요", style = TextStyle(fontSize = 16.sp, color = Color.LightGray)) },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchConfirmed(localSelectedHobbies) }
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 중단 영역: 스크롤 가능한 내용 영역
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // "선택한 취미" 섹션: 클릭 시 토글 방식으로 추가/제거
            TopSearchBarSection(
                title = "선택한 취미",
                items = localSelectedHobbies,
                onItemClick = { item ->
                    localSelectedHobbies = if (localSelectedHobbies.contains(item)) {
                        // 제거: 새로운 리스트 생성
                        localSelectedHobbies - item
                    } else {
                        // 추가: 새로운 리스트 생성
                        localSelectedHobbies + item
                    }
                }
            )

            if (inputText.isBlank()) {
                TopSearchBarSection(
                    title = "최근 검색한 취미",
                    items = recentSearchedHobbies,
                    onItemClick = { item ->
                        if (!localSelectedHobbies.contains(item)) {
                            localSelectedHobbies = localSelectedHobbies + item
                        }
                    }
                )
                TopSearchBarSection(
                    title = "급상승 취미",
                    items = trendingHobbies,
                    onItemClick = { item ->
                        if (!localSelectedHobbies.contains(item)) {
                            localSelectedHobbies = localSelectedHobbies + item
                        }
                    }
                )
            } else {
                TopSearchBarSection(
                    title = "검색 결과",
                    items = searchResults,
                    onItemClick = { item ->
                        if (!localSelectedHobbies.contains(item)) {
                            localSelectedHobbies = localSelectedHobbies + item
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 하단 영역: 검색 버튼과 돌아가기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onSearchConfirmed(localSelectedHobbies)
            }) {
                Text("검색", style = TextStyle(fontSize = 16.sp, color = Color.White))
            }
            Button(onClick = {
                onCancel()
            }) {
                Text("돌아가기", style = TextStyle(fontSize = 16.sp, color = Color.White))
            }
        }
    }
}

/**
 * TopSearchBar의 섹션 UI 컴포저블.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopSearchBarSection(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (items.isEmpty()) {
            Text(text = "없음", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
        } else {
            FlowRow {
                items.forEach { item ->
                    TopSearchBarChip(text = item, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

/**
 * TopSearchBar에서 개별 취미를 chip 형태로 표현하는 컴포저블.
 */
@Composable
fun TopSearchBarChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        color = Color(0xFF6200EE).copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = TextStyle(fontSize = 14.sp, color = Color.Black)
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun TagItem(tag: String, imageUrl: String) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Text(
            text = tag,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun TrendingTags(tags: List<Pair<String, String>>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        tags.forEach { (tag, image) ->
            TagItem(tag, image)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun SelectedTags(tags: List<Pair<String, String>>) {
    Column {
        tags.chunked(2).forEach { rowTags ->
            Row {
                rowTags.forEach { (tag, image) ->
                    TagItem(tag, image)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun RecommendedTags(tags: List<Pair<String, String>>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        tags.forEach { (tag, image) ->
            TagItem(tag, image)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainHomeMyTagContents() {
    TokitokiTheme {
        MainHomeMyTagContents(
            uiState = MainHomeMyTagUiState(
                trendingTags = listOf(
                    "운동" to "https://via.placeholder.com/150",
                    "독서" to "https://via.placeholder.com/150"
                ),
                selectedTags = listOf(
                    "게임" to "https://via.placeholder.com/150",
                    "요리" to "https://via.placeholder.com/150"
                ),
                recommendedTags = listOf(
                    "음악" to "https://via.placeholder.com/150",
                    "여행" to "https://via.placeholder.com/150"
                )
            ),
            onSearchTextChanged = {}
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainHomeContentsPreview() {
//    TokitokiTheme {
//        MainHomeContents(uiState = MainHomeUiState()) {
//        }
//    }
//}