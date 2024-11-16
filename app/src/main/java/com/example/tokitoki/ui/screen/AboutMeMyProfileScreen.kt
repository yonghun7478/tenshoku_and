package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeMyProfileAction
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeMyProfileViewModel

@Composable
fun AboutMeMyProfileScreen(
    onAboutMeProfInputScreen: () -> Unit = {},
    onIntroduceLikePageScreen: () -> Unit = {},
    viewModel: AboutMeMyProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeMyProfileContents()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->

        }
    }
}

@Composable
fun AboutMeMyProfileContents(
    modifier: Modifier = Modifier,
) {
    Column {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(LocalColor.current.lightGray)
                .weight(1f)
        ) {
            AboutMeMyProfilePicItem()
            AboutMeMyProfileNameItem()
            AboutMeMyProfileMyTag(
                modifier = Modifier.padding(top = 7.dp)
            )
            AboutMeMyProfileSelfIntroItem(
                modifier = Modifier.padding(top = 7.dp)
            )
            AboutMeMyProfileProf(
                modifier = Modifier.padding(top = 7.dp, bottom = 7.dp)
            )
        }
        AboutMeMyProfileBottomMenu()
    }
}

@Composable
fun AboutMeMyProfilePicItem(
    modifier: Modifier = Modifier,
) {
    // TODO:後にボタンを追加したい
    // TODO:DBからデータを持ってくる予定
    Box(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp))
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.profile_sample),
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )
    }
}

@Composable
fun AboutMeMyProfileNameItem(
    modifier: Modifier = Modifier,
) {
    // TODO: 名前と年齢持ってくる
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LocalColor.current.white)
            .padding(20.dp)
    ) {
        Text(
            text = "新着の相手",
            fontSize = 10.sp,
            color = LocalColor.current.red
        )
        Text(
            text = "yonghun",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "32歳",
            fontSize = 20.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        color = LocalColor.current.green,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "オンライン",
                fontSize = 10.sp,
            )
        }

    }
}

@Composable
fun AboutMeMyProfileMyTag(
    modifier: Modifier = Modifier,
) {
    // TODO: 7個以上はすべてを見るをみせる
    // TODO: すべて見るを押下したら画面遷移するように、
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topEnd = 30.dp,
                    topStart = 30.dp,
                    bottomEnd = 30.dp,
                    bottomStart = 30.dp
                )
            )
            .background(color = LocalColor.current.white)
            .padding(20.dp)
    ) {
        Text(
            text = "マイタグ",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        AboutMeMyProfileMyTagItem()
        AboutMeMyProfileMyTagItem()
        AboutMeMyProfileMyTagItem()
    }
}

@Composable
fun AboutMeMyProfileMyTagItem(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.profile_sample),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .aspectRatio(1f)
                .clip(
                    RoundedCornerShape(
                        topEnd = 10.dp,
                        topStart = 10.dp,
                        bottomEnd = 10.dp,
                        bottomStart = 10.dp
                    )
                )
        )

        Column(
            modifier = Modifier.padding(start = 10.dp),
        ) {
            Text(
                text = "title",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = "category name",
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun AboutMeMyProfileSelfIntroItem(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topEnd = 30.dp,
                    topStart = 30.dp,
                    bottomEnd = 30.dp,
                    bottomStart = 30.dp
                )
            )
            .background(color = LocalColor.current.white)
            .padding(20.dp)
    ) {
        Text(
            text = "自己紹介",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            fontSize = 13.sp
        )
    }
}

@Composable
fun AboutMeMyProfileProf(
    modifier: Modifier = Modifier,
) {
    // 基本情報
    // 　ニックネーム
    //　　年齢
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topEnd = 30.dp,
                    topStart = 30.dp,
                    bottomEnd = 30.dp,
                    bottomStart = 30.dp
                )
            )
            .background(color = LocalColor.current.white)
            .padding(20.dp)
    ) {
        Text(
            text = "プロフィール",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        AboutMeMyProfileProfBaseItem()
    }
}

@Composable
fun AboutMeMyProfileProfBaseItem(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "基本情報",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "名前",
                fontSize = 11.sp,
                color = LocalColor.current.grayColor
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "yonghun",
                fontSize = 11.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "年齢",
                fontSize = 11.sp,
                color = LocalColor.current.grayColor
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = "３３歳",
                fontSize = 11.sp
            )
        }

    }
}

@Composable
fun AboutMeMyProfileBottomMenu(
    modifier: Modifier = Modifier,
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(color = LocalColor.current.white)
            .fillMaxWidth()
    ) {
        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            text = "これではじめる",
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            action = aboutMeMyProfileAction,
            actionParam = AboutMeMyProfileAction.SUBMIT
        )

        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            text = "自己紹介文を編集する",
            textColor = LocalColor.current.blue,
            backgroundColor = LocalColor.current.white,
            action = aboutMeMyProfileAction,
            actionParam = AboutMeMyProfileAction.FIX_PROFILE_INFO
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileContentsPreview() {
    TokitokiTheme {
        AboutMeMyProfileContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileImagePreview() {
    TokitokiTheme {
        AboutMeMyProfilePicItem()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileMyTagPreview() {
    TokitokiTheme {
        AboutMeMyProfileMyTag()
    }
}


@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileMyTagItemPreview() {
    TokitokiTheme {
        AboutMeMyProfileMyTagItem()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileSelfIntroItemPreview() {
    TokitokiTheme {
        AboutMeMyProfileSelfIntroItem()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileProfItemPreview() {
    TokitokiTheme {
        AboutMeMyProfileProf()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileProfBaseItemPreview() {
    TokitokiTheme {
        AboutMeMyProfileProfBaseItem()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeMyProfileBottomMenuPreview() {
    TokitokiTheme {
        AboutMeMyProfileBottomMenu()
    }
}