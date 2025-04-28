package com.example.tokitoki.ui.screen.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onAshiatoClick: () -> Unit = {},
    onFavoriteUsersClick: () -> Unit = {},
    onIineSitaHitoClick: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
) {
    val state = viewModel.myPageState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadMyPageData()
    }

    // 로그아웃 완료시 콜백 호출
    LaunchedEffect(Unit) {
        viewModel.logoutCompleted.collect {
            onNavigateToSignIn()
        }
    }

    MyPageScreenContent(
        state = state,
        onEditProfileClick = viewModel::onEditProfileClick,
        onSeenMeClick = onAshiatoClick,
        onFavoriteUsersClick = onFavoriteUsersClick,
        onIineSitaHitoClick = onIineSitaHitoClick,
        onLogoutClick = { viewModel.onLogoutClick() },
    )
}

@Composable
fun MyPageScreenContent(
    state: State<MyPageState>,
    onEditProfileClick: () -> Unit,
    onSeenMeClick: () -> Unit,
    onFavoriteUsersClick: () -> Unit = {},
    onIineSitaHitoClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background), // 배경색 적용
        horizontalAlignment = Alignment.CenterHorizontally // 전체 Column 중앙 정렬
    ) {
        MyPageProfileSection(
            profileImageUrl = state.value.profileImageUrl,
            nickname = state.value.nickname,
            bio = state.value.bio,
            onEditProfileClick = onEditProfileClick
        )
        Spacer(modifier = Modifier.height(24.dp)) // 간격 증가
        MyPageBanner()
        Spacer(modifier = Modifier.height(24.dp)) // 간격 증가
        val myListItems = listOf(  // 리스트 생성
            MyPageListItemData(text = "나를 본 사람", icon = Icons.Filled.Face, onClick = onSeenMeClick),
            MyPageListItemData(
                text = "즐겨찾기",
                icon = Icons.Filled.Favorite,
                onClick = onFavoriteUsersClick
            ),
            MyPageListItemData(
                text = "내가 좋아요 한 사람",
                icon = Icons.Filled.ThumbUp,
                onClick = onIineSitaHitoClick
            ),
            MyPageListItemData(
                text = "로그아웃",
                icon = Icons.Filled.ExitToApp,
                onClick = onLogoutClick
            )
        )

        LazyColumn { // LazyColumn으로 변경
            items(myListItems) { item -> // items로 반복
                MyPageListItem(text = item.text, icon = item.icon, onClick = item.onClick)
            }
        }
    }
}


@Composable
fun MyPageProfileSection(
    profileImageUrl: String,
    nickname: String,
    bio: String?,
    onEditProfileClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // 내부 Column 중앙 정렬
        modifier = Modifier.fillMaxWidth()
    ) {
        MyPageProfilePicture(
            profileImageUrl = profileImageUrl,
            onEditProfileClick = onEditProfileClick
        )
        Spacer(modifier = Modifier.height(16.dp)) // 간격 증가
        Text(
            text = nickname,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp, // 폰트 크기 증가
                color = MaterialTheme.colorScheme.onBackground // 텍스트 색상 적용
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (!bio.isNullOrBlank()) {
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp, // 폰트 크기 증가
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f) // 텍스트 색상 및 투명도 적용
                ),
                modifier = Modifier.padding(horizontal = 32.dp), // 양쪽 여백 추가
                textAlign = androidx.compose.ui.text.style.TextAlign.Center // 텍스트 중앙 정렬
            )
        }
    }
}

@Composable
fun MyPageProfilePicture(profileImageUrl: String, onEditProfileClick: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        // 실제 앱에서는 Coil, Glide 등의 라이브러리를 사용하여 이미지 로딩
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery), // 임시 이미지
            contentDescription = "프로필 사진",
            modifier = Modifier
                .size(120.dp) // 크기 증가
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        IconButton(
            onClick = onEditProfileClick,
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp) // 여백 증가
                .size(32.dp) // 크기 증가
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "프로필 수정",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp) // 크기 증가
            )
        }
    }
}

@Composable
fun MyPageBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // 높이 증가
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium
            ) // 배경색 및 둥근 모서리 적용
            .padding(24.dp), // 패딩 증가
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "나만의 특별한 배너",
            style = MaterialTheme.typography.titleLarge.copy( // 스타일 변경
                fontWeight = FontWeight.SemiBold, // 굵기 추가
                fontSize = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun MyPageListItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // 여백 증가
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface, // 배경색 변경
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
                    tint = MaterialTheme.colorScheme.primary
                ) // 아이콘 크기, 색상 변경
                Spacer(modifier = Modifier.width(24.dp)) // 간격 증가
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) // 폰트 크기, 굵기 변경
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "다음",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            ) // 아이콘 크기, 색상 변경
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMyPageScreen() {
    MaterialTheme {
        // 1. Preview를 위한 가짜(dummy) 상태(State) 생성
        val dummyState: State<MyPageState> = remember {
            mutableStateOf(
                MyPageState(
                    isLoading = false, // 로딩 완료 상태로 가정
                    profileImageUrl = "https://placehold.co/200x200/E6E6FA/AAAAAA?text=Preview", // 예시 URL
                    nickname = "프리뷰 닉네임",
                    bio = "프리뷰용 자기소개입니다. 여기는 자기소개 글이 표시됩니다.",
                    error = null // 에러 없음 상태로 가정
                )
            )
        }

        // 2. ViewModel 인스턴스 생성 대신, 가짜 상태를 직접 Content Composable에 전달
        MyPageScreenContent(
            state = dummyState, // 생성한 가짜 상태 전달
            onEditProfileClick = { }, // Preview에서는 동작 확인 불필요
            onSeenMeClick = { },
            onIineSitaHitoClick = { },
            onLogoutClick = { },
        )
    }
}