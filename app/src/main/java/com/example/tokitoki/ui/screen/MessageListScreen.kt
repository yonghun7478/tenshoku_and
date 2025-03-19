package com.example.tokitoki.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat
import com.example.tokitoki.ui.state.MessageListUiState
import com.example.tokitoki.ui.viewmodel.MessageListViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MessageListScreen(viewModel: MessageListViewModel = hiltViewModel()) {
    // 7. StateFlow로부터 상태를 수집
    val uiState by viewModel.uiState.collectAsState()

    // 8. 화면 전체 구성
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            MessageListTitle(title = "メッセージ") // "메시지" 타이틀
            Spacer(modifier = Modifier.height(16.dp))
            MatchingUsersSection(matchingUsers = uiState.matchingUsers) // "매칭" 섹션
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            PreviousChatsSection(previousChats = uiState.previousChats) // "이전 채팅" 섹션

            // 로딩 및 에러 처리 추가
            if (uiState.isLoading) {
                // 로딩 인디케이터 표시 (예: CircularProgressIndicator)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...") // 간단한 로딩 메시지
                }
            }

            if (uiState.errorMessage != null) {
                // 에러 메시지 표시
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// 9. "메시지" 타이틀 Composable
@Composable
fun MessageListTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

// 10. "매칭" 섹션 Composable
@Composable
fun MatchingUsersSection(matchingUsers: List<MatchingUser>) {
    Column {
        Text(
            text = "マッチング", // "매칭" 타이틀
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (matchingUsers.isNotEmpty()) {
            MatchingUserList(users = matchingUsers)
        } else {
            Text("No matching users yet.")
        }
    }
}

// 11. 매칭 유저 리스트 Composable
@Composable
fun MatchingUserList(users: List<MatchingUser>) {
    LazyRow {
        items(users) { user ->
            MatchingUserItem(user = user)
            Spacer(modifier = Modifier.width(8.dp)) // 아이템 간 간격
        }
    }
}

// 12. 매칭 유저 아이템 Composable
@Composable
fun MatchingUserItem(user: MatchingUser) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp) // 아이템의 너비 고정
    ) {
        AsyncImage(
            model = user.thumbnail,
            contentDescription = user.name,
            modifier = Modifier
                .size(60.dp) // 섬네일 크기
                .clip(CircleShape), // 원형 모양으로 클리핑
            contentScale = ContentScale.Crop
        )
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1, // 이름이 길 경우 한 줄로 제한
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

// 13. "이전 채팅" 섹션 Composable
@Composable
fun PreviousChatsSection(previousChats: List<PreviousChat>) {
    Column {
        Text(
            text = "Previous Chats",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (previousChats.isNotEmpty()) {
            PreviousChatList(chats = previousChats)
        } else {
            Text("No previous chats.")
        }
    }
}

// 14. 이전 채팅 리스트 Composable
@Composable
fun PreviousChatList(chats: List<PreviousChat>) {
    LazyColumn {
        items(chats) { chat ->
            PreviousChatItem(chat = chat)
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp) // 구분선 추가
        }
    }
}

// 15. 이전 채팅 아이템 Composable
@Composable
fun PreviousChatItem(chat: PreviousChat) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

        ) {
        AsyncImage(
            model = chat.thumbnail,
            contentDescription = chat.nickname,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) { // 남은 공간을 모두 차지하도록 설정
            Text(
                text = chat.nickname,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = chat.hometown, style = MaterialTheme.typography.bodySmall)
        }
        val formattedDate = chat.lastMessageDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp
        )
    }
}

// 16. 프리뷰 Composable
@Preview(showBackground = true)
@Composable
fun MessageListScreenPreview() {
    // 프리뷰에서는 ViewModel 대신 더미 데이터를 직접 사용하여 UI를 테스트합니다.
    val dummyUiState = MessageListUiState(
        matchingUsers = listOf(
            MatchingUser("1", "Alice", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            MatchingUser("2", "Bob", "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg")
        ),
        previousChats = listOf(
            PreviousChat("4", "David", "Seoul", LocalDate.of(2024, 1, 20), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg"),
            PreviousChat("5", "Eve", "Busan", LocalDate.of(2024, 1, 15), "https://img.hankyung.com/photo/202112/BF.28211341.1.jpg")
        ),
        isLoading = false,
        errorMessage = null
    )

    //  MessageListScreen(viewModel = MessageListViewModel()) // 실제 ViewModel 사용 대신
    Column {
        MessageListTitle(title = "Messages")
        Spacer(modifier = Modifier.height(16.dp))
        MatchingUsersSection(matchingUsers = dummyUiState.matchingUsers)
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        PreviousChatsSection(previousChats = dummyUiState.previousChats)
    }
}