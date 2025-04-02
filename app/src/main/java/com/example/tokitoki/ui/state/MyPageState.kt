package com.example.tokitoki.ui.state

object MyPageDummyData {
    val myPageState = MyPageState(
        profileImageUrl = "https://via.placeholder.com/150", // 실제 앱에서는 사용자 프로필 이미지 URL 사용
        nickname = "김데이팅",
        bio = "만나서 반갑습니다! \n서로 좋은 인연 만들어요."
    )
}

// UI 상태를 위한 데이터 클래스
data class MyPageState(
    val profileImageUrl: String,
    val nickname: String,
    val bio: String?
)