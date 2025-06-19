package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.TagCategoryUiState
import com.example.tokitoki.ui.state.TagResultUiState
import com.example.tokitoki.ui.viewmodel.TagSearchViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// --- Main Screen ---
@Composable
fun TagSearchScreen(
    viewModel: TagSearchViewModel = hiltViewModel(),
    onNavigateToCategory: (String, String) -> Unit = { _, _ -> },
    onNavigateUp: () -> Unit = {},
    onNavigateToTagDetail: (String) -> Unit = {}
) {
    val categories by viewModel.categories.collectAsState()
    val tags by viewModel.tags.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    // val selectedCategoryId by viewModel.selectedCategoryId.collectAsState() // This line seems unused

    TagSearchScreenContents(
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        categories = categories,
        searchResults = tags,
        isSearching = searchQuery.isNotBlank(),
        onCategoryClick = { category -> onNavigateToCategory(category.id, category.name) },
        onTagClick = { tag -> onNavigateToTagDetail(tag.id) },
        isLoading = isLoading,
        errorMessage = errorMessage,
        onNavigateUp = onNavigateUp
    )
}

// --- Contents ---
@Composable
fun TagSearchScreenContents(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    categories: List<TagCategoryUiState>,
    searchResults: List<TagResultUiState>,
    isSearching: Boolean,
    onCategoryClick: (TagCategoryUiState) -> Unit,
    onTagClick: (TagResultUiState) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp), // Adjusted padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp)) // Spacer between icon and search bar
            TagSearchBar(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f) // Search bar takes remaining space
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("로딩 중...", color = Color.Gray)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorMessage, color = Color.Red)
                }
            }
            !isSearching -> {
                TagCategoryGrid(
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                TagSearchResultGrid(
                    tags = searchResults,
                    onTagClick = onTagClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// --- Search Bar ---
@Composable
fun TagSearchBar( // onNavigateUp parameter removed
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF2F2F2),
        modifier = modifier // Removed .height(48.dp) to allow Row to control height if needed
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth() // Search bar itself will fill width within its weighted space
                .padding(horizontal = 16.dp, vertical = 12.dp) // Adjusted padding
        ) {
            // IconButton and Spacer for back arrow removed from here
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = "興味のあるマイタグを検索",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

// --- Category Grid ---
@Composable
fun TagCategoryGrid(
    categories: List<TagCategoryUiState>,
    onCategoryClick: (TagCategoryUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            TagCategoryItem(category = category, onClick = { onCategoryClick(category) })
        }
    }
}

@Composable
fun TagCategoryItem(
    category: TagCategoryUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(Color(0xFFF8F8F8))
            .padding(8.dp)
            .width(100.dp) // Consider making this adaptive or removing fixed width
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image_icon)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}

// --- Tag Result Grid ---
@Composable
fun TagSearchResultGrid(
    tags: List<TagResultUiState>,
    onTagClick: (TagResultUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tags) { tag ->
            TagResultItem(tag = tag, onClick = { onTagClick(tag) })
        }
    }
}

@Composable
fun TagResultItem(
    tag: TagResultUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(Color(0xFFF8F8F8))
            .padding(8.dp)
            .width(100.dp) // Consider making this adaptive or removing fixed width
    ) {
        AsyncImage(
            model = tag.imageUrl,
            contentDescription = tag.name,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.no_image_icon)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = tag.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${tag.subscriberCount}人",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

// --- Preview for TagSearchScreenContents ---
@Preview(showBackground = true, name = "Contents - Initial State")
@Composable
fun TagSearchScreenContentsPreview_Initial() {
    val sampleCategories = listOf(
        TagCategoryUiState(id = "1", name = "맛집", imageUrl = ""), // Added tagCount for consistency
        TagCategoryUiState(id = "2", name = "여행", imageUrl = ""),
        TagCategoryUiState(id = "3", name = "운동", imageUrl = ""),
        TagCategoryUiState(id = "4", name = "게임", imageUrl = ""),
        TagCategoryUiState(id = "5", name = "영화", imageUrl = ""),
        TagCategoryUiState(id = "6", name = "음악", imageUrl = "")
    )
    MaterialTheme {
        TagSearchScreenContents(
            searchQuery = "",
            onSearchQueryChange = {},
            categories = sampleCategories,
            searchResults = emptyList(),
            isSearching = false,
            onCategoryClick = {},
            onTagClick = {},
            isLoading = false,
            errorMessage = null,
            onNavigateUp = {}
        )
    }
}

@Preview(showBackground = true, name = "Contents - Searching State")
@Composable
fun TagSearchScreenContentsPreview_Searching() {
    val sampleSearchResults = listOf(
        TagResultUiState(id = "t1", name = "강남역 맛집", imageUrl = "", subscriberCount = 120),
        TagResultUiState(id = "t2", name = "홍대 맛집", imageUrl = "", subscriberCount = 250),
        TagResultUiState(id = "t3", name = "부산 맛집", imageUrl = "", subscriberCount = 180)
    )
    MaterialTheme {
        TagSearchScreenContents(
            searchQuery = "맛집",
            onSearchQueryChange = {},
            categories = emptyList(),
            searchResults = sampleSearchResults,
            isSearching = true,
            onCategoryClick = {},
            onTagClick = {},
            isLoading = false,
            errorMessage = null,
            onNavigateUp = {}
        )
    }
}

@Preview(showBackground = true, name = "Contents - Loading State")
@Composable
fun TagSearchScreenContentsPreview_Loading() {
    MaterialTheme {
        TagSearchScreenContents(
            searchQuery = "",
            onSearchQueryChange = {},
            categories = emptyList(),
            searchResults = emptyList(),
            isSearching = false,
            onCategoryClick = {},
            onTagClick = {},
            isLoading = true,
            errorMessage = null,
            onNavigateUp = {}
        )
    }
}

@Preview(showBackground = true, name = "Contents - Error State")
@Composable
fun TagSearchScreenContentsPreview_Error() {
    MaterialTheme {
        TagSearchScreenContents(
            searchQuery = "",
            onSearchQueryChange = {},
            categories = emptyList(),
            searchResults = emptyList(),
            isSearching = false,
            onCategoryClick = {},
            onTagClick = {},
            isLoading = false,
            errorMessage = "에러가 발생했습니다. 다시 시도해주세요.",
            onNavigateUp = {}
        )
    }
}
