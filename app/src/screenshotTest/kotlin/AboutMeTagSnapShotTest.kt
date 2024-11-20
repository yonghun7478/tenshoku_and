import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.model.CategoryItem
import com.example.tokitoki.ui.model.TagItem
import com.example.tokitoki.ui.screen.AboutMeTagContents
import com.example.tokitoki.ui.state.AboutMeTagState
import com.example.tokitoki.ui.theme.TokitokiTheme

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTag1SnapShotTest() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 1
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags
    )

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTag2SnapShotTest() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 1
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags
    )

    val pagerState = rememberPagerState(
        initialPage = 1
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTag3SnapShotTest() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 1
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags
    )

    val pagerState = rememberPagerState(
        initialPage = 2
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTagCheckedItemSnapShotTest() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
            showBadge = true
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
            showBadge = true
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
            showBadge = true
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2,
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3,
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags
    )

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeTagIsDisplayedDialogSnapShotTest() {
    val coroutineScope = rememberCoroutineScope()

    val testCategotyList = listOf(
        CategoryItem(0, "趣味"),
        CategoryItem(1, "ライフスタイル"),
        CategoryItem(2, "価値観")
    )

    val hobbyItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 1,
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 1
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 1
        ),
    )

    val lifeStyleItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2,
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 2
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 2
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 2
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 2
        ),
    )

    val kachikanItem = listOf(
        // 趣味 (Hobby) 카테고리
        TagItem(
            id = 1,
            title = "ヨガ",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3,
        ),
        TagItem(
            id = 2,
            title = "Hobby Activity 2",
            url = "https://www.dabur.com/Blogs/Doshas/Importance%20and%20Benefits%20of%20Yoga%201020x450.jpg",
            categoryId = 3
        ),
        TagItem(
            id = 3,
            title = "Hobby Adventure",
            url = "https://example.com/hobby3",
            categoryId = 3
        ),
        TagItem(
            id = 4,
            title = "Hobby Crafting",
            url = "https://example.com/hobby4",
            categoryId = 3
        ),
        TagItem(
            id = 5,
            title = "Hobby Gaming",
            url = "https://example.com/hobby5",
            categoryId = 3
        ),
    )

    val tags =
        mapOf("趣味" to hobbyItem, "ライフスタイル" to lifeStyleItem, "価値観" to kachikanItem)

    val uiState = AboutMeTagState(
        categoryList = testCategotyList,
        tagsByCategory = tags,
        showDialog = true
    )

    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        3
    }

    TokitokiTheme {
        AboutMeTagContents(
            uiState = uiState,
            pagerState = pagerState,
            coroutineScope = coroutineScope,
            isTest = true
        )
    }
}
