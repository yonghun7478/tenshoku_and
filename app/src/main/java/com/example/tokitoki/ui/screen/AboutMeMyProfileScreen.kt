package com.example.tokitoki.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeMyProfileAction
import com.example.tokitoki.ui.model.MyProfileItem
import com.example.tokitoki.ui.model.MyTagItem
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.state.AboutMeMyProfileEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMeMyProfileViewModel

@Composable
fun AboutMeMyProfileScreen(
    uri: Uri = Uri.EMPTY,
    isFromMyPage: Boolean = false,
    birthday: String? = null,
    name: String? = null,
    selfSentenceId: Int? = null,
    tagIds: ArrayList<MyTagItem>? = null,
    onAboutMeProfInputScreen: (Int) -> Unit = {},
    onAboutMeNameScreen: (String) -> Unit = {},
    onAboutMeBirthDayScreen: (String) -> Unit = {},
    onAboutMeTagScreen: (String, Boolean) -> Unit = { _, _ -> },
    onAboutMePhotoUploadScreen: (Uri) -> Unit = {},
    onMainScreen:() -> Unit = {},
    onFavoriteTagScreen: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: AboutMeMyProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AboutMeMyProfileContents(
        uri = uiState.uri,
        myProfileItem = uiState.myProfileItem,
        aboutMeMyProfileAction = viewModel::aboutMeMyProfileAction,
        isFromMyPage = isFromMyPage
    )

    LaunchedEffect(uri) {
        viewModel.init(uri, birthday, name, selfSentenceId, tagIds)

        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMeMyProfileEvent.NOTHING -> {

                }

                is AboutMeMyProfileEvent.ACTION -> {
                    when (event.action) {
                        AboutMeMyProfileAction.SUBMIT -> {
                            val result = viewModel.registerMyProfile()
                            if (result) {
                                if (isFromMyPage) {
                                    // 마이페이지를 통해 접근한 경우, 프로필 저장 성공 시 별도의 화면 이동을 하지 않습니다.
                                    // 사용자가 이전 화면(마이페이지)으로 돌아갈 수 있도록 합니다.
                                    onNavigateUp()
                                } else {
                                    onMainScreen()
                                }
                            }
                        }

                        AboutMeMyProfileAction.CHECK_EVERYTHING -> {
                            onFavoriteTagScreen()
                        }

                        AboutMeMyProfileAction.FIX_BIRTHDAY -> {
                            onAboutMeBirthDayScreen(viewModel.getBirthDay())
                        }

                        AboutMeMyProfileAction.FIX_MY_SELF_SENTENCE -> {
                            onAboutMeProfInputScreen(viewModel.getMySelfSentenceId())
                        }

                        AboutMeMyProfileAction.FIX_MY_TAG -> {
                            onAboutMeTagScreen(viewModel.getMyTags(), true)
                        }

                        AboutMeMyProfileAction.FIX_NAME -> {
                            onAboutMeNameScreen(viewModel.uiState.value.myProfileItem.name)
                        }

                        AboutMeMyProfileAction.FIX_PICTURE -> {
                            onAboutMePhotoUploadScreen(uri)
                        }

                        AboutMeMyProfileAction.NOTHING -> {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutMeMyProfileContents(
    modifier: Modifier = Modifier,
    uri: Uri = Uri.EMPTY,
    myProfileItem: MyProfileItem = MyProfileItem(),
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
    isFromMyPage: Boolean = false
) {
    Column {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(LocalColor.current.lightGray)
                .weight(1f)
        ) {
            AboutMeMyProfilePicItem(
                thumbnailImageUri = uri,
                aboutMeMyProfileAction = aboutMeMyProfileAction
            )
            AboutMeMyProfileNameItem(
                name = myProfileItem.name,
                age = myProfileItem.age,
                aboutMeMyProfileAction = aboutMeMyProfileAction
            )
            AboutMeMyProfileMyTag(
                modifier = Modifier.padding(top = 7.dp),
                myTagItems = myProfileItem.myTagItems,
                aboutMeMyProfileAction = aboutMeMyProfileAction
            )
            AboutMeMyProfileSelfIntroItem(
                modifier = Modifier.padding(top = 7.dp),
                mySelfSentence = myProfileItem.mySelfSentence,
                aboutMeMyProfileAction = aboutMeMyProfileAction
            )
            AboutMeMyProfileProf(
                modifier = Modifier.padding(top = 7.dp, bottom = 7.dp),
                name = myProfileItem.name,
                age = myProfileItem.age,
                aboutMeMyProfileAction = aboutMeMyProfileAction
            )
        }
        AboutMeMyProfileBottomMenu(
            aboutMeMyProfileAction = aboutMeMyProfileAction,
            isFromMyPage = isFromMyPage
        )
    }
}

@Composable
fun AboutMeMyProfilePicItem(
    modifier: Modifier = Modifier,
    thumbnailImageUri: Uri = Uri.EMPTY,
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {}
) {
    val painter = if (thumbnailImageUri.path?.isNotEmpty() == true) {
        rememberAsyncImagePainter(thumbnailImageUri) // 직접 사용
    } else {
        painterResource(id = R.drawable.no_image_2) // 직접 사용
    }
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
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )

        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .align(Alignment.BottomCenter),
            backgroundColor = LocalColor.current.blue,
            text = "写真編集",
            textColor = LocalColor.current.white,
            action = aboutMeMyProfileAction,
            actionParam = AboutMeMyProfileAction.FIX_PICTURE
        )
    }
}

@Composable
fun AboutMeMyProfileNameItem(
    modifier: Modifier = Modifier,
    name: String = "",
    age: String = "",
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {}
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
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_NAME)
                },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${age}歳",
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_BIRTHDAY)
                },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }

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
    myTagItems: List<MyTagItem> = listOf(),
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
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
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "マイタグ",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_MY_TAG)
                },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }

        for (item in myTagItems.take(6)) {
            AboutMeMyProfileMyTagItem(
                tagTitle = item.title,
                categoryTitle = item.categoryTitle,
                url = item.url
            )
        }

        if (myTagItems.size > 6) {
            TkBtn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                text = "すべて見る",
                textColor = LocalColor.current.blue,
                backgroundColor = LocalColor.current.white,
                action = aboutMeMyProfileAction,
                actionParam = AboutMeMyProfileAction.CHECK_EVERYTHING
            )
        }
    }
}

@Composable
fun AboutMeMyProfileMyTagItem(
    modifier: Modifier = Modifier,
    tagTitle: String = "",
    categoryTitle: String = "",
    url: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = url,
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
                text = tagTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = categoryTitle,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun AboutMeMyProfileSelfIntroItem(
    modifier: Modifier = Modifier,
    mySelfSentence: String = "",
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
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
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "自己紹介",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_MY_SELF_SENTENCE)
                },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }

        Text(
            text = mySelfSentence,
            fontSize = 13.sp
        )
    }
}

@Composable
fun AboutMeMyProfileProf(
    modifier: Modifier = Modifier,
    name: String = "",
    age: String = "",
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
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

        AboutMeMyProfileProfBaseItem(
            name = name,
            age = age,
            aboutMeMyProfileAction = aboutMeMyProfileAction
        )
    }
}

@Composable
fun AboutMeMyProfileProfBaseItem(
    modifier: Modifier = Modifier,
    name: String = "",
    age: String = "",
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Row {
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
                    text = name,
                    fontSize = 11.sp
                )
            }


            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_NAME)
                    },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Row {
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
                    text = "${age}歳",
                    fontSize = 11.sp
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        aboutMeMyProfileAction(AboutMeMyProfileAction.FIX_BIRTHDAY)
                    },
                text = "編集",
                fontSize = 12.sp,
                color = LocalColor.current.grayColor
            )
        }
    }
}

@Composable
fun AboutMeMyProfileBottomMenu(
    modifier: Modifier = Modifier,
    aboutMeMyProfileAction: (AboutMeMyProfileAction) -> Unit = {},
    isFromMyPage: Boolean = false
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
            text = if (isFromMyPage) "修正する" else "これではじめる",
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            action = aboutMeMyProfileAction,
            actionParam = AboutMeMyProfileAction.SUBMIT
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