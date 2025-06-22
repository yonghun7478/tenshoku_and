package com.example.tokitoki.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.state.MyPageState
import com.example.tokitoki.ui.viewmodel.MyPageViewModel
import androidx.compose.ui.platform.LocalContext
import java.io.File
import com.example.tokitoki.R
import coil.size.Size
import com.example.tokitoki.ui.theme.LocalColor
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tokitoki.ui.theme.TokitokiTheme

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onAshiatoClick: () -> Unit = {},
    onFavoriteUsersClick: () -> Unit = {},
    onIineSitaHitoClick: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    onNavigateToAboutMeMyProfile: (String) -> Unit = {}
) {
    val state = viewModel.myPageState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadMyPageData()
    }

    // ログアウト完了時にコールバックを呼び出す
    LaunchedEffect(Unit) {
        viewModel.logoutCompleted.collect {
            onNavigateToSignIn()
        }
    }

    MyPageScreenContent(
        state = state,
        onEditProfileClick = { imageUrl -> onNavigateToAboutMeMyProfile(imageUrl) },
        onSeenMeClick = onAshiatoClick,
        onFavoriteUsersClick = onFavoriteUsersClick,
        onIineSitaHitoClick = onIineSitaHitoClick,
        onLogoutClick = { viewModel.onLogoutClick() },
    )
}

@Composable
fun MyPageScreenContent(
    state: State<MyPageState>,
    onEditProfileClick: (String) -> Unit,
    onSeenMeClick: () -> Unit,
    onFavoriteUsersClick: () -> Unit = {},
    onIineSitaHitoClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .background(Color.White), // 배경색을 흰색으로 변경
        horizontalAlignment = Alignment.CenterHorizontally // 全体のColumn을中央揃え
    ) {
        MyPageProfileSection(
            profileImageUrl = state.value.profileImageUrl,
            nickname = state.value.nickname,
            age = state.value.age,
            onEditProfileClick = onEditProfileClick
        )
        Spacer(modifier = Modifier.height(24.dp)) // 間隔を増加
        MyPageBanner(
            imageUrl = "https://picsum.photos/400/100",
            onClick = { }
        )
        Spacer(modifier = Modifier.height(24.dp)) // 間隔を増加
        val myListItems = listOf(  // リストを生成
            MyPageListItemData(text = "足跡", icon = Icons.Filled.Face, onClick = onSeenMeClick),
            MyPageListItemData(
                text = "お気に入り",
                icon = Icons.Filled.Favorite,
                onClick = onFavoriteUsersClick
            ),
            MyPageListItemData(
                text = "いいねした人",
                icon = Icons.Filled.ThumbUp,
                onClick = onIineSitaHitoClick
            ),
            MyPageListItemData(
                text = "ログアウト",
                icon = Icons.Filled.ExitToApp,
                onClick = onLogoutClick
            )
        )

        LazyColumn { // LazyColumnに変更
            items(myListItems) { item -> // itemsで繰り返し
                MyPageListItem(text = item.text, icon = item.icon, onClick = item.onClick)
            }
        }
    }
}


@Composable
fun MyPageProfileSection(
    profileImageUrl: String,
    nickname: String,
    age: Int,
    onEditProfileClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // 内部のColumnを中央揃え
        modifier = Modifier.fillMaxWidth()
    ) {
        MyPageProfilePicture(
            profileImageUrl = profileImageUrl,
            onEditProfileClick = onEditProfileClick
        )
        Spacer(modifier = Modifier.height(16.dp)) // 間隔を増加
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = nickname,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, // フォントのサイズを20.spに変更
                    color = MaterialTheme.colorScheme.onBackground // テキストの色を適用
                )
            )
            Spacer(modifier = Modifier.width(8.dp)) // 間隔を追加
            Text(
                text = "${age}歳", // '歳'に変更
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp, // フォントのサイズを20.spに変更
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f) // テキストの色と透明度を適用
                ),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun MyPageProfilePicture(profileImageUrl: String, onEditProfileClick: (String) -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(if (profileImageUrl.startsWith("/data/user/")) File(profileImageUrl) else profileImageUrl)
                .error(R.drawable.no_image_icon) // 画像のロードに失敗した場合の代替画像
                .placeholder(R.drawable.no_image_icon) // 画像のロード中に表示される一時的な画像
                .crossfade(durationMillis = 300) // 滑らかな変換アニメーション
                .diskCachePolicy(coil.request.CachePolicy.ENABLED) // ディスクキャッシュを有効にする
                .memoryCachePolicy(coil.request.CachePolicy.ENABLED) // メモリキャッシュを有効にする
                .size(Size.ORIGINAL) // 元の画像サイズを使用
                .build(),
            contentDescription = "プロフィール写真",
            modifier = Modifier
                .size(120.dp) // サイズを増加
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        IconButton(
            onClick = { onEditProfileClick(profileImageUrl) },
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp) // 間隔を増加
                .size(32.dp) // サイズを増加
                .background(LocalColor.current.blue, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "プロフィールを編集",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp) // サイズを増加
            )
        }
    }
}

@Composable
fun MyPageBanner(
    imageUrl: String = "https://picsum.photos/400/100",
    onClick: () -> Unit = {}
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "マイページバナー",
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.no_image_icon)
    )
}

@Composable
fun MyPageListItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // 間隔を増加
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // 배경색을 흰색으로 변경
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                ) // アイコンのサイズと色を変更
                Spacer(modifier = Modifier.width(24.dp)) // 間隔を増加
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) // フォントのサイズと太さを変更
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "次へ",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            ) // アイコンのサイズと色を変更
        }
    }
}


@Preview(showBackground = true, apiLevel = 31)
@Composable
fun MyPageScreenContentPreview() {
    TokitokiTheme {
// 1. Preview用の偽の(dummy)状態(State)を生成
        val dummyState: State<MyPageState> = remember {
            mutableStateOf(
                MyPageState(
                    isLoading = false, // ロード完了状態と仮定
                    profileImageUrl = "https://placehold.co/200x200/E6E6FA/AAAAAA?text=Preview", // 例のURL
                    nickname = "プレビューニックネーム", // ニックネーム
                    birthday = "1990-01-01", // 年齢計算のためのダミー生年月日 (UIに直接表示されません)
                    age = 34, // 満年齢追加
                    bio = null, // 自己紹介削除
                    error = null // エラーなしと仮定
                )
            )
        }

        // 2. ViewModelインスタンスを生成せずに、偽の状態を直接Content Composableに渡す
        MyPageScreenContent(
            state = dummyState, // 生成した偽の状態を渡す
            onEditProfileClick = { }, // Previewでは動作確認不要
            onSeenMeClick = { },
            onFavoriteUsersClick = { },
            onIineSitaHitoClick = { },
            onLogoutClick = { },
        )
    }
}