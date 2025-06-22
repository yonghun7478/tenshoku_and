package com.example.tokitoki.ui.state

object MyPageDummyData {
    val myPageState = MyPageState(
        profileImageUrl = "https://via.placeholder.com/150", // 실제 앱에서는 사용자 프로필 이미지 URL 사용
        nickname = "김데이팅",
        age = 30, // 만나이 더미 데이터 추가
    )
}

// UI 상태를 위한 데이터 클래스
data class MyPageState(
    val isLoading: Boolean = true, // 로딩 MyPageState
    val profileImageUrl: String = "", // 프로필 이미지 URL
    val nickname: String = "", // 닉네임
    val birthday: String = "", // 생년월일 (나이 계산을 위해 유지)
    val age: Int = 0, // 만나이 필드 추가
    val bio: String? = null, // 소개글 (Nullable)
    val error: String? = null // 에러 메시지 (Nullable)
)