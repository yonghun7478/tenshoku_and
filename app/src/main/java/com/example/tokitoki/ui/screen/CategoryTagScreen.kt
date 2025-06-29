package com.example.tokitoki.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tokitoki.R
import com.example.tokitoki.ui.state.CategoryTagResultUiState
import com.example.tokitoki.ui.state.TagCategoryUiState
import com.example.tokitoki.ui.viewmodel.CategoryTagViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CategoryTagScreen(
    categoryId: String,
    categoryName: String,
    viewModel: CategoryTagViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToTagDetail: (String) -> Unit = {}
) {
    val tags by viewModel.tags.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(categoryId)
    }

    CategoryTagScreenContents(
        categoryName = categoryName,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        tags = tags,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onNavigateToTagDetail = onNavigateToTagDetail,
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun CategoryTagScreenContents(
    categoryName: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    tags: List<CategoryTagResultUiState>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onNavigateToTagDetail: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with back button and category name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = categoryName,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Search Bar
        CategoryTagSearchBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tags Grid
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
            else -> {
                TagSearchResultGrid(
                    tags = tags,
                    onTagClick = { tag -> onNavigateToTagDetail(tag.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CategoryTagSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF2F2F2),
        modifier = modifier
            .height(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
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
                            text = "タグを検索",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun TagSearchResultGrid(
    tags: List<CategoryTagResultUiState>,
    onTagClick: (CategoryTagResultUiState) -> Unit,
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
    tag: CategoryTagResultUiState,
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
            .width(100.dp)
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