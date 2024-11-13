import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.model.MySelfSentenceItem
import com.example.tokitoki.ui.screen.AboutMeProfInputContents
import com.example.tokitoki.ui.theme.TokitokiTheme

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun AboutMeProfInputFirstPageSnapShotTest() {
    val myselfSentenceEntities = listOf(
        MySelfSentenceItem(
            id = 1,
            type = "タイプ１",
            typeColor = "FF36C2CE",
            sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n"
        ),
        MySelfSentenceItem(
            id = 2,
            type = "タイプ2",
            typeColor = "FF4B89DC",
            sentence = "Curabitur pretium tincidunt lacus. Nulla gravida orci a odio. Nullam varius, turpis et commodo pharetra, est eros bibendum elit, nec luctus magna felis sollicitudin mauris. Integer in mauris eu nibh euismod gravida. Duis ac tellus et risus vulputate vehicula. Donec lobortis risus a elit. Etiam tempor. Ut ullamcorper, ligula eu tempor congue, eros est euismod turpis, id tincidunt sapien risus a quam. Maecenas fermentum consequat mi. Donec fermentum. Pellentesque malesuada nulla a mi. Duis sapien sem, aliquet nec, commodo eget, consequat quis, neque. Aliquam faucibus, elit ut dictum aliquet, felis nisl adipiscing sapien, sed malesuada diam lacus eget erat. Cras mollis scelerisque nunc. Nullam arcu. Aliquam consequat. \n"
        ),
        MySelfSentenceItem(
            id = 3,
            type = "タイプ3",
            typeColor = "FFF35A1B",
            sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus feugiat sapien quis turpis luctus, id convallis mauris malesuada. Ut tincidunt sapien risus, sit amet accumsan elit varius ut. Sed condimentum malesuada ultricies. In hac habitasse platea dictumst.\n"
        ),
    )

    val pagerState = rememberPagerState {
        3
    }

    // snapshotではきいてない
    LaunchedEffect(Unit) {
        pagerState.scrollToPage(2)
    }

    TokitokiTheme {
        AboutMeProfInputContents(
            pagerState = pagerState,
            itemList = myselfSentenceEntities
        )
    }
}
